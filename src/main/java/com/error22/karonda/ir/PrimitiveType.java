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
		case Short:
			return new PrimitiveObject(Short, (short) 0);
		case Int:
			return new PrimitiveObject(Int, 0);
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public IObject fieldWrap(IObject value) {
		Object pvalue = ((PrimitiveObject) value).getValue();
		switch (this) {
		case Byte:
			return new PrimitiveObject(this, ((Number) pvalue).byteValue());
		case Boolean:
			return new PrimitiveObject(this, ((Number) pvalue).intValue() != 0);
		case Char:
			return new PrimitiveObject(this, (char) ((Number) pvalue).intValue());
		case Short:
			return new PrimitiveObject(this, ((Number) pvalue).shortValue());
		case Int:
			return new PrimitiveObject(this, ((Number) pvalue).byteValue());
		case Long:
			return new PrimitiveObject(this, ((Number) pvalue).longValue());
		case Float:
			return new PrimitiveObject(this, ((Number) pvalue).floatValue());
		case Double:
			return new PrimitiveObject(this, ((Number) pvalue).doubleValue());
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public IObject fieldUnwrap(IObject value) {
		Object pvalue = ((PrimitiveObject) value).getValue();
		switch (this) {
		case Byte:
		case Boolean:
		case Char:
		case Short:
		case Int:
			return new PrimitiveObject(this, ((Number) pvalue).intValue());
		case Long:
			return new PrimitiveObject(this, ((Number) pvalue).longValue());
		case Float:
			return new PrimitiveObject(this, ((Number) pvalue).floatValue());
		case Double:
			return new PrimitiveObject(this, ((Number) pvalue).doubleValue());
		default:
			throw new NotImplementedException();
		}
	}
}
