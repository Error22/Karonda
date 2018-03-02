package com.error22.karonda.ir;

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
	public int getSize() {
		return 1;
	}

	@Override
	public int[] getDefaultValue() {
		return new int[] { 0 };
	}

	@Override
	public void validate(int[] value) {
		if (value.length != 1)
			throw new IllegalArgumentException("Incorrect data size");
	}

	@Override
	public boolean isReference() {
		return true;
	}

	@Override
	public String getTypeName() {
		String name = "";
		for (int i = 0; i < dimensions; i++) {
			name += "[";
		}
		name += type.getEncodedName();
		return name;
	}

	@Override
	public String getEncodedName() {
		throw new IllegalStateException("Unsupported operation");
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

	public IType getType() {
		return type;
	}

	public int getDimensions() {
		return dimensions;
	}

	@Override
	public String toString() {
		return "ArrayType [type=" + type + ", dimensions=" + dimensions + "]";
	}

	public static final ArrayType REFLECT_FIELD_ARRAY = new ArrayType(ObjectType.REFLECT_FIELD_TYPE, 1);
	public static final ArrayType CHAR_ARRAY = new ArrayType(PrimitiveType.Char, 1);
}
