package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;

public class NewInstruction implements IInstruction {
	private IType type;

	public NewInstruction(IType type) {
		this.type = type;
	}

}
