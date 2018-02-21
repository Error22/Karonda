package com.error22.karonda.converter;

import org.objectweb.asm.Type;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.ObjectInstance;

public class ConversionUtils {
	private static final String STRING_CLASS = "java/lang/String";
	private static final FieldSignature VALUE_FIELD = new FieldSignature(STRING_CLASS, "value",
			new ArrayType(PrimitiveType.Char, 1));

	public static MethodSignature parseMethodSignature(String parent, String name, String desc) {
		IType returnType = convertType(Type.getReturnType(desc));
		IType[] arguments = convertTypes(Type.getArgumentTypes(desc));
		return new MethodSignature(parent, name, returnType, arguments);
	}

	public static FieldSignature parseFieldSignature(String owner, String name, String desc) {
		return new FieldSignature(owner, name, ConversionUtils.convertType(Type.getType(desc)));
	}

	public static IType[] convertTypes(Type[] types) {
		IType[] out = new IType[types.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = convertType(types[i]);
		}
		return out;
	}

	public static IType convertType(Type type) {
		if (type == Type.VOID_TYPE) {
			return PrimitiveType.Void;
		} else if (type == Type.BYTE_TYPE) {
			return PrimitiveType.Byte;
		} else if (type == Type.BOOLEAN_TYPE) {
			return PrimitiveType.Boolean;
		} else if (type == Type.CHAR_TYPE) {
			return PrimitiveType.Char;
		} else if (type == Type.SHORT_TYPE) {
			return PrimitiveType.Short;
		} else if (type == Type.INT_TYPE) {
			return PrimitiveType.Int;
		} else if (type == Type.LONG_TYPE) {
			return PrimitiveType.Long;
		} else if (type == Type.FLOAT_TYPE) {
			return PrimitiveType.Float;
		} else if (type == Type.DOUBLE_TYPE) {
			return PrimitiveType.Double;
		} else if (type.getSort() == Type.OBJECT) {
			return new ObjectType(type.getClassName().replaceAll("\\.", "/"));
		} else if (type.getSort() == Type.ARRAY) {
			return new ArrayType(convertType(type.getElementType()), type.getDimensions());
		} else {
			throw new IllegalArgumentException(type.toString() + " sort=" + type.getSort());
		}
	}

	public static long parseLong(int[] data, int index) {
		return (long) data[0] << 32 | data[1] & 0xFFFFFFFFL;
	}

	public static String parseString(InstancePool pool, int obj) {
		ObjectInstance inst = pool.getObject(pool.getObject(obj).getField(VALUE_FIELD)[0]);
		int size = inst.getArraySize();
		char[] chars = new char[size];

		for (int i = 0; i < size; i++) {
			chars[i] = (char) inst.getArrayElement(i)[0];
		}

		return new String(chars);
	}
}
