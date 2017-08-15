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
		DivideDoubles
	}

	private MathOp op;

	public MathInstruction(MathOp op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		PrimitiveObject rhsObj = (PrimitiveObject) stackFrame.pop();
		Number rhs = (Number) rhsObj.getValue();
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
		default:
			throw new NotImplementedException();
		}
	}

}
