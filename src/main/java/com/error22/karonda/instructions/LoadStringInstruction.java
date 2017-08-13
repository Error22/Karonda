package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class LoadStringInstruction implements IInstruction {
	private String value;

	public LoadStringInstruction(String value) {
		this.value = value;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
