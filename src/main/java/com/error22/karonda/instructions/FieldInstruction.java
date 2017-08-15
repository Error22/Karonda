package com.error22.karonda.instructions;

import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;

public class FieldInstruction implements IInstruction {
	public static enum FieldOperation {
		LoadStatic,
		StoreStatic
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
			IObject value = signature.getType().fieldUnwrap(instancePool.getStaticField(clazz, signature));
			stackFrame.push(value);
			break;
		}
		case StoreStatic: {
			// TODO: check types compatible
			KClass clazz = classPool.getClass(signature.getClazz(), currentClass);
			if (thread.staticInit(clazz, true))
				return;
			IObject value = signature.getType().fieldWrap(stackFrame.pop());
			instancePool.setStaticField(clazz, signature, value);
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
	}
}
