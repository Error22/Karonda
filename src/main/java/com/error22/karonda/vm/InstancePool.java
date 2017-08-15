package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.PrimitiveType;

public class InstancePool {
	private Map<KClass, Boolean> staticInitMap;
	private Map<FieldSignature, IObject> staticFields;

	public InstancePool() {
		staticInitMap = new ConcurrentHashMap<KClass, Boolean>();
		staticFields = new HashMap<FieldSignature, IObject>();
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

}
