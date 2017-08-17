package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IType;
import com.error22.karonda.vm.StackFrame;

public class TypeInstruction implements IInstruction {
	public static enum TypeOperation {
		InstanceOf,
		CheckCast
	}

	private TypeOperation op;
	private IType type;

	public TypeInstruction(TypeOperation op, IType type) {
		this.op = op;
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
