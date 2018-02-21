package com.error22.karonda.instructions;

import com.error22.karonda.ir.IType;
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
			// TODO: check types compatible
			stackFrame.push(inst.getArrayType().getType().fieldUnwrap(inst.getArrayElement(index)));
			break;
		}
		case Store: {
			int[] value = stackFrame.pop(type.isCategoryTwo() ? 2 : 1);
			int index = stackFrame.pop();
			ObjectInstance inst = instancePool.getObject(stackFrame.pop());
			// TODO: check types compatible
			inst.setArrayElement(index, inst.getArrayType().getType().fieldWrap(value));
			break;
		}
		case Length: {
			ObjectInstance inst = instancePool.getObject(stackFrame.pop());
			stackFrame.push(inst.getArraySize());
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
	}
}
