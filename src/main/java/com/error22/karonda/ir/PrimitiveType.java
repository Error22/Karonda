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
	public int getSize() {
		return this == Long || this == Double ? 2 : 1;
	}

	@Override
	public int[] getDefaultValue() {
		switch (this) {
		case Byte:
		case Boolean:
		case Char:
		case Short:
		case Int:
		case Float:
			return new int[1];
		case Long:
		case Double:
			return new int[2];
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public void validate(int[] value) {
		switch (this) {
		case Byte:
		case Boolean:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			value[0] = value[0] & 0xff;
			break;
		case Char:
		case Short:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			value[0] = value[0] & 0xffff;
			break;
		case Int:
		case Float:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			break;
		case Long:
		case Double:
			if (value.length != 2)
				throw new IllegalArgumentException("Incorrect data size");
			break;
		default:
			throw new NotImplementedException();
		}
	}
	
	@Override
	public boolean isReference() {
		return false;
	}

}
