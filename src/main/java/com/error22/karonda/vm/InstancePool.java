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

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class InstancePool {
	private Map<KClass, Boolean> staticInitMap;
	private Object2ObjectOpenHashMap<FieldSignature, int[]> staticFields;
	private Int2ObjectOpenHashMap<ObjectInstance> objects;
	private BitSet objectIds;
	private int idHint;
	private BiMap<Pair<IType, Object>, Integer> runtimeClasses;
	private List<Integer> forceLoaded;

	public InstancePool() {
		staticInitMap = new ConcurrentHashMap<KClass, Boolean>();
		staticFields = new Object2ObjectOpenHashMap<FieldSignature, int[]>();
		objects = new Int2ObjectOpenHashMap<ObjectInstance>();
		objectIds = new BitSet();
		objectIds.set(0);
		idHint = 1;
		forceLoaded = new ArrayList<Integer>();
		runtimeClasses = HashBiMap.create();
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
		if (idHint > id)
			idHint = id;
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
			if (idHint > i)
				idHint = i;
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
}
