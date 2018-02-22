package com.error22.karonda.ir;

public class ObjectType implements IType {
	private String name;

	public ObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}

	@Override
	public int[] getDefaultValue() {
		return new int[] { 0 };
	}

	@Override
	public int[] fieldWrap(int[] value) {
		if (value.length != 1)
			throw new IllegalArgumentException("Incorrect data size");
		return new int[] { value[0] };
	}

	@Override
	public int[] fieldUnwrap(int[] value) {
		if (value.length != 1)
			throw new IllegalArgumentException("Incorrect data size");
		return new int[] { value[0] };
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ObjectType other = (ObjectType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectType [name=" + name + "]";
	}

	public static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	public static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	public static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");
}
