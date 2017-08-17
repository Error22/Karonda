package com.error22.karonda.instructions;

import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class IncrementInstruction implements IInstruction {
	private int index, by;

	public IncrementInstruction(int index, int by) {
		this.index = index;
		this.by = by;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		PrimitiveObject obj = (PrimitiveObject) stackFrame.getLocal(index);

		if (obj.getType() != PrimitiveType.Int)
			throw new IllegalArgumentException();

		int value = ((Number) obj.getValue()).intValue();
		value += by;

		stackFrame.setLocal(index, new PrimitiveObject(PrimitiveType.Int, value));
	}
}
