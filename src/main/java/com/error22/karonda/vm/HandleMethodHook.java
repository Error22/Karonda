package com.error22.karonda.vm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;

public class HandleMethodHook implements IMethodHook {
	private MethodHandle handle;
	private MethodType methodType;
	private IType returnType;
	private IType[] arguments;

	public HandleMethodHook(MethodHandle handle) {
		this.handle = handle;
		methodType = handle.type();
		this.returnType = ConversionUtils.convertType(methodType.returnType());
		this.arguments = ConversionUtils.convertTypes(methodType.parameterArray());
	}

	@Override
	public void invoke(KThread thread, StackFrame stackFrame, int[] args) {
		InstancePool instancePool = thread.getInstancePool();

		Object[] values = new Object[arguments.length];

		int argPos = 0;
		for (int i = 0; i < values.length; i++) {
			IType type = arguments[i];
			Class<?> clazz = methodType.parameterType(i);
			if (type instanceof PrimitiveType) {
				switch ((PrimitiveType) type) {
				case Void:
					throw new IllegalArgumentException("Voids should not be an argument type");
				case Byte:
					values[i] = (byte) args[argPos];
					break;
				case Boolean:
					values[i] = args[argPos] != 0;
					break;
				case Char:
					values[i] = (char) args[argPos];
					break;
				case Short:
					values[i] = (short) args[argPos];
					break;
				case Int:
					values[i] = args[argPos];
					break;
				case Long:
					values[i] = ConversionUtils.parseLong(args, argPos);
					break;
				case Float:
					values[i] = ConversionUtils.parseFloat(args, argPos);
					break;
				case Double:
					values[i] = ConversionUtils.parseDouble(args, argPos);
					break;
				default:
					throw new IllegalAccessError("Unknown type " + type);
				}
			} else if (type instanceof ObjectType) {
				if (clazz.equals(String.class)) {
					values[i] = instancePool.getStringContent(args[argPos]);
				} else {
					values[i] = instancePool.getObject(args[argPos]);
				}
			} else {
				throw new IllegalAccessError("Unknown type " + type);
			}

			argPos += type.getSize();
		}

		Object result;
		try {
			result = handle.invokeWithArguments(values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (returnType instanceof PrimitiveType) {
			switch ((PrimitiveType) returnType) {
			case Void:
				stackFrame.exit();
				return;
			case Byte:
			case Boolean:
			case Char:
			case Short:
			case Int:
				stackFrame.exit(new int[] { ((Number) result).intValue() }, false);
				return;
			case Long:
				stackFrame.exit(ConversionUtils.convertLong(((Number) result).longValue()), false);
				return;
			case Float:
				stackFrame.exit(ConversionUtils.convertFloat(((Number) result).floatValue()), false);
				return;
			case Double:
				stackFrame.exit(ConversionUtils.convertDouble(((Number) result).doubleValue()), false);
				return;
			default:
				throw new IllegalAccessError("Unknown type " + returnType);
			}
		} else if (returnType instanceof ObjectType) {
			Class<?> clazz = methodType.returnType();
			if (clazz.equals(String.class)) {
				stackFrame.exit(new int[] { instancePool.getStringInstance((String) result) }, true);
			} else {
				stackFrame.exit(new int[] { ((ObjectInstance) result).getId() }, true);
			}
			return;
		} else {
			throw new IllegalAccessError("Unknown type " + returnType);
		}
	}

	public IType getReturnType() {
		return returnType;
	}

	public IType[] getArguments() {
		return arguments;
	}
}