package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectReference;
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
		ClassPool pool = thread.getClassPool();

		if (isInterface)
			throw new NotImplementedException();

		KClass clazz = pool.getClass(signature.getClazz(), currentClass);
		KMethod method = clazz.getMethod(signature);

		switch (type) {
		case Static: {
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
			thread.initAndCall(method, false, args);
			break;
		}
		case Special: {
			IType[] arguments = method.getSignature().getArguments();
			int size = 1;
			for (IType type : arguments) {
				size += type.isCategoryTwo() ? 2 : 1;
			}

			IObject[] args = new IObject[size];
			int pos = args.length;
			for (int i = arguments.length - 1; i >= 0; i--) {
				pos -= arguments[i].isCategoryTwo() ? 2 : 1;
				args[pos] = stackFrame.pop();
			}

			ObjectReference reference = (ObjectReference) stackFrame.pop();
			KClass targetClass = reference.getKClass();
			args[0] = reference;

			boolean specialResolve = targetClass.shouldSpecialMethodResolve() && targetClass.isParent(clazz)
					&& !signature.isLocalInitializer();

			KMethod resolved = null;
			if (specialResolve)
				throw new NotImplementedException();
			else
				resolved = method;

			System.out.println("InvokeInstruction: special: " + resolved.getSignature());
			thread.initAndCall(resolved, false, args);
			break;
		}
		case Virtual: {
			IType[] arguments = method.getSignature().getArguments();
			int size = 1;
			for (IType type : arguments) {
				size += type.isCategoryTwo() ? 2 : 1;
			}

			IObject[] args = new IObject[size];
			int pos = args.length;
			for (int i = arguments.length - 1; i >= 0; i--) {
				pos -= arguments[i].isCategoryTwo() ? 2 : 1;
				args[pos] = stackFrame.pop();
			}

			ObjectReference reference = (ObjectReference) stackFrame.pop();
			KClass targetClass = reference.getKClass();
			args[0] = reference;

			KMethod resolved = targetClass.findMethod(signature);
			thread.initAndCall(resolved, false, args);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}
}
