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
		int[] stack = stackFrame.getStack();
		boolean[] stackObjectMap = stackFrame.getStackObjectMap();
		int pointer = stackFrame.getStackPointer();
		int[] locals = stackFrame.getLocals();
		boolean[] localsObjectMap = stackFrame.getLocalsObjectMap();
		switch (operation) {
		case Load:
			for (int i = 0; i < type.getSize(); i++) {
				stack[pointer + i] = locals[index + i];
				stackObjectMap[pointer + i] = localsObjectMap[index + i];
			}
			pointer += type.getSize();
			break;
		case Store:
			pointer -= type.getSize();
			for (int i = 0; i < type.getSize(); i++) {
				locals[index + i] = stack[pointer + i];
				localsObjectMap[index + i] = stackObjectMap[pointer + i];
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		stackFrame.setStackPointer(pointer);
	}

	@Override
	public String toString() {
		return "LocalInstruction [operation=" + operation + ", type=" + type + ", index=" + index + "]";
	}

}
