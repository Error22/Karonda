package com.error22.karonda.vm;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.ObjectReference;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveObject;
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
		manager.addUnboundHook(this::floatToRawIntBits, "floatToRawIntBits", PrimitiveType.Int, PrimitiveType.Float);
		manager.addUnboundHook(this::doubleToRawLongBits, "doubleToRawLongBits", PrimitiveType.Long,
				PrimitiveType.Double);
		manager.addUnboundHook(this::longBitsToDouble, "longBitsToDouble", PrimitiveType.Double, PrimitiveType.Long);
	}
	
	public void loadSystem() {
		manager.addUnboundHook(this::arraycopy, "arraycopy", PrimitiveType.Void, OBJECT_TYPE, PrimitiveType.Int,
				OBJECT_TYPE, PrimitiveType.Int, PrimitiveType.Int);
	}

	private void empty(KThread thread, StackFrame frame, IObject[] args) {
		frame.exit();
	}

	private void desiredAssertionStatus0(KThread thread, StackFrame frame, IObject[] args) {
		frame.exit(new PrimitiveObject(PrimitiveType.Boolean, 0));
	}

	private void getPrimitiveClass(KThread thread, StackFrame frame, IObject[] args) {
		String name = ConversionUtils.parseString(args[0]);
		IType type;
		if (name.equals("int")) {
			type = PrimitiveType.Int;
		}else if (name.equals("float")) {
			type = PrimitiveType.Float;
		} else if (name.equals("double")) {
			type = PrimitiveType.Double;
		} else {
			throw new NotImplementedException(name);
		}
		frame.exit(
				thread.getInstancePool().getRuntimeClass(thread.getClassPool(), type, frame.getMethod().getKClass()));
	}

	private void arraycopy(KThread thread, StackFrame frame, IObject[] args) {
		ObjectInstance srcArray = ((ObjectReference) args[0]).getInstance();
		int srcStart = ((Number) ((PrimitiveObject) args[1]).getValue()).intValue();
		ObjectInstance dstArray = ((ObjectReference) args[2]).getInstance();
		int dstStart = ((Number) ((PrimitiveObject) args[3]).getValue()).intValue();
		int length = ((Number) ((PrimitiveObject) args[4]).getValue()).intValue();
		for (int i = 0; i < length; i++) {
			dstArray.setArrayElement(dstStart + i, srcArray.getArrayElement(srcStart + i).duplicate());
		}
		frame.exit();
	}

	private void floatToRawIntBits(KThread thread, StackFrame frame, IObject[] args) {
		float value = ((Number) ((PrimitiveObject) args[0]).getValue()).floatValue();
		frame.exit(new PrimitiveObject(PrimitiveType.Int, Float.floatToRawIntBits(value)));
	}

	private void doubleToRawLongBits(KThread thread, StackFrame frame, IObject[] args) {
		double value = ((Number) ((PrimitiveObject) args[0]).getValue()).doubleValue();
		frame.exit(new PrimitiveObject(PrimitiveType.Long, Double.doubleToRawLongBits(value)));
	}

	private void longBitsToDouble(KThread thread, StackFrame frame, IObject[] args) {
		long value = ((Number) ((PrimitiveObject) args[0]).getValue()).longValue();
		frame.exit(new PrimitiveObject(PrimitiveType.Double, Double.longBitsToDouble(value)));
	}

	private static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	private static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	private static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");

}
