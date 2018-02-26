package com.error22.karonda.instructions;

import org.javatuples.Pair;

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

	private Pair<int[], boolean[]> fetchArguments(MethodSignature signature, StackFrame stackFrame, boolean instance) {
		IType[] arguments = signature.getArguments();
		int size = instance ? 1 : 0;
		for (IType type : arguments) {
			size += type.getSize();
		}

		int[] args = new int[size];
		boolean[] objectMap = new boolean[size];
		int pos = args.length;
		for (int i = arguments.length - 1; i >= 0; i--) {
			pos -= arguments[i].getSize();
			for (int j = 0; j < arguments[i].getSize(); j++) {
				args[pos + j] = stackFrame.pop();
				objectMap[pos + j] = arguments[i].isReference();
			}
		}
		if (instance) {
			args[0] = stackFrame.pop();
			objectMap[0] = true;
		}
		return new Pair<int[], boolean[]>(args, objectMap);
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
			Pair<int[], boolean[]> args = fetchArguments(signature, stackFrame, false);
			thread.initAndCall(method, false, args.getValue0(), args.getValue1());
			break;
		}
		case Special: {
			Pair<int[], boolean[]> args = fetchArguments(signature, stackFrame, true);

			if (args.getValue0()[0] == 0)
				throw new NotImplementedException("Null object supported not implemented");

			boolean useSuperclass = currentClass.shouldSpecialMethodResolve() && !signature.isLocalInitializer()
					&& !signature.isStaticInitializer()
					&& (signatureClass.getType() == ClassType.Interface || currentClass.isParent(signatureClass));

			KClass targetClass = useSuperclass ? currentClass.getSuperClass() : signatureClass;
			KMethod resolved = targetClass.findMethod(signature, true);

			thread.initAndCall(resolved, false, args.getValue0(), args.getValue1());
			break;
		}
		case Interface:
		case Virtual: {
			Pair<int[], boolean[]> args = fetchArguments(signature, stackFrame, true);

			int oid = args.getValue0()[0];
			if (oid == 0)
				throw new NotImplementedException("Null object supported not implemented");
			ObjectInstance instance = instancePool.getObject(oid);

			KClass targetClass = instance.getKClass();
			KMethod resolved = targetClass.findMethod(signature, true);
			thread.initAndCall(resolved, false, args.getValue0(), args.getValue1());
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
