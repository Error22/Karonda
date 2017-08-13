package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;

public class InvokeInstruction implements IInstruction {
	public static enum InvokeType {
		Special,
		Virtual,
		Static,
		Interface
	}

	private InvokeType type;
	private MethodSignature signature;
	private boolean isInterface;

	public InvokeInstruction(InvokeType type, MethodSignature signature, boolean isInterface) {
		this.type = type;
		this.signature = signature;
		this.isInterface = isInterface;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool pool = thread.getPool();

		if (isInterface)
			throw new NotImplementedException();

		switch (type) {
		case Static: {
			KClass clazz = pool.getClass(signature.getClazz(), currentClass);
			KMethod method = clazz.getMethod(signature);

			IType[] arguments = method.getSignature().getArguments();
			int size = 0;
			for (IType type : arguments) {
				size += type.isCategoryTwo() ? 2 : 1;
			}

			IObject[] args = new IObject[size];
			int pos = args.length;
			for (int i = arguments.length - 1; i >= 0; i--) {
				pos -= arguments[i].isCategoryTwo() ? 2 : 1;
				args[pos] = stackFrame.pop();
			}
			thread.callMethod(method, args);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}
}
