package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
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
		throw new NotImplementedException();
	}
}
