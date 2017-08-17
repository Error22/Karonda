package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.javatuples.Pair;

import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.PrimitiveType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class InstancePool {
	private Map<KClass, Boolean> staticInitMap;
	private Map<FieldSignature, IObject> staticFields;
	private Map<UUID, ObjectInstance> objects;
	private BiMap<Pair<IType, Object>, UUID> runtimeClasses;

	public InstancePool() {
		staticInitMap = new ConcurrentHashMap<KClass, Boolean>();
		staticFields = new HashMap<FieldSignature, IObject>();
		objects = new HashMap<UUID, ObjectInstance>();
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

	public void setStaticField(KClass clazz, FieldSignature field, IObject value) {
		System.out.println("setStaticField " + field + " " + value);
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		staticFields.put(field, value);
	}

	public IObject getStaticField(KClass clazz, FieldSignature field) {
		System.out.println("getStaticField " + field);
		if (!hasStaticInit(clazz))
			throw new IllegalStateException("Class has not been staticly initialized");
		return staticFields.get(field);
	}

	public ObjectReference createInstance(KClass clazz) {
		UUID id = UUID.randomUUID();
		ObjectInstance instance = new ObjectInstance(id, clazz);
		objects.put(id, instance);
		return instance.makeReference();
	}

	public ObjectReference createArray(int size) {
		UUID id = UUID.randomUUID();
		ObjectInstance instance = new ObjectInstance(id, size);
		objects.put(id, instance);
		return instance.makeReference();
	}

	public ObjectReference getRuntimeClass(ClassPool pool, IType type, KClass clazz) {
		Pair<IType, Object> key = new Pair<IType, Object>(type, null);
		if (runtimeClasses.containsKey(key)) {
			return objects.get(runtimeClasses.get(key)).makeReference();
		}

		KClass classClass = pool.getClass("java/lang/Class", null);
		UUID id = UUID.randomUUID();
		ObjectInstance instance = new ObjectInstance(id, classClass);
		objects.put(id, instance);
		runtimeClasses.put(key, id);
		return instance.makeReference();
	}

}
