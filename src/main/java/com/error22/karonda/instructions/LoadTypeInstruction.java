package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IType;
import com.error22.karonda.vm.StackFrame;

public class LoadTypeInstruction implements IInstruction {
	private IType type;

	public LoadTypeInstruction(IType type) {
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
