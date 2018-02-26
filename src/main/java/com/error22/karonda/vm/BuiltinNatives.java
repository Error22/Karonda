package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;

public class BuiltinNatives {
	private NativeManager manager;
	private KMethod initPropertiesMethod;

	public BuiltinNatives(NativeManager manager, KMethod initPropertiesMethod) {
		this.manager = manager;
		this.initPropertiesMethod = initPropertiesMethod;
	}

	public void loadAll() {
		loadPrimitives();
		loadSystem();
		loadRuntime();
		loadThread();
		loadSecurity();
		loadObject();
		loadSunVM();
		loadThrowable();
		loadFileDescriptor();
		loadUnsafe();
		loadReflection();
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
		manager.addUnboundHook(this::returnFirstArgAsObject, "identityHashCode", PrimitiveType.Int, OBJECT_TYPE);
		manager.addUnboundHook(this::initProperties, "initProperties", ObjectType.PROPERTIES_TYPE,
				ObjectType.PROPERTIES_TYPE);
	}

	public void loadRuntime() {
		manager.addUnboundHook(this::maxMemory, "maxMemory", PrimitiveType.Long);
	}

	public void loadThread() {
		manager.addUnboundHook(this::currentThread, "currentThread", ObjectType.THREAD_TYPE);
		manager.addUnboundHook(this::empty, "setPriority0", PrimitiveType.Void, PrimitiveType.Int);
	}

	public void loadSecurity() {
		manager.addUnboundHook(this::returnNull, "getStackAccessControlContext",
				new ObjectType("java/security/AccessControlContext"));
		manager.addUnboundHook(this::doPrivileged, "doPrivileged", ObjectType.OBJECT_TYPE,
				new ObjectType("java/security/PrivilegedAction"));
	}

	public void loadObject() {
		manager.addUnboundHook(this::getClass, "getClass", CLASS_TYPE);
		manager.addUnboundHook(this::returnFirstArgAsObject, "hashCode", PrimitiveType.Int);
	}

	public void loadSunVM() {
		manager.addUnboundHook(this::empty, "initialize", PrimitiveType.Void);
	}

	public void loadThrowable() {
		manager.addUnboundHook(this::returnFirstArgAsObject, "fillInStackTrace", THROWABLE_TYPE, PrimitiveType.Int);
	}

	public void loadFileDescriptor() {
		manager.addUnboundHook(this::empty, "initIDs", PrimitiveType.Void);
	}

	public void loadUnsafe() {
		manager.addUnboundHook(this::arrayBaseOffset, "arrayBaseOffset", PrimitiveType.Int, ObjectType.CLASS_TYPE);
		manager.addUnboundHook(this::arrayIndexScale, "arrayIndexScale", PrimitiveType.Int, ObjectType.CLASS_TYPE);
		manager.addUnboundHook(this::addressSize, "addressSize", PrimitiveType.Int);
	}

	public void loadReflection() {
		manager.addUnboundHook(this::getCallerClass, "getCallerClass", ObjectType.CLASS_TYPE);
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

	private void initProperties(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { args[0] }, true);
		thread.callMethod(initPropertiesMethod, new int[] { args[0] }, new boolean[] { true });
	}

	private void maxMemory(KThread thread, StackFrame frame, int[] args) {
		// TODO: Implement memory manager
		long lval = Long.MAX_VALUE;
		frame.exit(new int[] { (int) (lval >> 32), (int) lval }, false);
	}

	private void currentThread(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { thread.getThreadObjRef() }, true);
	}

	private void doPrivileged(KThread thread, StackFrame frame, int[] args) {
		if (thread.getFrames().pop() != frame) {
			throw new IllegalStateException("Unexpected frame");
		}

		InstancePool instancePool = thread.getInstancePool();
		int oid = args[0];
		if (oid == 0)
			throw new NotImplementedException("Null object supported not implemented");
		ObjectInstance instance = instancePool.getObject(oid);

		KClass targetClass = instance.getKClass();
		KMethod resolved = targetClass.findMethod(PRIVILEGED_ACTION_RUN_METHOD, true);
		thread.initAndCall(resolved, false, new int[] { args[0] }, new boolean[] { true });
	}

	private void returnArgsNonObject(KThread thread, StackFrame frame, int[] args) {
		frame.exit(args, false);
	}

	private void returnNull(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[1], true);
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

	private void arrayBaseOffset(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[1], false);
	}

	private void arrayIndexScale(KThread thread, StackFrame frame, int[] args) {
		ArrayType arrayType = (ArrayType) thread.getInstancePool().getTypeFromRuntimeClass(args[1]);
		frame.exit(new int[] { arrayType.getType().getSize() * 4 }, false);
	}

	private void addressSize(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { 4 }, false);
	}

	private void getCallerClass(KThread thread, StackFrame frame, int[] args) {
		InstancePool pool = thread.getInstancePool();
		ClassPool classPool = thread.getClassPool();

		Stack<StackFrame> sf = thread.getFrames();
		StackFrame target = sf.get(sf.size() - 3);
		int type = pool.getRuntimeClass(classPool, new ObjectType(target.getMethod().getKClass().getName()),
				frame.getMethod().getKClass());
		frame.exit(new int[] { type }, true);
	}

	private static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	private static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	private static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");
	private static final ObjectType THROWABLE_TYPE = new ObjectType("java/lang/Throwable");
	private static final MethodSignature PRIVILEGED_ACTION_RUN_METHOD = new MethodSignature(
			"java/security/PrivilegedAction", "run", ObjectType.OBJECT_TYPE);
}
