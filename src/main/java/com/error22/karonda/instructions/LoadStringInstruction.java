package com.error22.karonda.instructions;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.ClassPool;
import com.error22.karonda.vm.InstancePool;
import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.ObjectInstance;
import com.error22.karonda.vm.StackFrame;

public class LoadStringInstruction implements IInstruction {
	private String value;

	public LoadStringInstruction(String value) {
		this.value = value;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();

		KClass targetClass = classPool.getClass(StringClass, currentClass);
		ObjectReference reference = instancePool.createInstance(targetClass);

		char[] chars = value.toCharArray();

		ObjectReference valueRef = instancePool.createArray(chars.length);
		ObjectInstance valueInst = valueRef.getInstance();

		for (int i = 0; i < chars.length; i++) {
			valueInst.setArrayElement(i, new PrimitiveObject(PrimitiveType.Char, chars[i]));
		}

		reference.getInstance().setField(ValueField, valueRef);

		stackFrame.push(reference);
	}

	private static final String StringClass = "java/lang/String";
	private static final FieldSignature ValueField = new FieldSignature(StringClass, "value",
			new ArrayType(PrimitiveType.Char, 1));
}
