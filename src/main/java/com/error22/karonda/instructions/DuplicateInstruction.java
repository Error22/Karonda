package com.error22.karonda.instructions;

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

}
