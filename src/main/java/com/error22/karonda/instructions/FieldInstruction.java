package com.error22.karonda.instructions;

import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.ObjectInstance;
import com.error22.karonda.vm.StackFrame;

public class FieldInstruction implements IInstruction {
	public static enum FieldOperation {
		LoadStatic,
		StoreStatic,
		LoadLocal,
		StoreLocal
	}

	private FieldOperation operation;
	private FieldSignature signature;

	public FieldInstruction(FieldOperation operation, FieldSignature signature) {
		this.operation = operation;
		this.signature = signature;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();

		switch (operation) {
		case LoadStatic: {
			KClass clazz = classPool.getClass(signature.getClazz(), currentClass);
			if (thread.staticInit(clazz, true))
				return;
			stackFrame.push(instancePool.getStaticField(clazz, signature), signature.getType().isReference());
			break;
		}
		case StoreStatic: {
			KClass clazz = classPool.getClass(signature.getClazz(), currentClass);
			if (thread.staticInit(clazz, true))
				return;
			int[] value = stackFrame.pop(signature.getType().getSize());
			signature.getType().validate(value);
			instancePool.setStaticField(clazz, signature, value);
			break;
		}
		case LoadLocal: {
			KClass clazz = classPool.getClass(signature.getClazz(), currentClass);
			if (thread.staticInit(clazz, true))
				return;
			ObjectInstance instance = instancePool.getObject(stackFrame.pop());
			KField target = clazz.findField(signature);
			stackFrame.push(instance.getField(target.getSignature()), signature.getType().isReference());
			break;
		}
		case StoreLocal: {
			KClass clazz = classPool.getClass(signature.getClazz(), currentClass);
			if (thread.staticInit(clazz, true))
				return;
			int[] value = stackFrame.pop(signature.getType().getSize());
			signature.getType().validate(value);
			ObjectInstance instance = instancePool.getObject(stackFrame.pop());
			KField target = clazz.findField(signature);
			instance.setField(target.getSignature(), value);
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return "FieldInstruction [operation=" + operation + ", signature=" + signature + "]";
	}

}
