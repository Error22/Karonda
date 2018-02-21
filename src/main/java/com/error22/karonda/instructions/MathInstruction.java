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
			stackFrame.push(-stackFrame.pop());
			return;
		case NegateLong:
			stackFrame.push(-stackFrame.popLong());
			return;
		case NegateFloat:
			stackFrame.push(Float.floatToIntBits(-Float.intBitsToFloat(stackFrame.pop())));
			return;
		case NegateDouble:
			stackFrame.push(Double.doubleToRawLongBits(-Double.longBitsToDouble(stackFrame.popLong())));
			return;
		default:
			break;
		}

		switch (op) {
		case AddInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs + rhs);
			break;
		}
		case AddLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs + lhs);
			break;
		}
		case AddFloats: {
			float rhs = Float.intBitsToFloat(stackFrame.pop());
			float lhs = Float.intBitsToFloat(stackFrame.pop());
			stackFrame.push(Float.floatToRawIntBits(lhs + rhs));
			break;
		}
		case AddDoubles: {
			double rhs = Double.longBitsToDouble(stackFrame.popLong());
			double lhs = Double.longBitsToDouble(stackFrame.popLong());
			stackFrame.push(Double.doubleToRawLongBits(lhs + rhs));
			break;
		}
		case SubtractInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs - rhs);
			break;
		}
		case SubtractLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs - lhs);
			break;
		}
		case SubtractFloats: {
			float rhs = Float.intBitsToFloat(stackFrame.pop());
			float lhs = Float.intBitsToFloat(stackFrame.pop());
			stackFrame.push(Float.floatToRawIntBits(lhs - rhs));
			break;
		}
		case SubtractDoubles: {
			double rhs = Double.longBitsToDouble(stackFrame.popLong());
			double lhs = Double.longBitsToDouble(stackFrame.popLong());
			stackFrame.push(Double.doubleToRawLongBits(lhs - rhs));
			break;
		}
		case MultiplyInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs * rhs);
			break;
		}
		case MultiplyLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs * lhs);
			break;
		}
		case MultiplyFloats: {
			float rhs = Float.intBitsToFloat(stackFrame.pop());
			float lhs = Float.intBitsToFloat(stackFrame.pop());
			stackFrame.push(Float.floatToRawIntBits(lhs * rhs));
			break;
		}
		case MultiplyDoubles: {
			double rhs = Double.longBitsToDouble(stackFrame.popLong());
			double lhs = Double.longBitsToDouble(stackFrame.popLong());
			stackFrame.push(Double.doubleToRawLongBits(lhs * rhs));
			break;
		}
		case DivideInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs / rhs);
			break;
		}
		case DivideLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs / lhs);
			break;
		}
		case DivideFloats: {
			float rhs = Float.intBitsToFloat(stackFrame.pop());
			float lhs = Float.intBitsToFloat(stackFrame.pop());
			stackFrame.push(Float.floatToRawIntBits(lhs / rhs));
			break;
		}
		case DivideDoubles: {
			double rhs = Double.longBitsToDouble(stackFrame.popLong());
			double lhs = Double.longBitsToDouble(stackFrame.popLong());
			stackFrame.push(Double.doubleToRawLongBits(lhs / rhs));
			break;
		}
		case RemainderInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs % rhs);
			break;
		}
		case RemainderLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs % lhs);
			break;
		}
		case RemainderFloats: {
			float rhs = Float.intBitsToFloat(stackFrame.pop());
			float lhs = Float.intBitsToFloat(stackFrame.pop());
			stackFrame.push(Float.floatToRawIntBits(lhs % rhs));
			break;
		}
		case RemainderDoubles: {
			double rhs = Double.longBitsToDouble(stackFrame.popLong());
			double lhs = Double.longBitsToDouble(stackFrame.popLong());
			stackFrame.push(Double.doubleToRawLongBits(lhs % rhs));
			break;
		}
		case ShiftLeftInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs << rhs);
			break;
		}
		case ShiftLeftLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.push(lhs << rhs);
			break;
		}
		case ArithmeticShiftRightInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs >> rhs);
			break;
		}
		case ArithmeticShiftRightLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.push(lhs >> rhs);
			break;
		}
		case LogicalShiftRightInt: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs >>> rhs);
			break;
		}
		case LogicalShiftRightLong: {
			int rhs = stackFrame.pop();
			long lhs = stackFrame.popLong();
			stackFrame.push(lhs >>> rhs);
			break;
		}
		case OrInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs | rhs);
			break;
		}
		case OrLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs | lhs);
			break;
		}
		case XOrInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs ^ rhs);
			break;
		}
		case XOrLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs ^ lhs);
			break;
		}
		case AndInts: {
			int rhs = stackFrame.pop();
			int lhs = stackFrame.pop();
			stackFrame.push(lhs & rhs);
			break;
		}
		case AndLongs: {
			long rhs = stackFrame.popLong();
			long lhs = stackFrame.popLong();
			stackFrame.push(rhs & lhs);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}

}
