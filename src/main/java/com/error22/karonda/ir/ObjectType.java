package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public class ObjectType implements IType {
	private String name;

	public ObjectType(String name) {
		this.name = name;
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}

	@Override
	public IObject getDefaultValue() {
		throw new NotImplementedException();
	}
}
