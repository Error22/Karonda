package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;

public class LoadTypeInstruction implements IInstruction {
	private IType type;

	public LoadTypeInstruction(IType type) {
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();
		stackFrame.push(instancePool.getRuntimeClass(classPool, type, currentClass));
	}

}
