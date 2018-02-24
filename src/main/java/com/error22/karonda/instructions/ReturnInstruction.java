package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class ReturnInstruction implements IInstruction {
	private IType type;

	public ReturnInstruction(IType type) {
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		if (type.equals(PrimitiveType.Void)) {
			stackFrame.exit();
			return;
		}
		stackFrame.exit(stackFrame.pop(type.getSize()), type instanceof ObjectType);
	}

	@Override
	public String toString() {
		return "ReturnInstruction [type=" + type + "]";
	}

}
