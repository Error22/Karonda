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
			value = stackFrame.popFloat();
			break;
		case Double:
			value = stackFrame.popDouble();
			break;
		default:
			throw new NotImplementedException();
		}

		switch (to) {
		case Byte:
			stackFrame.pushInt(value.byteValue());
			break;
		case Boolean:
			stackFrame.pushInt(value.intValue() != 0 ? 1 : 0);
			break;
		case Char:
			stackFrame.pushInt((int) (char) value.intValue());
			break;
		case Short:
			stackFrame.pushInt(value.shortValue());
			break;
		case Int:
			stackFrame.pushInt(value.intValue());
			break;
		case Long:
			stackFrame.pushLong(value.longValue());
			break;
		case Float:
			stackFrame.pushFloat(value.floatValue());
			break;
		case Double:
			stackFrame.pushDouble(value.doubleValue());
			break;
		default:
			throw new NotImplementedException();
		}
	}

}
