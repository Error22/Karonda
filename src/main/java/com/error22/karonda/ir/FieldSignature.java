package com.error22.karonda.ir;

public class FieldSignature {
	private String clazz, name;
	private IType type;
	private int nonBoundSignature;

	public FieldSignature(String clazz, String name, IType type) {
		this.clazz = clazz;
		this.name = name;
		this.type = type;

		final int prime = 31;
		nonBoundSignature = 1;
		nonBoundSignature = prime * nonBoundSignature + ((name == null) ? 0 : name.hashCode());
		nonBoundSignature = prime * nonBoundSignature + ((type == null) ? 0 : type.hashCode());
	}

	public String getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public IType getType() {
		return type;
	}

	public boolean matches(FieldSignature signature) {
		return nonBoundSignature == signature.nonBoundSignature && name.equals(signature.name)
				&& type.equals(signature.type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FieldSignature other = (FieldSignature) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return "FieldSignature [clazz=" + clazz + ", name=" + name + ", type=" + type + "]";
	}

}