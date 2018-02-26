package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.ObjectInstance;
import com.error22.karonda.vm.StackFrame;

public class TypeInstruction implements IInstruction {
	public static enum TypeOperation {
		InstanceOf,
		CheckCast
	}

	private TypeOperation op;
	private IType type;

	public TypeInstruction(TypeOperation op, IType type) {
		this.op = op;
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();
		KClass currentClass = stackFrame.getMethod().getKClass();

		int ref = stackFrame.pop();

		if (ref == 0) {
			switch (op) {
			case InstanceOf:
				stackFrame.push(0, false);
				return;
			case CheckCast:
				// TODO: Check against spec
				stackFrame.push(ref, true);
				return;
			default:
				throw new IllegalArgumentException("Unknown operation " + op);
			}
		}

		ObjectInstance object = instancePool.getObject(ref);
		boolean sameType = isCompatible(classPool, currentClass, object.getType(), type);

		switch (op) {
		case InstanceOf:
			stackFrame.push(sameType ? 1 : 0, false);
			break;
		case CheckCast:
			if (sameType) {
				stackFrame.push(ref, true);
			} else {
				throw new NotImplementedException();
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown operation " + op);
		}
	}

	public boolean isCompatible(ClassPool classPool, KClass currentClass, IType type, IType target) {
		if (type instanceof ObjectType) {
			if (!(target instanceof ObjectType)) {
				return false;
			}

			KClass typeClass = classPool.getClass(((ObjectType) type).getName(), currentClass);
			KClass targetClass = classPool.getClass(((ObjectType) target).getName(), currentClass);

			switch (typeClass.getType()) {
			case Class:
				switch (targetClass.getType()) {
				case Class:
					return typeClass.equals(targetClass) || typeClass.isParent(targetClass);
				case Interface:
					return typeClass.isImplemented(targetClass);
				default:
					return false;
				}
			case Interface:
				switch (targetClass.getType()) {
				case Class:
					return target.equals(ObjectType.OBJECT_TYPE);
				case Interface:
					return typeClass.equals(targetClass) || typeClass.isImplemented(targetClass);
				default:
					return false;
				}
			default:
				return false;
			}
		} else if (type instanceof ArrayType) {
			if (target instanceof ObjectType) {
				KClass targetClass = classPool.getClass(((ObjectType) target).getName(), currentClass);
				switch (targetClass.getType()) {
				case Class:
					return target.equals(ObjectType.OBJECT_TYPE);
				case Interface:
					throw new NotImplementedException("Unknown interfaces of arrays");
				default:
					return false;
				}
			} else if (target instanceof ArrayType) {
				ArrayType currentArray = (ArrayType) type;
				ArrayType targetArray = (ArrayType) target;

				if (currentArray.getDimensions() != targetArray.getDimensions()) {
					return false;
				}

				if (currentArray.getType() instanceof PrimitiveType && targetArray.getType() instanceof PrimitiveType) {
					return currentArray.getType().equals(targetArray.getType());
				}

				if (currentArray.getType() instanceof ObjectType && targetArray.getType() instanceof ObjectType) {
					return isCompatible(classPool, currentClass, currentArray.getType(), targetArray.getType());
				}
				return false;
			} else {
				return false;
			}
		}
		return false;
	}

}
