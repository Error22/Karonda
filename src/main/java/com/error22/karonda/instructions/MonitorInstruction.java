package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class MonitorInstruction implements IInstruction {
	public static enum MonitorOperation {
		Enter,
		Exit
	}

	private MonitorOperation op;

	public MonitorInstruction(MonitorOperation op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
