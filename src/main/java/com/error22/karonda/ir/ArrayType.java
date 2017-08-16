package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public class ArrayType implements IType {
	private IType type;
	private int dimensions;

	public ArrayType(IType type, int dimensions) {
		if (type instanceof ArrayType) {
			ArrayType other = (ArrayType) type;
			this.type = other.type;
			this.dimensions = other.dimensions + dimensions;
		} else {
			this.type = type;
			this.dimensions = dimensions;
		}
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}

	@Override
	public IObject getDefaultValue() {
		throw new NotImplementedException();
	}

	@Override
	public IObject fieldWrap(IObject value) {
		throw new NotImplementedException();
	}

	@Override
	public IObject fieldUnwrap(IObject value) {
		throw new NotImplementedException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimensions;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayType other = (ArrayType) obj;
		if (dimensions != other.dimensions)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArrayType [type=" + type + ", dimensions=" + dimensions + "]";
	}

}
