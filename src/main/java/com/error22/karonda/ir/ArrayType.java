package com.error22.karonda.ir;

public class ArrayType implements IType {
	private IType type;

	public ArrayType(IType type) {
		this.type = type;
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}

}
