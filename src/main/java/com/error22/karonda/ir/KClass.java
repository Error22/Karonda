package com.error22.karonda.ir;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KClass implements IType {
	private String name, superName;
	private ClassType type;
	private boolean specialResolve;
	private Map<MethodSignature, KMethod> methods;

	public KClass(String name, ClassType type, boolean specialResolve, String superName) {
		this.name = name;
		this.type = type;
		this.specialResolve = specialResolve;
		this.superName = superName;
		methods = new ConcurrentHashMap<MethodSignature, KMethod>();
	}

	public void addMethod(KMethod method) {
		methods.put(method.getSignature(), method);
	}

	public KMethod getMethod(MethodSignature signature) {
		return methods.get(signature);
	}

}
