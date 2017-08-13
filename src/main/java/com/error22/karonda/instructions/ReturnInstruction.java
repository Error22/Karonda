package com.error22.karonda.instructions;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class ReturnInstruction implements IInstruction {
	private IType type;

	public ReturnInstruction(IType type) {
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		IObject result = null;
		if (!type.equals(PrimitiveType.Void))
			result = stackFrame.pop();
		// TODO: check types compatible
		stackFrame.exit(result);
	}
}
