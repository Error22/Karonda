package com.error22.karonda.ir;

import java.util.Arrays;

public class MethodSignature {
	private String clazz, name;
	private IType returnType;
	private IType[] arguments;

	public MethodSignature(String clazz, String name, IType returnType, IType... arguments) {
		this.clazz = clazz;
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
	}

	public boolean isLocalInitializer() {
		return name.equals("<init>");
	}

	public boolean isStaticInitializer() {
		return name.equals("<clinit>");
	}

	public String getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public IType getReturnType() {
		return returnType;
	}

	public IType[] getArguments() {
		return arguments;
	}

	public String toNiceString() {
		return clazz + "::" + name + "(" + Arrays.deepToString(arguments) + ")" + returnType.toString();
	}

	public boolean matches(MethodSignature signature) {
		return name.equals(signature.name) && returnType.equals(signature.returnType)
				&& Arrays.equals(arguments, signature.arguments);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(arguments);
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
		MethodSignature other = (MethodSignature) obj;
		if (!Arrays.equals(arguments, other.arguments))
			return false;
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
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MethodSignature [clazz=" + clazz + ", name=" + name + ", returnType=" + returnType + ", arguments="
				+ Arrays.toString(arguments) + "]";
	}

}
