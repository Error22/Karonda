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
		return name.replaceAll("/", ".");
	}

	@Override
	public String getEncodedName() {
		return "L" + getTypeName();
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
	public static final ObjectType THREAD_GROUP_TYPE = new ObjectType("java/lang/ThreadGroup");
	public static final ObjectType THREAD_TYPE = new ObjectType("java/lang/Thread");
	public static final ObjectType PROPERTIES_TYPE = new ObjectType("java/util/Properties");
	public static final ObjectType REFLECT_ACCESSIBLE_OBJECT_TYPE = new ObjectType(
			"java/lang/reflect/AccessibleObject");
	public static final ObjectType REFLECT_FIELD_TYPE = new ObjectType("java/lang/reflect/Field");
}
