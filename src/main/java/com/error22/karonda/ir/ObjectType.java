package com.error22.karonda.ir;

public class ObjectType implements IType {
	private String name;

	public ObjectType(String name) {
		this.name = name;
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}
}
