package com.error22.karonda.instructions;

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

}
