package com.error22.karonda.instructions;

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
		switch (op) {
		case Longs: {
			long value2 = stackFrame.popLong();
			long value1 = stackFrame.popLong();
			if (value1 > value2) {
				stackFrame.pushInt(1);
			} else if (value1 == value2) {
				stackFrame.pushInt(0);
			} else if (value1 < value2) {
				stackFrame.pushInt(-1);
			} else {
				throw new IllegalStateException();
			}
			break;
		}
		case FloatsPosNaN:
		case FloatsNegNaN: {
			float value2 = stackFrame.popFloat();
			float value1 = stackFrame.popFloat();
			if (value1 == Float.NaN || value2 == Float.NaN) {
				stackFrame.pushInt(op == CompareOp.FloatsNegNaN ? -1 : 1);
			} else if (value1 > value2) {
				stackFrame.pushInt(1);
			} else if (value1 == value2) {
				stackFrame.pushInt(0);
			} else if (value1 < value2) {
				stackFrame.pushInt(-1);
			} else {
				throw new IllegalStateException();
			}
			break;
		}
		case DoublesPosNaN:
		case DoublesNegNaN: {
			double value2 = stackFrame.popDouble();
			double value1 = stackFrame.popDouble();
			if (value1 == Double.NaN || value2 == Double.NaN) {
				stackFrame.pushInt(op == CompareOp.DoublesNegNaN ? -1 : 1);
			} else if (value1 > value2) {
				stackFrame.pushInt(1);
			} else if (value1 == value2) {
				stackFrame.pushInt(0);
			} else if (value1 < value2) {
				stackFrame.pushInt(-1);
			} else {
				throw new IllegalStateException();
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown op " + op);
		}

	}

}
