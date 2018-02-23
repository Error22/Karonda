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
				stackFrame.push(1);
			} else if (value1 == value2) {
				stackFrame.push(0);
			} else if (value1 < value2) {
				stackFrame.push(-1);
			} else {
				throw new IllegalStateException();
			}
			break;
		}
		case FloatsPosNaN:
		case FloatsNegNaN: {
			float value2 = Float.intBitsToFloat(stackFrame.pop());
			float value1 = Float.intBitsToFloat(stackFrame.pop());
			if (value1 == Float.NaN || value2 == Float.NaN) {
				stackFrame.push(op == CompareOp.FloatsNegNaN ? -1 : 1);
			} else if (value1 > value2) {
				stackFrame.push(1);
			} else if (value1 == value2) {
				stackFrame.push(0);
			} else if (value1 < value2) {
				stackFrame.push(-1);
			} else {
				throw new IllegalStateException();
			}
			break;
		}
		case DoublesPosNaN:
		case DoublesNegNaN: {
			double value2 = Double.longBitsToDouble(stackFrame.popLong());
			double value1 = Double.longBitsToDouble(stackFrame.popLong());
			if (value1 == Double.NaN || value2 == Double.NaN) {
				stackFrame.push(op == CompareOp.DoublesNegNaN ? -1 : 1);
			} else if (value1 > value2) {
				stackFrame.push(1);
			} else if (value1 == value2) {
				stackFrame.push(0);
			} else if (value1 < value2) {
				stackFrame.push(-1);
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
