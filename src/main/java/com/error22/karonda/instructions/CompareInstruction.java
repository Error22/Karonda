package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class CompareInstruction implements IInstruction {
	public static enum CompareOp {
		Longs,
		FloatsPosNaN,
		FloatsNegNaN,
		DoublesPosNaN,
		DoublesNegNaN
	}

	private CompareOp op;

	public CompareInstruction(CompareOp op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
