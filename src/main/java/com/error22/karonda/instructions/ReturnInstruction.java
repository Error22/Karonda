package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;

public class ReturnInstruction implements IInstruction {
	private IType type;

	public ReturnInstruction(IType type) {
		this.type = type;
	}
}
