package com.error22.karonda.ir;

public class KClass implements IType {
	private String name, superName;
	private ClassType type;
	private boolean specialResolve;
	
	public KClass(String name, ClassType type, boolean specialResolve, String superName) {
		this.name = name;
		this.type = type;
		this.specialResolve = specialResolve;
		this.superName = superName;
	}

}
