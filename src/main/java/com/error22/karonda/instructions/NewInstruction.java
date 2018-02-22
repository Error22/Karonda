package com.error22.karonda.instructions;

import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;

public class NewInstruction implements IInstruction {
	private ObjectType type;

	public NewInstruction(ObjectType type) {
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();
		KClass targetClass = classPool.getClass(type.getName(), currentClass);
		stackFrame.push(instancePool.createInstance(targetClass, type));
	}

}
