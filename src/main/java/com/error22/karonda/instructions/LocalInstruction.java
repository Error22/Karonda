package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
import com.error22.karonda.vm.StackFrame;

public class LocalInstruction implements IInstruction {
	public static enum LocalOperation {
		Load,
		Store
	}

	private LocalOperation operation;
	private IType type;
	private int index;

	public LocalInstruction(LocalOperation operation, IType type, int index) {
		this.operation = operation;
		this.type = type;
		this.index = index;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (operation) {
		case Load:
			// TODO: check types compatible
			stackFrame.push(stackFrame.getLocal(index));
			break;
		case Store:
			// TODO: check types compatible
			stackFrame.setLocal(index, stackFrame.pop());
			break;

		default:
			throw new IllegalArgumentException();
		}

	}
}
