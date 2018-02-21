package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public enum PrimitiveType implements IType {
	Void, Byte, Boolean, Char, Short, Int, Long, Float, Double;

	@Override
	public boolean isCategoryTwo() {
		return this == Long || this == Double;
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
	public int[] fieldWrap(int[] value) {
		switch (this) {
		case Byte:
		case Boolean:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			return new int[] { value[0] & 0xff };
		case Char:
		case Short:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			return new int[] { value[0] & 0xffff };
		case Int:
		case Float:
			if (value.length != 1)
				throw new IllegalArgumentException("Incorrect data size");
			return new int[] { value[0] };
		case Long:
		case Double:
			if (value.length != 2)
				throw new IllegalArgumentException("Incorrect data size");
			return new int[] { value[0], value[1] };
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public int[] fieldUnwrap(int[] value) {
		return fieldWrap(value);
	}
}
