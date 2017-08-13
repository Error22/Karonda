package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
import com.error22.karonda.vm.StackFrame;

public class LoadLocalInstruction implements IInstruction {
	private IType type;
	private int index;

	public LoadLocalInstruction(IType type, int index) {
		this.type = type;
		this.index = index;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		// TODO: check types compatible
		stackFrame.push(stackFrame.getLocal(index));
	}
}
