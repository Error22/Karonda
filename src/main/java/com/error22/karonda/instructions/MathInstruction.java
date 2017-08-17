package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
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
		PrimitiveObject rhsObj = (PrimitiveObject) stackFrame.pop();
		Number rhs = (Number) rhsObj.getValue();

		switch (op) {
		case NegateInt:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), -rhs.intValue()));
			return;
		case NegateLong:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, -rhs.longValue()));
			return;
		case NegateFloat:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, -rhs.floatValue()));
			return;
		case NegateDouble:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, -rhs.doubleValue()));
			return;
		default:
			break;
		}

		Number lhs = (Number) ((PrimitiveObject) stackFrame.pop()).getValue();
		switch (op) {
		case AddInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() + rhs.intValue()));
			break;
		case AddLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() + rhs.longValue()));
			break;
		case AddFloats:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, lhs.floatValue() + rhs.floatValue()));
			break;
		case AddDoubles:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, lhs.doubleValue() + rhs.doubleValue()));
			break;
		case SubtractInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() - rhs.intValue()));
			break;
		case SubtractLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() - rhs.longValue()));
			break;
		case SubtractFloats:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, lhs.floatValue() - rhs.floatValue()));
			break;
		case SubtractDoubles:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, lhs.doubleValue() - rhs.doubleValue()));
			break;
		case MultiplyInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() * rhs.intValue()));
			break;
		case MultiplyLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() * rhs.longValue()));
			break;
		case MultiplyFloats:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, lhs.floatValue() * rhs.floatValue()));
			break;
		case MultiplyDoubles:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, lhs.doubleValue() * rhs.doubleValue()));
			break;
		case DivideInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() / rhs.intValue()));
			break;
		case DivideLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() / rhs.longValue()));
			break;
		case DivideFloats:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, lhs.floatValue() / rhs.floatValue()));
			break;
		case DivideDoubles:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, lhs.doubleValue() / rhs.doubleValue()));
			break;
		case RemainderInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() % rhs.intValue()));
			break;
		case RemainderLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() % rhs.longValue()));
			break;
		case RemainderFloats:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, lhs.floatValue() % rhs.floatValue()));
			break;
		case RemainderDoubles:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, lhs.doubleValue() % rhs.doubleValue()));
			break;
		case ShiftLeftInt:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() << rhs.intValue()));
			break;
		case ShiftLeftLong:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() << rhs.intValue()));
			break;
		case ArithmeticShiftRightInt:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() >> rhs.intValue()));
			break;
		case ArithmeticShiftRightLong:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() >> rhs.intValue()));
			break;
		case LogicalShiftRightInt:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() >>> rhs.intValue()));
			break;
		case LogicalShiftRightLong:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() >>> rhs.intValue()));
			break;
		case OrInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() | rhs.intValue()));
			break;
		case OrLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() | rhs.longValue()));
			break;
		case XOrInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() ^ rhs.intValue()));
			break;
		case XOrLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() ^ rhs.longValue()));
			break;
		case AndInts:
			stackFrame.push(new PrimitiveObject(rhsObj.getType(), lhs.intValue() & rhs.intValue()));
			break;
		case AndLongs:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, lhs.longValue() & rhs.longValue()));
			break;
		default:
			throw new NotImplementedException();
		}
	}

}
