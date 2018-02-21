package com.error22.karonda.vm;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;

public class BuiltinNatives {
	private NativeManager manager;

	public BuiltinNatives(NativeManager manager) {
		this.manager = manager;
	}

	public void loadAll() {
		loadPrimitives();
		loadSystem();
	}

	public void loadPrimitives() {
		manager.addUnboundHook(this::empty, "registerNatives", PrimitiveType.Void);
		manager.addUnboundHook(this::desiredAssertionStatus0, "desiredAssertionStatus0", PrimitiveType.Boolean,
				CLASS_TYPE);
		manager.addUnboundHook(this::getPrimitiveClass, "getPrimitiveClass", CLASS_TYPE, STRING_TYPE);
		manager.addUnboundHook(this::returnArgs, "floatToRawIntBits", PrimitiveType.Int, PrimitiveType.Float);
		manager.addUnboundHook(this::returnArgs, "doubleToRawLongBits", PrimitiveType.Long, PrimitiveType.Double);
		manager.addUnboundHook(this::returnArgs, "longBitsToDouble", PrimitiveType.Double, PrimitiveType.Long);
	}

	public void loadSystem() {
		manager.addUnboundHook(this::arraycopy, "arraycopy", PrimitiveType.Void, OBJECT_TYPE, PrimitiveType.Int,
				OBJECT_TYPE, PrimitiveType.Int, PrimitiveType.Int);
	}

	private void empty(KThread thread, StackFrame frame, int[] args) {
		frame.exit();
	}

	private void desiredAssertionStatus0(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { 0 });
	}

	private void getPrimitiveClass(KThread thread, StackFrame frame, int[] args) {
		String name = ConversionUtils.parseString(thread.getInstancePool(), args[0]);
		IType type;
		if (name.equals("int")) {
			type = PrimitiveType.Int;
		} else if (name.equals("float")) {
			type = PrimitiveType.Float;
		} else if (name.equals("double")) {
			type = PrimitiveType.Double;
		} else {
			throw new NotImplementedException(name);
		}
		frame.exit(new int[] {
				thread.getInstancePool().getRuntimeClass(thread.getClassPool(), type, frame.getMethod().getKClass()) });
	}

	private void arraycopy(KThread thread, StackFrame frame, int[] args) {
		InstancePool pool = thread.getInstancePool();

		ObjectInstance srcArray = pool.getObject(args[0]);
		int srcStart = args[1];
		ObjectInstance dstArray = pool.getObject(args[2]);
		int dstStart = args[3];
		int length = args[4];
		for (int i = 0; i < length; i++) {
			dstArray.setArrayElement(dstStart + i, srcArray.getArrayElement(srcStart + i));
		}
		frame.exit();
	}

	private void returnArgs(KThread thread, StackFrame frame, int[] args) {
		frame.exit(args);
	}

	private static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	private static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	private static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");
}
