package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.IObject;
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
		IObject value = stackFrame.pop();
		stackFrame.push(value);
		stackFrame.push(value.duplicate());
	}

}
