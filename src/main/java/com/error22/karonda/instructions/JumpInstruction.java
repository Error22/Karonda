package com.error22.karonda.instructions;

import org.objectweb.asm.Label;

public class JumpInstruction implements IInstruction {
	public static enum JumpType {
		EqualZero,
		NotEqualZero,
		LessThanZero,
		LessThanEqualZero,
		GreaterThanEqualZero,
		GreaterThanZero,
		IntsEqual,
		IntsNotEqual,
		IntsLessThan,
		IntsLessThanEqual,
		IntsGreaterThanEqual,
		IntsGreaterThan,
		ReferenceEqual,
		ReferenceNotEqual
	}

	private JumpType type;
	private Label label;

	public JumpInstruction(JumpType type, Label label) {
		this.type = type;
		this.label = label;
	}

}
