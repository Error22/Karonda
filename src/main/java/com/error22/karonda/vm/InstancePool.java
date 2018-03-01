package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.javatuples.Pair;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class InstancePool {
	private ClassPool classPool;
	private Map<KClass, Boolean> staticInitMap;
	private Object2ObjectOpenHashMap<FieldSignature, int[]> staticFields;
	private Int2ObjectOpenHashMap<ObjectInstance> objects;
	private BitSet objectIds;
	private int idHint;
	private BiMap<Pair<IType, Object>, Integer> runtimeClasses;
	private List<Integer> forceLoaded;
	private Int2ObjectMap<String> objToStringMap;
	private Object2IntMap<String> stringToObjMap;

	public InstancePool(ClassPool classPool) {
		this.classPool = classPool;
		staticInitMap = new ConcurrentHashMap<KClass, Boolean>();
		staticFields = new Object2ObjectOpenHashMap<FieldSignature, int[]>();
		objects = new Int2ObjectOpenHashMap<ObjectInstance>();
		objectIds = new BitSet();
		objectIds.set(0);
		idHint = 1;
		forceLoaded = new ArrayList<Integer>();
		runtimeClasses = HashBiMap.create();
		objToStringMap = new Int2ObjectOpenHashMap<String>();
		stringToObjMap = new Object2IntOpenHashMap<String>();
	}

	public KMethod staticInit(KClass clazz) {
		if (staticInitMap.containsKey(clazz))
			return null;
		staticInitMap.put(clazz, true);
		for (KField field : clazz.getFields()) {
			if (field.isStatic()) {
				FieldSignature signature = field.getSignature();
				staticFields.put(signature, signature.getType().getDefaultValue());
			}
		}
		return clazz.getMethod(new MethodSignature(clazz.getName(), "<clinit>", PrimitiveType.Void));
	}

	public boolean hasStaticInit(KClass clazz) {
		return staticInitMap.containsKey(clazz);
	}

	public void setStaticField(KClass clazz, FieldSignature field, int[] value) {
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		if (value == null)
			throw new IllegalArgumentException("Null values not accepted");
		staticFields.put(field, value);
	}

	public int[] getStaticField(KClass clazz, FieldSignature field) {
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		if (!staticFields.containsKey(field))
			throw new IllegalArgumentException("Unknown field");
		return staticFields.get(field);
	}

	private int allocateObjectId() {
		int id = objectIds.nextClearBit(idHint);
		objectIds.set(id);
		idHint = id + 1;
		return id;
	}

	private void deallocateObjectId(int id) {
		if (forceLoaded.contains(id)) {
			throw new IllegalArgumentException("Object is force loaded");
		}
		objects.remove(id);
		objectIds.clear(id);
		if (idHint > id) {
			idHint = id;
		}
		if (objToStringMap.containsKey(id)) {
			String value = objToStringMap.remove(id);
			if (stringToObjMap.getInt(value) == id) {
				stringToObjMap.removeInt(value);
			}
		}
	}

	public int getStringInstance(String value) {
		if (stringToObjMap.containsKey(value)) {
			return stringToObjMap.getInt(value);
		}

		KClass targetClass = classPool.getClass(ObjectType.STRING_TYPE.getName(), null);
		int reference = createInstance(targetClass, ObjectType.STRING_TYPE);

		char[] chars = value.toCharArray();
		int valueRef = createArray(classPool, ArrayType.CHAR_ARRAY, chars.length);
		ObjectInstance valueInst = getObject(valueRef);
		for (int i = 0; i < chars.length; i++) {
			valueInst.setArrayElement(i, new int[] { chars[i] });
		}
		getObject(reference).setField(STRING_VALUE_FIELD, new int[] { valueRef });

		stringToObjMap.put(value, reference);
		objToStringMap.put(reference, value);
		return reference;
	}

	public String getStringContent(int obj) {
		if (objToStringMap.containsKey(obj)) {
			return objToStringMap.get(obj);
		}

		ObjectInstance inst = getObject(getObject(obj).getField(STRING_VALUE_FIELD)[0]);
		int size = inst.getArraySize();
		char[] chars = new char[size];

		for (int i = 0; i < size; i++) {
			chars[i] = (char) inst.getArrayElement(i)[0];
		}

		String value = new String(chars);
		objToStringMap.put(obj, value);
		if (!stringToObjMap.containsKey(value)) {
			stringToObjMap.put(value, obj);
		}
		return value;
	}

	public int createInstance(KClass clazz, ObjectType type) {
		int id = allocateObjectId();
		ObjectInstance instance = new ObjectInstance(id, clazz, type);
		objects.put(id, instance);
		return id;
	}

	public int createArray(ClassPool pool, ArrayType type, int size) {
		int id = allocateObjectId();
		KClass objectClass = pool.getClass("java/lang/Object", null);
		ObjectInstance instance = new ObjectInstance(objectClass, type, id, size);
		objects.put(id, instance);
		return id;
	}

	public int getRuntimeClass(ClassPool pool, IType type, KClass clazz) {
		Pair<IType, Object> key = new Pair<IType, Object>(type, null);
		if (runtimeClasses.containsKey(key)) {
			return runtimeClasses.get(key);
		}

		KClass classClass = pool.getClass("java/lang/Class", null);
		int id = allocateObjectId();
		ObjectInstance instance = new ObjectInstance(id, classClass, ObjectType.CLASS_TYPE);
		objects.put(id, instance);
		runtimeClasses.put(key, id);
		return id;
	}

	public IType getTypeFromRuntimeClass(int ref) {
		if (!runtimeClasses.inverse().containsKey(ref))
			throw new IllegalArgumentException("Invlaid ref " + ref);
		return runtimeClasses.inverse().get(ref).getValue0();
	}

	public int garbageCollect(List<KThread> threads) {
		BitSet foundMap = new BitSet();
		foundMap.set(0);
		for (int i : forceLoaded) {
			foundMap.set(i);
		}
		for (Entry<FieldSignature, int[]> entry : staticFields.entrySet()) {
			if (entry.getKey().getType().isReference()) {
				markObject(foundMap, entry.getValue()[0]);
			}
		}
		for (int id : runtimeClasses.values()) {
			markObject(foundMap, id);
		}
		for (KThread thread : threads) {
			for (int id : thread.getLocks()) {
				markObject(foundMap, id);
			}
			for (StackFrame frame : thread.getFrames()) {
				int[] locals = frame.getLocals();
				boolean[] localsObjectMap = frame.getLocalsObjectMap();
				int[] stack = frame.getStack();
				boolean[] stackObjectMap = frame.getStackObjectMap();
				int stackPointer = frame.getStackPointer();
				for (int i = 0; i < locals.length; i++) {
					if (localsObjectMap[i]) {
						markObject(foundMap, locals[i]);
					}
				}
				for (int i = 0; i < stackPointer; i++) {
					if (stackObjectMap[i]) {
						markObject(foundMap, stack[i]);
					}
				}
			}
		}
		objectIds.andNot(foundMap);
		int deallocated = objectIds.cardinality();
		for (int i = objectIds.nextSetBit(0); i >= 0; i = objectIds.nextSetBit(i + 1)) {
			objects.remove(i);
			if (idHint > i) {
				idHint = i;
			}
			if (objToStringMap.containsKey(i)) {
				String value = objToStringMap.remove(i);
				if (stringToObjMap.getInt(value) == i) {
					stringToObjMap.removeInt(value);
				}
			}
			if (i == Integer.MAX_VALUE) {
				break;
			}
		}
		objectIds = foundMap;
		return deallocated;
	}

	private void markObject(BitSet foundMap, int id) {
		if (id == 0 || foundMap.get(id)) {
			return;
		}
		foundMap.set(id);
		ObjectInstance instance = objects.get(id);
		if (instance.isArray()) {
			if (instance.getArrayType().getDimensions() > 1 || instance.getArrayType().getType().isReference()) {
				for (int i = 0; i < instance.getArraySize(); i++) {
					markObject(foundMap, instance.getArrayElement(i)[0]);
				}
			}
		} else {
			for (Entry<FieldSignature, int[]> entry : instance.getFields().entrySet()) {
				if (entry.getKey().getType().isReference()) {
					markObject(foundMap, entry.getValue()[0]);
				}
			}
		}
	}

	public ObjectInstance getObject(int id) {
		if (!objectIds.get(id))
			throw new IllegalArgumentException("No object with id " + id + " exists");
		return objects.get(id);
	}

	public void forceLoad(int id) {
		if (!objectIds.get(id))
			throw new IllegalArgumentException("No object with id " + id + " exists");
		forceLoaded.add(id);
	}

	public void unforceLoad(int id) {
		if (!objectIds.get(id))
			throw new IllegalArgumentException("No object with id " + id + " exists");
		forceLoaded.remove(id);
	}

	private static final FieldSignature STRING_VALUE_FIELD = new FieldSignature(ObjectType.STRING_TYPE.getName(),
			"value", ArrayType.CHAR_ARRAY);
}
