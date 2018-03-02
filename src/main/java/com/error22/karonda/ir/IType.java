package com.error22.karonda.ir;

public interface IType {

	int[] getDefaultValue();

	int getSize();

	void validate(int[] value);

	boolean isReference();

	String getTypeName();

	String getEncodedName();

	public static IType getType(String type) {
		if (type.equals("boolean")) {
			return PrimitiveType.Boolean;
		} else if (type.equals("byte")) {
			return PrimitiveType.Byte;
		} else if (type.equals("char")) {
			return PrimitiveType.Char;
		} else if (type.equals("double")) {
			return PrimitiveType.Double;
		} else if (type.equals("float")) {
			return PrimitiveType.Float;
		} else if (type.equals("int")) {
			return PrimitiveType.Int;
		} else if (type.equals("long")) {
			return PrimitiveType.Long;
		} else if (type.equals("short")) {
			return PrimitiveType.Short;
		} else if (type.startsWith("[")) {
			int count = 1 + type.lastIndexOf("[");
			return new ArrayType(getEncodedType(type.substring(count)), count);
		}
		return new ObjectType(type.replaceAll("\\.", "/"));
	}

	public static IType getEncodedType(String type) {
		if (type.equals("Z")) {
			return PrimitiveType.Boolean;
		} else if (type.equals("B")) {
			return PrimitiveType.Byte;
		} else if (type.equals("C")) {
			return PrimitiveType.Char;
		} else if (type.equals("D")) {
			return PrimitiveType.Double;
		} else if (type.equals("F")) {
			return PrimitiveType.Float;
		} else if (type.equals("I")) {
			return PrimitiveType.Int;
		} else if (type.equals("J")) {
			return PrimitiveType.Long;
		} else if (type.equals("S")) {
			return PrimitiveType.Short;
		} else if (type.startsWith("L")) {
			return new ObjectType(type.substring(1).replaceAll("\\.", "/"));
		}
		throw new IllegalArgumentException();
	}

}
