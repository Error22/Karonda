package com.error22.karonda.vm;

import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.javatuples.Pair;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
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

	public InstancePool() {
		staticInitMap = new ConcurrentHashMap<KClass, Boolean>();
		staticFields = new Object2ObjectOpenHashMap<FieldSignature, int[]>();
		objects = new Int2ObjectOpenHashMap<ObjectInstance>();
		objectIds = new BitSet();
		objectIds.set(0);
		idHint = 1;
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
		System.out.println("setStaticField " + field + " " + value);
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		staticFields.put(field, value);
	}

	public int[] getStaticField(KClass clazz, FieldSignature field) {
		System.out.println("getStaticField " + field);
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		return staticFields.get(field);
	}

	private int allocateObjectId() {
		int id = objectIds.nextClearBit(idHint);
		objectIds.set(id);
		idHint = id + 1;
		return id;
	}

	private void deallocateObjectId(int id) {
		objectIds.clear(id);
		idHint = id;
	}

	public int createInstance(KClass clazz) {
		int id = allocateObjectId();
		ObjectInstance instance = new ObjectInstance(id, clazz);
		objects.put(id, instance);
		return id;
	}

	public int createArray(ArrayType type, int size) {
		int id = allocateObjectId();
		ObjectInstance instance = new ObjectInstance(type, id, size);
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
		ObjectInstance instance = new ObjectInstance(id, classClass);
		objects.put(id, instance);
		runtimeClasses.put(key, id);
		return id;
	}

	public ObjectInstance getObject(int id) {
		return objects.get(id);
	}
}
