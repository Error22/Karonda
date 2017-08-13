package com.error22.karonda.instructions;

import org.objectweb.asm.Label;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.vm.StackFrame;

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
		ReferenceNotEqual,
		Subroutine,
		Goto,
		Null,
		NotNull
	}

	private JumpType type;
	private Label label;

	public JumpInstruction(JumpType type, Label label) {
		this.type = type;
		this.label = label;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		throw new NotImplementedException();
	}

}
