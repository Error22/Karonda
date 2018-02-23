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
		boolean jump;
		switch (type) {
		case EqualZero:
			jump = stackFrame.pop() == 0;
			break;
		case NotEqualZero:
			jump = stackFrame.pop() != 0;
			break;
		case LessThanZero:
			jump = stackFrame.pop() < 0;
			break;
		case LessThanEqualZero:
			jump = stackFrame.pop() <= 0;
			break;
		case GreaterThanEqualZero:
			jump = stackFrame.pop() >= 0;
			break;
		case GreaterThanZero:
			jump = stackFrame.pop() > 0;
			break;
		case IntsEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 == value2;
			break;
		}
		case IntsNotEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 != value2;
			break;
		}
		case IntsLessThan: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 < value2;
			break;
		}
		case IntsLessThanEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 <= value2;
			break;
		}
		case IntsGreaterThanEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 >= value2;
			break;
		}
		case IntsGreaterThan: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 > value2;
			break;
		}
		case ReferenceEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 == value2;
			break;
		}
		case ReferenceNotEqual: {
			int value2 = stackFrame.pop();
			int value1 = stackFrame.pop();
			jump = value1 != value2;
			break;
		}
		case Subroutine:
			throw new NotImplementedException(type.toString());
		case Goto:
			jump = true;
			break;
		case Null:
			jump = stackFrame.pop() == 0;
			break;
		case NotNull:
			jump = stackFrame.pop() != 0;
			break;
		default:
			throw new NotImplementedException();
		}
		if (jump)
			stackFrame.jump(label);
	}

	@Override
	public String toString() {
		return "JumpInstruction [type=" + type + ", label=" + label + "]";
	}

}
