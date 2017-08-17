package com.error22.karonda.instructions;

import org.objectweb.asm.Label;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.vm.ObjectInstance;
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
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() == 0;
			break;
		case NotEqualZero:
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() != 0;
			break;
		case LessThanZero:
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() < 0;
			break;
		case LessThanEqualZero:
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() <= 0;
			break;
		case GreaterThanEqualZero:
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() >= 0;
			break;
		case GreaterThanZero:
			jump = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue() > 0;
			break;
		case IntsEqual: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 == value2;
			break;
		}
		case IntsNotEqual: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 != value2;
			break;
		}
		case IntsLessThan: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 < value2;
			break;
		}
		case IntsLessThanEqual: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 <= value2;
			break;
		}
		case IntsGreaterThanEqual: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 >= value2;
			break;
		}
		case IntsGreaterThan: {
			int value2 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			int value1 = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			jump = value1 > value2;
			break;
		}
		case ReferenceEqual: {
			ObjectInstance value2 = ((ObjectReference) stackFrame.pop()).getInstance();
			ObjectInstance value1 = ((ObjectReference) stackFrame.pop()).getInstance();
			jump = value1.getId().equals(value2.getId());
			break;
		}
		case ReferenceNotEqual: {
			ObjectInstance value2 = ((ObjectReference) stackFrame.pop()).getInstance();
			ObjectInstance value1 = ((ObjectReference) stackFrame.pop()).getInstance();
			jump = !value1.getId().equals(value2.getId());
			break;
		}
		case Subroutine:
			throw new NotImplementedException(type.toString());
		case Goto:
			jump = true;
			break;
		case Null:
			jump = !(stackFrame.pop() instanceof ObjectReference);
			break;
		case NotNull:
			jump = stackFrame.pop() instanceof ObjectReference;
			break;
		default:
			throw new NotImplementedException();
		}
		if (jump)
			stackFrame.jump(label);
	}

}
