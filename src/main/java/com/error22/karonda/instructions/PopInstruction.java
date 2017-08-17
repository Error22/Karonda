package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class PopInstruction implements IInstruction {
	public static enum PopMode {
		Single,
		Double
	}

	private PopMode mode;

	public PopInstruction(PopMode mode) {
		this.mode = mode;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (mode) {
		case Single: {
			if (stackFrame.pop().getType().isCategoryTwo())
				throw new IllegalArgumentException();
			break;
		}
		case Double: {
			if (!stackFrame.pop().getType().isCategoryTwo())
				stackFrame.pop();
			break;
		}
		default:
			throw new NotImplementedException();
		}

	}

}
