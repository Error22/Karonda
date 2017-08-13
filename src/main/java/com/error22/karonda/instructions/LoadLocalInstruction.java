package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;

public class LoadLocalInstruction implements IInstruction {
	private IType type;
	private int index;

	public LoadLocalInstruction(IType type, int index) {
		this.type = type;
		this.index = index;
	}
}
