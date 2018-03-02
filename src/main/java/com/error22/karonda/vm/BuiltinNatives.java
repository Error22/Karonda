package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.converter.ConversionUtils;
import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
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
		loadClass();
		loadSunVM();
		loadThrowable();
		loadFileDescriptor();
		loadUnsafe();
		loadReflection();
		loadString();
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
		manager.addUnboundHook(this::isAlive, "isAlive", PrimitiveType.Boolean);
		manager.addUnboundHook(this::startThread, "start0", PrimitiveType.Void);
	}

	public void loadSecurity() {
		manager.addUnboundHook(this::returnNull, "getStackAccessControlContext",
				new ObjectType("java/security/AccessControlContext"));
		manager.addUnboundHook(this::doPrivileged, "doPrivileged", ObjectType.OBJECT_TYPE,
				new ObjectType("java/security/PrivilegedAction"));
		manager.addUnboundHook(this::doPrivilegedException, "doPrivileged", ObjectType.OBJECT_TYPE,
				new ObjectType("java/security/PrivilegedExceptionAction"));
	}

	public void loadObject() {
		manager.addUnboundHook(this::getClass, "getClass", CLASS_TYPE);
		manager.addUnboundHook(this::returnFirstArgAsObject, "hashCode", PrimitiveType.Int);
	}

	public void loadClass() {
		manager.addUnboundHook(this::getDeclaredFields0, "getDeclaredFields0", ArrayType.REFLECT_FIELD_ARRAY,
				PrimitiveType.Boolean);
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
		manager.addUnboundHook(this::objectFieldOffset, "objectFieldOffset", PrimitiveType.Long,
				ObjectType.REFLECT_FIELD_TYPE);
		manager.addUnboundHook(this::compareAndSwapObject, "compareAndSwapObject", PrimitiveType.Boolean,
				ObjectType.OBJECT_TYPE, PrimitiveType.Long, ObjectType.OBJECT_TYPE, ObjectType.OBJECT_TYPE);
	}

	public void loadReflection() {
		manager.addUnboundHook(this::getCallerClass, "getCallerClass", ObjectType.CLASS_TYPE);
	}

	public void loadString() {
		manager.addUnboundHook(this::intern, "intern", ObjectType.STRING_TYPE);
	}

	private void empty(KThread thread, StackFrame frame, int[] args) {
		frame.exit();
	}

	private void desiredAssertionStatus0(KThread thread, StackFrame frame, int[] args) {
		frame.exit(new int[] { 0 }, false);
	}

	private void getPrimitiveClass(KThread thread, StackFrame frame, int[] args) {
		String name = thread.getInstancePool().getStringContent(args[0]);
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

	private void isAlive(KThread thread, StackFrame frame, int[] args) {
		if (args[0] == 0) {
			throw new NotImplementedException();
		}

		KThread target = thread.getThreadManager().getThreadByRef(args[0]);
		frame.exit(new int[] { target != null ? 1 : 0 }, false);
	}

	private void startThread(KThread thread, StackFrame frame, int[] args) {
		ThreadManager threadManager = thread.getThreadManager();
		InstancePool instancePool = thread.getInstancePool();

		int oid = args[0];
		if (oid == 0)
			throw new NotImplementedException("Null object supported not implemented");

		if (threadManager.getThreadByRef(oid) != null) {
			throw new IllegalStateException();
		}

		ObjectInstance instance = instancePool.getObject(oid);
		KClass targetClass = instance.getKClass();
		KMethod resolved = targetClass.findMethod(THREAD_RUN_METHOD, true);

		KThread newThread = new KThread(threadManager, thread.getClassPool(), thread.getInstancePool(),
				thread.getNativeManager());
		newThread.setThreadObjRef(oid);
		threadManager.addThread(newThread);
		newThread.initAndCall(resolved, false, new int[] { args[0] }, new boolean[] { true });

		thread.exitFrame();
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

	private void doPrivilegedException(KThread thread, StackFrame frame, int[] args) {
		if (thread.getFrames().pop() != frame) {
			throw new IllegalStateException("Unexpected frame");
		}

		InstancePool instancePool = thread.getInstancePool();
		int oid = args[0];
		if (oid == 0)
			throw new NotImplementedException("Null object supported not implemented");
		ObjectInstance instance = instancePool.getObject(oid);

		KClass targetClass = instance.getKClass();
		KMethod resolved = targetClass.findMethod(PRIVILEGED_EXCEPTION_ACTION_RUN_METHOD, true);
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

	private void getDeclaredFields0(KThread thread, StackFrame frame, int[] args) {
		InstancePool instancePool = thread.getInstancePool();
		ClassPool classPool = thread.getClassPool();
		KClass currentClass = frame.getMethod().getKClass();

		IType type = instancePool.getTypeFromRuntimeClass(args[0]);
		List<Integer> ids = new ArrayList<Integer>();

		if (type instanceof PrimitiveType || type instanceof ArrayType) {
		} else if (type instanceof ObjectType) {
			KClass clazz = classPool.getClass(((ObjectType) type).getName(), currentClass);

			List<KField> fields = new ArrayList<KField>();
			clazz.getAllFields(fields);

			// TODO: Add public check using first argument
			for (KField field : fields) {
				ids.add(createReflectionField(field, thread, frame));
			}

		} else {
			throw new IllegalArgumentException();
		}

		int ref = instancePool.createArray(classPool, ArrayType.REFLECT_FIELD_ARRAY, ids.size());
		ObjectInstance inst = instancePool.getObject(ref);

		for (int i = 0; i < ids.size(); i++) {
			inst.setArrayElement(i, new int[] { ids.get(i) });
		}

		frame.exit(new int[] { ref }, true);
	}

	private int createReflectionField(KField field, KThread thread, StackFrame stackFrame) {
		KClass currentClass = stackFrame.getMethod().getKClass();
		ClassPool classPool = thread.getClassPool();
		InstancePool instancePool = thread.getInstancePool();
		KClass targetClass = classPool.getClass(ObjectType.REFLECT_FIELD_TYPE.getName(), currentClass);
		int ref = instancePool.createInstance(targetClass, ObjectType.REFLECT_FIELD_TYPE);
		ObjectInstance instance = instancePool.getObject(ref);

		// Class
		instance.setField(new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "clazz", ObjectType.CLASS_TYPE),
				new int[] { instancePool.getRuntimeClass(classPool, new ObjectType(field.getSignature().getClazz()),
						currentClass) });
		// Slot
		instance.setField(new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "slot", PrimitiveType.Int),
				new int[] { field.getIndex() });
		// Name
		instance.setField(new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "name", ObjectType.STRING_TYPE),
				new int[] { instancePool.getStringInstance(field.getSignature().getName()) });
		// Type
		instance.setField(new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "type", ObjectType.CLASS_TYPE),
				new int[] { instancePool.getRuntimeClass(classPool, field.getSignature().getType(), currentClass) });
		// Override
		instance.setField(new FieldSignature(ObjectType.REFLECT_ACCESSIBLE_OBJECT_TYPE.getName(), "override",
				PrimitiveType.Boolean), new int[] { 0 });
		// TODO: Add modifier, generic signature & annotations

		return ref;
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

	private void objectFieldOffset(KThread thread, StackFrame frame, int[] args) {
		InstancePool instancePool = thread.getInstancePool();
		ClassPool classPool = thread.getClassPool();
		ObjectInstance fieldInst = instancePool.getObject(args[1]);

		int classRef = fieldInst.getField(
				new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "clazz", ObjectType.CLASS_TYPE))[0];
		ObjectType type = (ObjectType) instancePool.getTypeFromRuntimeClass(classRef);
		KClass clazz = classPool.getClass(type.getName(), frame.getMethod().getKClass());

		int slot = fieldInst
				.getField(new FieldSignature(ObjectType.REFLECT_FIELD_TYPE.getName(), "slot", PrimitiveType.Int))[0];

		List<KField> fields = new ArrayList<KField>();
		clazz.getAllFields(fields);

		long offset = 0;
		boolean found = false;
		for (KField field : fields) {
			if (field.getIndex() == slot) {
				found = true;
				break;
			}
			offset += field.getSignature().getType().getSize() * 4;
		}

		if (!found) {
			throw new RuntimeException("Failed to find field");
		}

		frame.exit(ConversionUtils.convertLong(offset), false);
	}

	private void compareAndSwapObject(KThread thread, StackFrame frame, int[] args) {
		InstancePool instancePool = thread.getInstancePool();
		ObjectInstance objInst = instancePool.getObject(args[1]);

		KClass clazz = objInst.getKClass();
		List<KField> fields = new ArrayList<KField>();
		clazz.getAllFields(fields);

		long targetOffset = ConversionUtils.parseLong(args, 2);
		long offset = 0;
		KField targetField = null;
		for (KField field : fields) {
			if (offset == targetOffset) {
				targetField = field;
				break;
			}
			offset += field.getSignature().getType().getSize() * 4;
		}

		if (targetField == null) {
			throw new RuntimeException("Failed to find field");
		}

		int current = objInst.getField(targetField.getSignature())[0];

		boolean set = false;
		if (current == args[4]) {
			objInst.setField(targetField.getSignature(), new int[] { args[5] });
			set = true;
		}

		frame.exit(new int[] { set ? 1 : 0 }, false);
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

	private void intern(KThread thread, StackFrame frame, int[] args) {
		InstancePool pool = thread.getInstancePool();
		frame.exit(new int[] { pool.getStringInstance(pool.getStringContent(args[0])) }, true);
	}

	private static final ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
	private static final ObjectType CLASS_TYPE = new ObjectType("java/lang/Class");
	private static final ObjectType STRING_TYPE = new ObjectType("java/lang/String");
	private static final ObjectType THROWABLE_TYPE = new ObjectType("java/lang/Throwable");
	private static final MethodSignature PRIVILEGED_ACTION_RUN_METHOD = new MethodSignature(
			"java/security/PrivilegedAction", "run", ObjectType.OBJECT_TYPE);
	private static final MethodSignature PRIVILEGED_EXCEPTION_ACTION_RUN_METHOD = new MethodSignature(
			"java/security/PrivilegedExceptionAction", "run", ObjectType.OBJECT_TYPE);
	private static final MethodSignature THREAD_RUN_METHOD = new MethodSignature("java/lang/Thread", "run",
			PrimitiveType.Void);
}
