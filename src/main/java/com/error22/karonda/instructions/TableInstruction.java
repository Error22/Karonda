package com.error22.karonda.instructions;

import org.objectweb.asm.Label;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

public class TableInstruction implements IInstruction{
	private int min, max;
	private Label defaultLabel;
	private Label[] labels;

	public TableInstruction(int min, int max, Label defaultLabel, Label[] labels) {
		this.min = min;
		this.max = max;
		this.defaultLabel = defaultLabel;
		this.labels = labels;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
