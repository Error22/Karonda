package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class DuplicateInstruction implements IInstruction {
	public static enum DuplicateMode {
		SingleCat1, SingleCat1TwoDown, SingleSpecialDown, TwoSpecial, TwoSpecialDown, TwoSpecialFurtherDown
	}

	private DuplicateMode mode;

	public DuplicateInstruction(DuplicateMode mode) {
		this.mode = mode;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (mode) {
		case SingleCat1: {
			int value = stackFrame.pop();
			stackFrame.push(value);
			stackFrame.push(value);
			break;
		}
		case SingleCat1TwoDown: {
			int value1 = stackFrame.pop();
			int value2 = stackFrame.pop();
			stackFrame.push(value1);
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		case SingleSpecialDown: {
			int value1 = stackFrame.pop();
			int value2 = stackFrame.pop();
			int value3 = stackFrame.pop();
			stackFrame.push(value1);
			stackFrame.push(value3);
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		case TwoSpecial: {
			int value1 = stackFrame.pop();
			int value2 = stackFrame.pop();
			stackFrame.push(value2);
			stackFrame.push(value1);
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		case TwoSpecialDown: {
			int value1 = stackFrame.pop();
			int value2 = stackFrame.pop();
			int value3 = stackFrame.pop();
			stackFrame.push(value2);
			stackFrame.push(value1);
			stackFrame.push(value3);
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		case TwoSpecialFurtherDown: {
			int value1 = stackFrame.pop();
			int value2 = stackFrame.pop();
			int value3 = stackFrame.pop();
			int value4 = stackFrame.pop();
			stackFrame.push(value2);
			stackFrame.push(value1);
			stackFrame.push(value4);
			stackFrame.push(value3);
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}

}
