package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public enum PrimitiveType implements IType {
	Void,
	Byte,
	Boolean,
	Char,
	Short,
	Int,
	Long,
	Float,
	Double;

	@Override
	public boolean isCategoryTwo() {
		return this == Long || this == Double;
	}

	@Override
	public IObject getDefaultValue() {
		switch (this) {
		case Int:
			return new PrimitiveObject(Int, 0);
		default:
			throw new NotImplementedException();
		}
	}
}
