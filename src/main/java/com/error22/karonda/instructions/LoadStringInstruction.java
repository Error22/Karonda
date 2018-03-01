package com.error22.karonda.instructions;

import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;

public class LoadStringInstruction implements IInstruction {
	private String value;

	public LoadStringInstruction(String value) {
		this.value = value;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		InstancePool instancePool = thread.getInstancePool();
		stackFrame.push(instancePool.getStringInstance(value), true);
	}
}
