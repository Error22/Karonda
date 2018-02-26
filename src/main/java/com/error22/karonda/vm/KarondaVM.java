package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.Arrays;

import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.InvokeInstruction;
import com.error22.karonda.instructions.InvokeInstruction.InvokeType;
import com.error22.karonda.instructions.LoadConstantInstruction;
import com.error22.karonda.instructions.LoadStringInstruction;
import com.error22.karonda.instructions.PopInstruction;
import com.error22.karonda.instructions.PopInstruction.PopMode;
import com.error22.karonda.instructions.ReturnInstruction;
import com.error22.karonda.ir.ClassType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;

public class KarondaVM {
	private ClassPool classPool;
	private InstancePool instancePool;
	private NativeManager nativeManager;
	private ThreadManager threadManager;

	public KarondaVM(BootstrapClassLoader bootstrapClassLoader) {
		classPool = new ClassPool(bootstrapClassLoader);
		instancePool = new InstancePool();
		threadManager = new ThreadManager();
		nativeManager = new NativeManager();

		BuiltinNatives builtinNatives = new BuiltinNatives(nativeManager);
		builtinNatives.loadAll();
	}

	public void start(MethodSignature mainMethod) {
		KClass autoStartClass = new KClass("__AutoStartVMClass", ClassType.Class, true, null, new String[0]);
		KMethod initVMMethod = new KMethod(autoStartClass,
				new MethodSignature(autoStartClass.getName(), "__InitVM", PrimitiveType.Void), false, false, false);

		ArrayList<IInstruction> instructions = new ArrayList<IInstruction>();
		for (String clazz : Arrays.asList("java/lang/System", "java/lang/ThreadGroup", "java/lang/Thread")) {
			KMethod method = instancePool.staticInit(classPool.bootstrapResolve(clazz));
			if (method != null) {
				MethodSignature signature = method.getSignature();
				instructions.add(new InvokeInstruction(InvokeType.Static, signature, false));
			}
		}

		// Create thread groups
		KClass threadGroupClass = classPool.bootstrapResolve(ObjectType.THREAD_GROUP_TYPE.getName());
		int sysThreadGroupRef = instancePool.createInstance(threadGroupClass, ObjectType.THREAD_GROUP_TYPE);
		int mainThreadGroupRef = instancePool.createInstance(threadGroupClass, ObjectType.THREAD_GROUP_TYPE);

		// Construct system group
		instructions.add(new LoadConstantInstruction(PrimitiveType.Int, sysThreadGroupRef, true));
		instructions.add(new InvokeInstruction(InvokeType.Special,
				new MethodSignature(ObjectType.THREAD_GROUP_TYPE.getName(), "<init>", PrimitiveType.Void), false));

		// Construct main group
		instructions.add(new LoadConstantInstruction(PrimitiveType.Int, mainThreadGroupRef, true));
		instructions.add(new LoadConstantInstruction(PrimitiveType.Int, sysThreadGroupRef, true));
		instructions.add(new LoadStringInstruction("main"));
		instructions.add(
				new InvokeInstruction(InvokeType.Special, new MethodSignature(ObjectType.THREAD_GROUP_TYPE.getName(),
						"<init>", PrimitiveType.Void, ObjectType.THREAD_GROUP_TYPE, ObjectType.STRING_TYPE), false));

		// Create main thread
		KClass threadClass = classPool.bootstrapResolve(ObjectType.THREAD_TYPE.getName());
		int mainThreadRef = instancePool.createInstance(threadClass, ObjectType.THREAD_TYPE);

		// Set main thread priority
		instancePool.getObject(mainThreadRef).setField(
				new FieldSignature(ObjectType.THREAD_TYPE.getName(), "priority", PrimitiveType.Int), new int[] { 1 });

		// Construct main thread
		instructions.add(new LoadConstantInstruction(PrimitiveType.Int, mainThreadRef, true));
		instructions.add(new LoadConstantInstruction(PrimitiveType.Int, mainThreadGroupRef, true));
		instructions.add(new LoadStringInstruction("main"));
		instructions.add(new InvokeInstruction(InvokeType.Special, new MethodSignature(ObjectType.THREAD_TYPE.getName(),
				"<init>", PrimitiveType.Void, ObjectType.THREAD_GROUP_TYPE, ObjectType.STRING_TYPE), false));

		// Invoke main method
		instructions.add(new InvokeInstruction(InvokeType.Static, mainMethod, false));
		if (mainMethod.getReturnType() != PrimitiveType.Void) {
			instructions.add(
					new PopInstruction(mainMethod.getReturnType().getSize() == 2 ? PopMode.Double : PopMode.Single));

		}

		instructions.add(new ReturnInstruction(PrimitiveType.Void));
		initVMMethod.setInstructions(instructions.toArray(new IInstruction[instructions.size()]));
		initVMMethod.setMaxLocals(0);
		initVMMethod.setMaxStack(3);

		KThread mainThread = new KThread(threadManager, classPool, instancePool, nativeManager);
		mainThread.setThreadObjRef(mainThreadRef);
		threadManager.setMainThread(mainThread);
		mainThread.initAndCall(initVMMethod, false, new int[0], new boolean[0]);
	}

	public boolean step() {
		// TODO: Implement multithreaded stepping
		KThread mainThread = threadManager.getMainThread();
		if (!mainThread.hasFrame())
			return false;
		mainThread.step();
		return true;
	}

	public ClassPool getClassPool() {
		return classPool;
	}

	public InstancePool getInstancePool() {
		return instancePool;
	}

	public NativeManager getNativeManager() {
		return nativeManager;
	}

	public ThreadManager getThreadManager() {
		return threadManager;
	}
}