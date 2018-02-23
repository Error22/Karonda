package com.error22.karonda.instructions;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.ObjectInstance;
import com.error22.karonda.vm.StackFrame;

public class NewArrayInstruction implements IInstruction {
	private ArrayType type;
	private int dimensions;

	public NewArrayInstruction(ArrayType type, int dimensions) {
		this.type = type;
		this.dimensions = dimensions;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();

		// TODO: Get proper array class

		int[] dimensionSizes = new int[dimensions];
		for (int i = dimensions - 1; i >= 0; i--)
			dimensionSizes[i] = stackFrame.pop();

		int reference = createDimension(classPool, instancePool, dimensionSizes, 0);
		stackFrame.push(reference);
	}

	private int createDimension(ClassPool classPool, InstancePool instancePool, int[] dimensionSizes, int dimension) {
		int size = dimensionSizes[dimension];
		int ref = instancePool.createArray(classPool, new ArrayType(type, -dimension), size);
		ObjectInstance inst = instancePool.getObject(ref);

		if (dimension < dimensions - 1) {
			for (int i = 0; i < size; i++) {
				inst.setArrayElement(i,
						new int[] { createDimension(classPool, instancePool, dimensionSizes, dimension + 1) });
			}
		}

		return ref;
	}

}