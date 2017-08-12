package com.error22.karonda.ir;

public class KClass implements IType {
	private String name;
	private ClassType type;
	private boolean specialResolve;
	
	public KClass(String name, ClassType type, boolean specialResolve) {
		this.name = name;
		this.type = type;
		this.specialResolve = specialResolve;
	}

}
