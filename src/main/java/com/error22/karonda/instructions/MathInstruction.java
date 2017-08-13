package com.error22.karonda.instructions;

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

}
