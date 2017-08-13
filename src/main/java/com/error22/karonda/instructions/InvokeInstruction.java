package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.vm.StackFrame;

public class InvokeInstruction implements IInstruction {
	public static enum InvokeType {
		Special,
		Virtual,
		Static,
		Interface
	}

	private InvokeType type;
	private MethodSignature signature;
	private boolean isInterface;

	public InvokeInstruction(InvokeType type, MethodSignature signature, boolean isInterface) {
		this.type = type;
		this.signature = signature;
		this.isInterface = isInterface;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}
}
