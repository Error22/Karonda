package com.error22.karonda.instructions;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.PrimitiveObject;
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
		// KClass currentClass = stackFrame.getMethod().getKClass();
		// ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();
		// KClass targetClass = classPool.getClass(type.getName(), currentClass);

		// TODO: Get proper array class

		int[] dimensionSizes = new int[dimensions];
		for (int i = dimensions - 1; i >= 0; i--)
			dimensionSizes[i] = ((Number) ((PrimitiveObject) stackFrame.pop()).getValue()).intValue();

		ObjectReference reference = createDimension(instancePool, dimensionSizes, 0);

		stackFrame.push(reference);
	}

	private ObjectReference createDimension(InstancePool instancePool, int[] dimensionSizes, int dimension) {
		int size = dimensionSizes[dimension];
		ObjectReference ref = instancePool.createArray(size);
		ObjectInstance inst = ref.getInstance();

		if (dimension < dimensions - 1) {
			for (int i = 0; i < size; i++) {
				inst.setArrayElement(i, createDimension(instancePool, dimensionSizes, dimension + 1));
			}
		}

		return ref;
	}

}