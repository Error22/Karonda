package com.error22.karonda.instructions;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.ObjectInstance;
import com.error22.karonda.vm.StackFrame;

public class ArrayInstruction implements IInstruction {
	public static enum ArrayOperation {
		Load,
		Store,
		Length
	}

	private ArrayOperation operation;
	private IType type;

	public ArrayInstruction(ArrayOperation operation) {
		this.operation = operation;
	}

	public ArrayInstruction(ArrayOperation operation, IType type) {
		this.operation = operation;
		this.type = type;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		switch (operation) {
		case Load: {
			int index = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			ObjectInstance inst = ((ObjectReference) stackFrame.pop()).getInstance();
			// TODO: check types compatible
			stackFrame.push(inst.getArrayElement(index).duplicate());
			break;
		}
		case Store: {
			IObject value = stackFrame.pop();
			int index = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();
			ObjectInstance inst = ((ObjectReference) stackFrame.pop()).getInstance();
			// TODO: check types compatible
			inst.setArrayElement(index, value);
			break;
		}
		case Length: {
			ObjectInstance inst = ((ObjectReference) stackFrame.pop()).getInstance();
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, inst.getArraySize()));
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
	}
}
