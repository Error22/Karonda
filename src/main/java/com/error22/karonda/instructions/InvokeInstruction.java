package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.ClassType;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.ObjectInstance;
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

	private int[] fetchArguments(MethodSignature signature, StackFrame stackFrame, boolean instance) {
		IType[] arguments = signature.getArguments();
		int size = instance ? 1 : 0;
		for (IType type : arguments) {
			size += type.isCategoryTwo() ? 2 : 1;
		}

		int[] args = new int[size];
		int pos = args.length;
		for (int i = arguments.length - 1; i >= 0; i--) {
			if (arguments[i].isCategoryTwo()) {
				pos -= 2;
				args[pos] = stackFrame.pop();
				args[pos + 1] = stackFrame.pop();
			} else {
				pos--;
				args[pos] = stackFrame.pop();
			}
		}
		if (instance)
			args[0] = stackFrame.pop();
		return args;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool pool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();

		KClass signatureClass = pool.getClass(signature.getClazz(), currentClass);

		switch (type) {
		case Static: {
			KMethod method = signatureClass.getMethod(signature);
			int[] args = fetchArguments(signature, stackFrame, false);
			thread.initAndCall(method, false, args);
			break;
		}
		case Special: {
			int[] args = fetchArguments(signature, stackFrame, true);

			if (args[0] == 0)
				throw new NotImplementedException("Null object supported not implemented");

			boolean useSuperclass = currentClass.shouldSpecialMethodResolve() && !signature.isLocalInitializer()
					&& !signature.isStaticInitializer()
					&& (signatureClass.getType() == ClassType.Interface || currentClass.isParent(signatureClass));

			KClass targetClass = useSuperclass ? currentClass.getSuperClass() : signatureClass;
			KMethod resolved = targetClass.findMethod(signature, true);

			thread.initAndCall(resolved, false, args);
			break;
		}
		case Interface:
		case Virtual: {
			int[] args = fetchArguments(signature, stackFrame, true);

			int oid = args[0];
			if (oid == 0)
				throw new NotImplementedException("Null object supported not implemented");
			ObjectInstance instance = instancePool.getObject(oid);

			KClass targetClass = instance.getKClass();
			KMethod resolved = targetClass.findMethod(signature, true);
			thread.initAndCall(resolved, false, args);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public String toString() {
		return "InvokeInstruction [type=" + type + ", signature=" + signature + ", isInterface=" + isInterface + "]";
	}

}
