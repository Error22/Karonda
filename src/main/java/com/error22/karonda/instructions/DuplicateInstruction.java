package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class DuplicateInstruction implements IInstruction {
	public static enum DuplicateMode {
		SingleCat1,
		SingleCat1TwoDown,
		SingleSpecialDown,
		TwoSpecial,
		TwoSpecialDown,
		TwoSpecialFurtherDown
	}

	private DuplicateMode mode;

	public DuplicateInstruction(DuplicateMode mode) {
		this.mode = mode;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		int[] stack = stackFrame.getStack();
		boolean[] stackObjectMap = stackFrame.getStackObjectMap();
		int pointer = stackFrame.getStackPointer();
		switch (mode) {
		case SingleCat1: {
			stack[pointer] = stack[pointer - 1];
			stackObjectMap[pointer] = stackObjectMap[pointer - 1];
			pointer++;
			break;
		}
		case SingleCat1TwoDown: {
			stack[pointer] = stack[pointer - 1];
			stackObjectMap[pointer] = stackObjectMap[pointer - 1];
			stack[pointer - 1] = stack[pointer - 2];
			stackObjectMap[pointer - 1] = stackObjectMap[pointer - 2];
			stack[pointer - 2] = stack[pointer];
			stackObjectMap[pointer - 2] = stackObjectMap[pointer];
			pointer++;
			break;
		}
		case SingleSpecialDown: {
			stack[pointer] = stack[pointer - 1];
			stackObjectMap[pointer] = stackObjectMap[pointer - 1];
			stack[pointer - 1] = stack[pointer - 2];
			stackObjectMap[pointer - 1] = stackObjectMap[pointer - 2];
			stack[pointer - 2] = stack[pointer - 3];
			stackObjectMap[pointer - 2] = stackObjectMap[pointer - 3];
			stack[pointer - 3] = stack[pointer];
			stackObjectMap[pointer - 3] = stackObjectMap[pointer];
			pointer++;
			break;
		}
		case TwoSpecial: {
			stack[pointer + 1] = stack[pointer - 1];
			stackObjectMap[pointer + 1] = stackObjectMap[pointer - 1];
			stack[pointer] = stack[pointer - 2];
			stackObjectMap[pointer] = stackObjectMap[pointer - 2];
			pointer += 2;
			break;
		}
		case TwoSpecialDown: {
			stack[pointer + 1] = stack[pointer - 1];
			stackObjectMap[pointer + 1] = stackObjectMap[pointer - 1];
			stack[pointer] = stack[pointer - 2];
			stackObjectMap[pointer] = stackObjectMap[pointer - 2];
			stack[pointer - 1] = stack[pointer - 3];
			stackObjectMap[pointer - 1] = stackObjectMap[pointer - 3];
			stack[pointer - 2] = stack[pointer + 1];
			stackObjectMap[pointer - 2] = stackObjectMap[pointer + 1];
			stack[pointer - 3] = stack[pointer];
			stackObjectMap[pointer - 3] = stackObjectMap[pointer];
			pointer += 2;
			break;
		}
		case TwoSpecialFurtherDown: {
			stack[pointer + 1] = stack[pointer - 1];
			stackObjectMap[pointer + 1] = stackObjectMap[pointer - 1];
			stack[pointer] = stack[pointer - 2];
			stackObjectMap[pointer] = stackObjectMap[pointer - 2];
			stack[pointer - 1] = stack[pointer - 3];
			stackObjectMap[pointer - 1] = stackObjectMap[pointer - 3];
			stack[pointer - 2] = stack[pointer - 4];
			stackObjectMap[pointer - 2] = stackObjectMap[pointer - 4];
			stack[pointer - 3] = stack[pointer + 1];
			stackObjectMap[pointer - 3] = stackObjectMap[pointer + 1];
			stack[pointer - 4] = stack[pointer];
			stackObjectMap[pointer - 4] = stackObjectMap[pointer];
			pointer += 2;
			break;
		}
		default:
			throw new NotImplementedException();
		}
		stackFrame.setStackPointer(pointer);
	}

}
