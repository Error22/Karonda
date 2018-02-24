package com.error22.karonda.vm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.InvokeInstruction;
import com.error22.karonda.instructions.InvokeInstruction.InvokeType;
import com.error22.karonda.instructions.LoadConstantInstruction;
import com.error22.karonda.instructions.LoadStringInstruction;
import com.error22.karonda.instructions.PopInstruction;
import com.error22.karonda.instructions.PopInstruction.PopMode;
import com.error22.karonda.instructions.ReturnInstruction;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
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
		loadObject();
		loadSunVM();
		loadThrowable();
	}

	public void loadPrimitives() {
		manager.addUnboundHook(this::empty, "registerNatives", PrimitiveType.Void);
		manager.addUnboundHook(this::desiredAssertionStatus0, "desiredAssertionStatus0", PrimitiveType.Boolean,
				CLASS_TYPE);
		manager.addUnboundHook(this::getPrimitiveClass, "getPrimitiveClass", CLASS_TYPE, STRING_TYPE);
		manager.addUnboundHook(this::returnArgsNonObject, "floatToRawIntBits", PrimitiveType.Int, PrimitiveType.Float);
		manager.addUnboundHook(this::returnArgsNonObject, "doubleToRawLongBits", PrimitiveType.Long,
				PrimitiveType.Double);
		manager.addUnboundHook(this::returnArgsNonObject, "longBitsToDouble", PrimitiveType.Double, PrimitiveType.Long);
	}

	public void loadSystem() {
		manager.addUnboundHook(this::arraycopy, "arraycopy", PrimitiveType.Void, OBJECT_TYPE, PrimitiveType.Int,
				OBJECT_TYPE, PrimitiveType.Int, PrimitiveType.Int);
		manager.addUnboundHook(this::nanoTime, "nanoTime", PrimitiveType.Long);
		manager.addUnboundHook(this::currentTimeMillis, "currentTimeMillis", PrimitiveType.Long);
		manager.addUnboundHook(this::identityHashCode, "identityHashCode", PrimitiveType.Int, OBJECT_TYPE);
	}

	public void loadObject() {
		manager.addUnboundHook(this::getClass, "getClass", CLASS_TYPE);
	}

	public void loadSunVM() {
		manager.addUnboundHook(this::initializeVM, "initialize", PrimitiveType.Void);
	}

	public void loadThrowable() {
		manager.addUnboundHook(this::returnFirstArgAsObject, "fillInStackTrace", THROWABLE_TYPE, PrimitiveType.Int);
	}

	private void initializeVM(KThread thread, StackFrame frame, int[] args) {
		frame.exit();
		try {
			Class<?> clazz = Class.forName("sun.misc.VM");
			Field field = clazz.getDeclaredField("savedProps");
			field.setAccessible(true);
			Properties prop = (Properties) field.get(null);

			KClass currentClass = frame.getMethod().getKClass();
			InstancePool instancePool = thread.getInstancePool();

			int ref = instancePool.getStaticField(currentClass, SAVED_PROPS_FIELD)[0];

			MethodSignature signature = new MethodSignature(currentClass.getName(), "initializeVM_impl",
					PrimitiveType.Void);
			KMethod initMethod = new KMethod(currentClass, signature, false, false, false);

			ArrayList<IInstruction> instructions = new ArrayList<IInstruction>();
			for (Entry<Object, Object> e : prop.entrySet()) {
				instructions.add(new LoadConstantInstruction(PrimitiveType.Int, ref, true));
				instructions.add(new LoadStringInstruction((String) e.getKey()));
				instructions.add(new LoadStringInstruction((String) e.getValue()));
				instructions.add(new InvokeInstruction(InvokeType.Virtual, SET_PROPERTY_METHOD, false));
				instructions.add(new PopInstruction(PopMode.Single));
			}
			instructions.add(new ReturnInstruction(PrimitiveType.Void));

			initMethod.setInstructions(instructions.toArray(new IInstruction[instructions.size()]));
			initMethod.setMaxLocals(0);
			initMethod.setMaxStack(3);

			thread.callMethod(initMethod, new int[0], new boolean[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void empty(KThread thread, StackFrame frame, int[] args) {
		frame.exit();
	}

	private void desiredAssertionStatus0(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { 0 }, false);
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
				thread.getInstancePool().getRuntimeClass(thread.getClassPool(), type, frame.getMethod().getKClass()) },
				true);
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

	private void nanoTime(KThread thread, StackFrame frame, int[] args) {
		long lval = System.nanoTime();
		frame.exit(new int[] { (int) (lval >> 32), (int) lval }, false);
	}

	private void currentTimeMillis(KThread thread, StackFrame frame, int[] args) {
		long lval = System.currentTimeMillis();
		frame.exit(new int[] { (int) (lval >> 32), (int) lval }, false);
	}

	private void identityHashCode(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { args[0] }, true);
	}

	private void returnArgsNonObject(KThread thread, StackFrame frame, int[] args) {
		frame.exit(args, false);
	}

	private void returnFirstArgAsObject(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { args[0] }, true);
	}

	private void getClass(KThread thread, StackFrame frame, int[] args) {
		InstancePool pool = thread.getInstancePool();
		ClassPool classPool = thread.getClassPool();
		ObjectInstance object = pool.getObject(args[0]);

		int type = pool.getRuntimeClass(classPool, object.getType(), frame.getMethod().getKClass());
		frame.exit(new int[] { type }, true);
	}

	private static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	private static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	private static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");
	private static final ObjectType THROWABLE_TYPE = new ObjectType("java/lang/Throwable");
	private static final ObjectType PROPERTIES_TYPE = new ObjectType("java/util/Properties");
	private static final FieldSignature SAVED_PROPS_FIELD = new FieldSignature("sun/misc/VM", "savedProps",
			PROPERTIES_TYPE);
	private static final MethodSignature SET_PROPERTY_METHOD = new MethodSignature("java/util/Properties",
			"setProperty", OBJECT_TYPE, STRING_TYPE, STRING_TYPE);
}
