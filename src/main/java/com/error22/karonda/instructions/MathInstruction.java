package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class MathInstruction implements IInstruction {
	public enum MathOp {
		AddInts,
		AddLongs,
		AddFloats,
		AddDoubles,
		SubtractInts,
		SubtractLongs,
		SubtractFloats,
		SubtractDoubles,
		MultiplyInts,
		MultiplyLongs,
		MultiplyFloats,
		MultiplyDoubles,
		DivideInts,
		DivideLongs,
		DivideFloats,
		DivideDoubles,
		RemainderInts,
		RemainderLongs,
		RemainderFloats,
		RemainderDoubles,
		NegateInt,
		NegateLong,
		NegateFloat,
		NegateDouble,
		ShiftLeftInt,
		ShiftLeftLong,
		ArithmeticShiftRightInt,
		ArithmeticShiftRightLong,
		LogicalShiftRightInt,
		LogicalShiftRightLong,
		OrInts,
		OrLongs,
		XOrInts,
		XOrLongs,
		AndInts,
		AndLongs
	}

	private MathOp op;

	public MathInstruction(MathOp op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (op) {
		case NegateInt:
			stackFrame.pushInt(-stackFrame.pop());
			return;
		case NegateLong:
			stackFrame.pushLong(-stackFrame.popLong());
			return;
		case NegateFloat:
			stackFrame.pushFloat(-stackFrame.popFloat());
			return;
		case NegateDouble:
			stackFrame.pushDouble(-stackFrame.popDouble());
			return;
		default:
			break;
		}

		switch (op) {
		case AddInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs + rhs);
			break;
		}
		case AddLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs + lhs);
			break;
		}
		case AddFloats: {
			float rhs = stackFrame.popFloat();
			float lhs = stackFrame.popFloat();
			stackFrame.pushFloat(lhs + rhs);
			break;
		}
		case AddDoubles: {
			double rhs = stackFrame.popDouble();
			double lhs = stackFrame.popDouble();
			stackFrame.pushDouble(lhs + rhs);
			break;
		}
		case SubtractInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs - rhs);
			break;
		}
		case SubtractLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs - lhs);
			break;
		}
		case SubtractFloats: {
			float rhs = stackFrame.popFloat();
			float lhs = stackFrame.popFloat();
			stackFrame.pushFloat(lhs - rhs);
			break;
		}
		case SubtractDoubles: {
			double rhs = stackFrame.popDouble();
			double lhs = stackFrame.popDouble();
			stackFrame.pushDouble(lhs - rhs);
			break;
		}
		case MultiplyInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs * rhs);
			break;
		}
		case MultiplyLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs * lhs);
			break;
		}
		case MultiplyFloats: {
			float rhs = stackFrame.popFloat();
			float lhs = stackFrame.popFloat();
			stackFrame.pushFloat(lhs * rhs);
			break;
		}
		case MultiplyDoubles: {
			double rhs = stackFrame.popDouble();
			double lhs = stackFrame.popDouble();
			stackFrame.pushDouble(lhs * rhs);
			break;
		}
		case DivideInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs / rhs);
			break;
		}
		case DivideLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs / lhs);
			break;
		}
		case DivideFloats: {
			float rhs = stackFrame.popFloat();
			float lhs = stackFrame.popFloat();
			stackFrame.pushFloat(lhs / rhs);
			break;
		}
		case DivideDoubles: {
			double rhs = stackFrame.popDouble();
			double lhs = stackFrame.popDouble();
			stackFrame.pushDouble(lhs / rhs);
			break;
		}
		case RemainderInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs % rhs);
			break;
		}
		case RemainderLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs % lhs);
			break;
		}
		case RemainderFloats: {
			float rhs = stackFrame.popFloat();
			float lhs = stackFrame.popFloat();
			stackFrame.pushFloat(lhs % rhs);
			break;
		}
		case RemainderDoubles: {
			double rhs = stackFrame.popDouble();
			double lhs = stackFrame.popDouble();
			stackFrame.pushDouble(lhs % rhs);
			break;
		}
		case ShiftLeftInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs << rhs);
			break;
		}
		case ShiftLeftLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(lhs << rhs);
			break;
		}
		case ArithmeticShiftRightInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs >> rhs);
			break;
		}
		case ArithmeticShiftRightLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(lhs >> rhs);
			break;
		}
		case LogicalShiftRightInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs >>> rhs);
			break;
		}
		case LogicalShiftRightLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(lhs >>> rhs);
			break;
		}
		case OrInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs | rhs);
			break;
		}
		case OrLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs | lhs);
			break;
		}
		case XOrInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs ^ rhs);
			break;
		}
		case XOrLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs ^ lhs);
			break;
		}
		case AndInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.pushInt(lhs & rhs);
			break;
		}
		case AndLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.pushLong(rhs & lhs);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public String toString() {
		return "MathInstruction [op=" + op + "]";
	}

}
