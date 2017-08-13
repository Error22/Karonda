package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IType;
import com.error22.karonda.vm.StackFrame;

public class StoreLocalInstruction implements IInstruction {
	private IType type;
	private int index;

	public StoreLocalInstruction(IType type, int index) {
		this.type = type;
		this.index = index;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}
}
