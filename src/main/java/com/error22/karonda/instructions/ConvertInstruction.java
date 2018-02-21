package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class ConvertInstruction implements IInstruction {
	private PrimitiveType from, to;

	public ConvertInstruction(PrimitiveType from, PrimitiveType to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		Number value;
		switch (from) {
		case Byte:
		case Boolean:
		case Char:
		case Short:
		case Int:
			value = stackFrame.pop();
			break;
		case Long:
			value = stackFrame.popLong();
			break;
		case Float:
			value = Float.intBitsToFloat(stackFrame.pop());
			break;
		case Double:
			value = Double.longBitsToDouble(stackFrame.popLong());
			break;
		default:
			throw new NotImplementedException();
		}

		switch (to) {
		case Byte:
			stackFrame.push(value.byteValue());
			break;
		case Boolean:
			stackFrame.push(value.intValue() != 0 ? 1 : 0);
			break;
		case Char:
			stackFrame.push((int) (char) value.intValue());
			break;
		case Short:
			stackFrame.push(value.shortValue());
			break;
		case Int:
			stackFrame.push(value.intValue());
			break;
		case Long:
			stackFrame.push(value.longValue());
			break;
		case Float:
			stackFrame.push(Float.floatToIntBits(value.floatValue()));
			break;
		case Double:
			stackFrame.push(Double.doubleToRawLongBits(value.doubleValue()));
			break;
		default:
			throw new NotImplementedException();
		}
	}

}
