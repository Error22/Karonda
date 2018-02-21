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
		Special, Virtual, Static, Interface
	}

	private InvokeType type;
	private MethodSignature signature;
	private boolean isInterface;

	public InvokeInstruction(InvokeType type, MethodSignature signature, boolean isInterface) {
		this.type = type;
		this.signature = signature;
		this.isInterface = isInterface;
	}

	private int[] fetchArguments(KMethod method, StackFrame stackFrame, boolean instance) {
		IType[] arguments = method.getSignature().getArguments();
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

		// if (isInterface && type != InvokeType.Interface)
		// throw new NotImplementedException();

		KClass clazz = pool.getClass(signature.getClazz(), currentClass);
		KMethod method = clazz.getMethod(signature);

		switch (type) {
		case Static: {
			int[] args = fetchArguments(method, stackFrame, false);
			thread.initAndCall(method, false, args);
			break;
		}
		case Special: {
			int[] args = fetchArguments(method, stackFrame, true);

			KMethod resolved;
			if (isInterface) {
				resolved = method;
				System.out.println(
						"InvokeInstruction: specialResolve:  INTERFACE OVERRIDE  special: " + resolved.getSignature());
			} else {
				boolean specialResolve = currentClass.shouldSpecialMethodResolve()
						&& (clazz.getType() == ClassType.Interface || currentClass.isParent(clazz))
						&& !signature.isLocalInitializer();

				KClass scanClass = specialResolve ? currentClass.getSuperClass() : clazz;
				resolved = scanClass.findMethod(signature, true);
				System.out.println("InvokeInstruction: specialResolve: " + specialResolve + " resolved: "
						+ resolved.getSignature());
			}

			thread.initAndCall(resolved, false, args);
			break;
		}
		case Interface:
		case Virtual: {
			int[] args = fetchArguments(method, stackFrame, true);

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
}
