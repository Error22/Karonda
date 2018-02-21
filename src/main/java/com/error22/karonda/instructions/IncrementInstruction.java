package com.error22.karonda.instructions;

import com.error22.karonda.vm.StackFrame;

public class IncrementInstruction implements IInstruction {
	private int index, by;

	public IncrementInstruction(int index, int by) {
		this.index = index;
		this.by = by;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		int val = stackFrame.getLocal(index);
		val += by;
		stackFrame.setLocal(index, val);
	}

	@Override
	public String toString() {
		return "IncrementInstruction [index=" + index + ", by=" + by + "]";
	}

}
