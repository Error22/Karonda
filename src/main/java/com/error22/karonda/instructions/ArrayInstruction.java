package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
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
		KThread thread = stackFrame.getThread();
		InstancePool instancePool = thread.getInstancePool();

		switch (operation) {
		case Load: {
			int index = stackFrame.pop();
			ObjectInstance inst = instancePool.getObject(stackFrame.pop());
			stackFrame.push(inst.getArrayElement(index), inst.getArrayType().getType() instanceof ObjectType);
			break;
		}
		case Store: {
			int[] value = stackFrame.pop(type.getSize());
			int index = stackFrame.pop();
			ObjectInstance inst = instancePool.getObject(stackFrame.pop());
			inst.getArrayType().getType().validate(value);
			inst.setArrayElement(index, value);
			break;
		}
		case Length: {
			ObjectInstance inst = instancePool.getObject(stackFrame.pop());
			stackFrame.pushInt(inst.getArraySize());
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
	}
}
