package com.error22.karonda.ir;

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
}
