package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class MathInstruction implements IInstruction {
	public enum MathOp {
		AddInts,
		AddLongs,
		AddFloats,
		AddDoubles
	}

	private MathOp op;

	public MathInstruction(MathOp op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
