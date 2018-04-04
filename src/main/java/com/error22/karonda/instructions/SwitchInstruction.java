package com.error22.karonda.instructions;

import org.objectweb.asm.Label;

import com.error22.karonda.vm.StackFrame;

public class SwitchInstruction implements IInstruction {
	private Label defaultLabel;
	private int[] keys;
	private Label[] labels;

	public SwitchInstruction(Label defaultLabel, int[] keys, Label[] labels) {
		this.defaultLabel = defaultLabel;
		this.keys = keys;
		this.labels = labels;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		int value = stackFrame.pop();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == value) {
				stackFrame.jump(labels[i]);
				return;
			}
		}
		stackFrame.jump(defaultLabel);
	}

}
