package com.error22.karonda.ir;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.error22.karonda.vm.ClassPool;

public class KClass {
	private String name, superName;
	private String[] interfaceNames;
	private ClassType type;
	private boolean specialMethodResolve, resolved;
	private Map<MethodSignature, KMethod> methods;
	private Map<FieldSignature, KField> fields;
	private KClass superClass;
	private KClass[] interfaces;

	public KClass(String name, ClassType type, boolean specialMethodResolve, String superName,
			String[] interfaceNames) {
		this.name = name;
		this.type = type;
		this.specialMethodResolve = specialMethodResolve;
		this.superName = superName;
		this.interfaceNames = interfaceNames;
		methods = new ConcurrentHashMap<MethodSignature, KMethod>();
		fields = new ConcurrentHashMap<FieldSignature, KField>();
	}

	public void bootstrapResolve(ClassPool pool) {
		if (resolved)
			return;
		resolved = true;

		superClass = superName != null ? pool.bootstrapResolve(superName) : null;

		interfaces = new KClass[interfaceNames.length];
		for (int i = 0; i < interfaceNames.length; i++) {
			interfaces[i] = pool.bootstrapResolve(interfaceNames[i]);
		}
	}

	public void addMethod(KMethod method) {
		methods.put(method.getSignature(), method);
	}

	public KMethod getMethod(MethodSignature signature) {
		return methods.get(signature);
	}

	public void addField(KField field) {
		fields.put(field.getSignature(), field);
	}

	public KField getField(FieldSignature signature) {
		return fields.get(signature);
	}

	public String getName() {
		return name;
	}

}
