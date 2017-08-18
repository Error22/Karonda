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
		switch (mode) {
		case SingleCat1: {
			IObject value = stackFrame.pop();
			if (value.getType().isCategoryTwo())
				throw new IllegalArgumentException();
			stackFrame.push(value);
			stackFrame.push(value.duplicate());
			break;
		}
		case SingleCat1TwoDown: {
			IObject value1 = stackFrame.pop();
			IObject value2 = stackFrame.pop();
			if (value1.getType().isCategoryTwo() || value2.getType().isCategoryTwo())
				throw new IllegalArgumentException();
			stackFrame.push(value1.duplicate());
			stackFrame.push(value2);
			stackFrame.push(value1);
			break;
		}
		case SingleSpecialDown: {
			IObject value1 = stackFrame.pop();
			IObject value2 = stackFrame.pop();

			if (!value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()) {
				IObject value3 = stackFrame.pop();
				if (value3.getType().isCategoryTwo())
					throw new IllegalArgumentException();

				stackFrame.push(value1.duplicate());
				stackFrame.push(value3);
				stackFrame.push(value2);
				stackFrame.push(value1);
			} else if (!value1.getType().isCategoryTwo() && value2.getType().isCategoryTwo()) {
				stackFrame.push(value1.duplicate());
				stackFrame.push(value2);
				stackFrame.push(value1);
			} else {
				throw new IllegalArgumentException();
			}
			break;
		}
		case TwoSpecial: {
			IObject value1 = stackFrame.pop();

			if (value1.getType().isCategoryTwo()) {
				stackFrame.push(value1.duplicate());
				stackFrame.push(value1);
			} else {
				IObject value2 = stackFrame.pop();
				if (value2.getType().isCategoryTwo())
					throw new IllegalArgumentException();

				stackFrame.push(value2.duplicate());
				stackFrame.push(value1.duplicate());
				stackFrame.push(value2);
				stackFrame.push(value1);
			}

			break;
		}
		case TwoSpecialDown: {
			IObject value1 = stackFrame.pop();
			IObject value2 = stackFrame.pop();

			if (value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()) {
				stackFrame.push(value1.duplicate());
				stackFrame.push(value2);
				stackFrame.push(value1);
			} else if (!value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()) {
				IObject value3 = stackFrame.pop();
				if (value3.getType().isCategoryTwo())
					throw new IllegalArgumentException();

				stackFrame.push(value2.duplicate());
				stackFrame.push(value1.duplicate());
				stackFrame.push(value3);
				stackFrame.push(value2);
				stackFrame.push(value1);
			} else {
				throw new IllegalArgumentException();
			}
			break;
		}
		case TwoSpecialFurtherDown: {
			IObject value1 = stackFrame.pop();
			IObject value2 = stackFrame.pop();

			if (value1.getType().isCategoryTwo() && value2.getType().isCategoryTwo()) {
				stackFrame.push(value1.duplicate());
				stackFrame.push(value2);
				stackFrame.push(value1);
			} else {
				IObject value3 = stackFrame.pop();
				if (!value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()
						&& value3.getType().isCategoryTwo()) {
					stackFrame.push(value2.duplicate());
					stackFrame.push(value1.duplicate());
					stackFrame.push(value3);
					stackFrame.push(value2);
					stackFrame.push(value1);
				} else if (value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()
						&& !value3.getType().isCategoryTwo()) {
					stackFrame.push(value1.duplicate());
					stackFrame.push(value3);
					stackFrame.push(value2);
					stackFrame.push(value1);
				} else if (!value1.getType().isCategoryTwo() && !value2.getType().isCategoryTwo()
						&& !value3.getType().isCategoryTwo()) {
					IObject value4 = stackFrame.pop();
					if (value4.getType().isCategoryTwo())
						throw new IllegalArgumentException();

					stackFrame.push(value2.duplicate());
					stackFrame.push(value1.duplicate());
					stackFrame.push(value4);
					stackFrame.push(value3);
					stackFrame.push(value2);
					stackFrame.push(value1);
				} else {
					throw new IllegalArgumentException();
				}
			}
			break;
		}
		default:
			throw new NotImplementedException();
		}

	}

}
