package com.error22.karonda.instructions;

import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class LoadConstantInstruction implements IInstruction {
	private PrimitiveType type;
	private Object value;

	public LoadConstantInstruction(PrimitiveType type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (type) {
		case Void:
			stackFrame.push(new PrimitiveObject(type, null));
			break;
		case Byte:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).byteValue()));
			break;
		case Boolean:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).intValue() != 0));
			break;
		case Char:
			stackFrame.push(new PrimitiveObject(type, (char) ((Number) value).intValue()));
			break;
		case Short:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).shortValue()));
			break;
		case Int:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).intValue()));
			break;
		case Long:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).longValue()));
			break;
		case Float:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).floatValue()));
			break;
		case Double:
			stackFrame.push(new PrimitiveObject(type, ((Number) value).doubleValue()));
			break;
		default:
			break;
		}
	}
}
