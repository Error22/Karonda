package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;

import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.InvokeInstruction;
import com.error22.karonda.instructions.InvokeInstruction.InvokeType;
import com.error22.karonda.instructions.LoadConstantInstruction;
import com.error22.karonda.instructions.LoadStringInstruction;
import com.error22.karonda.instructions.LocalInstruction;
import com.error22.karonda.instructions.LocalInstruction.LocalOperation;
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
	private IVMHost vmHost;
	private ClassPool classPool;
	private MemoryManager memoryManager;
	private InstancePool instancePool;
	private NativeManager nativeManager;
	private ThreadManager threadManager;
	private SignalManager signalManager;
	private KClass autoStartClass;

	public KarondaVM(IVMHost vmHost, ClassPool classPool, MemoryManager memoryManager, InstancePool instancePool,
			NativeManager nativeManager, ThreadManager threadManager, SignalManager signalManager) {
		this.vmHost = vmHost;
		this.classPool = classPool;
		this.memoryManager = memoryManager;
		this.instancePool = instancePool;
		this.nativeManager = nativeManager;
		this.threadManager = threadManager;
		this.signalManager = signalManager;
	}

	public KarondaVM(IVMHost vmHost, BootstrapClassLoader bootstrapClassLoader) {
		this.vmHost = vmHost;
		classPool = new ClassPool(bootstrapClassLoader);
		memoryManager = new MemoryManager();
		instancePool = new InstancePool(classPool);
		threadManager = new ThreadManager();
		nativeManager = new NativeManager();
		signalManager = new SignalManager();

		signalManager.registerDefaults();

		autoStartClass = new KClass("__AutoStartVMClass", ClassType.Class, 0, true, null, new String[0]);

		BuiltinNatives builtinNatives = new BuiltinNatives(vmHost, signalManager, nativeManager,
				createInitPropertiesMethod());
		builtinNatives.loadAll();
	}

	private KMethod createInitPropertiesMethod() {
		KMethod initPropertiesMethod = new KMethod(autoStartClass,
				new MethodSignature(autoStartClass.getName(), "__InitVM", PrimitiveType.Void), 0, false, false, false);

		ArrayList<IInstruction> instructions = new ArrayList<IInstruction>();
		try {
			Properties properties = vmHost.getSunProperties();

			// Store values into savedProps
			MethodSignature setPropertyMethod = new MethodSignature("java/util/Properties", "setProperty",
					ObjectType.OBJECT_TYPE, ObjectType.STRING_TYPE, ObjectType.STRING_TYPE);
			for (Entry<Object, Object> e : properties.entrySet()) {
				instructions.add(new LocalInstruction(LocalOperation.Load, ObjectType.PROPERTIES_TYPE, 0));
				instructions.add(new LoadStringInstruction((String) e.getKey()));
				instructions.add(new LoadStringInstruction((String) e.getValue()));
				instructions.add(new InvokeInstruction(InvokeType.Virtual, setPropertyMethod, false));
				instructions.add(new PopInstruction(PopMode.Single));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		instructions.add(new ReturnInstruction(PrimitiveType.Void));
		initPropertiesMethod.setInstructions(instructions.toArray(new IInstruction[instructions.size()]));
		initPropertiesMethod.setMaxLocals(1);
		initPropertiesMethod.setMaxStack(3);

		return initPropertiesMethod;
	}

	public void start(MethodSignature mainMethod) {
		KMethod initVMMethod = new KMethod(autoStartClass,
				new MethodSignature(autoStartClass.getName(), "__InitVM", PrimitiveType.Void), 0, false, false, false);

		ArrayList<IInstruction> instructions = new ArrayList<IInstruction>();

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

		// Initialise system class
		instructions.add(new InvokeInstruction(InvokeType.Static,
				new MethodSignature("java/lang/System", "initializeSystemClass", PrimitiveType.Void), false));

		// Invoke main method
		if (mainMethod != null) {
			instructions.add(new InvokeInstruction(InvokeType.Static, mainMethod, false));
			if (mainMethod.getReturnType() != PrimitiveType.Void) {
				instructions.add(new PopInstruction(
						mainMethod.getReturnType().getSize() == 2 ? PopMode.Double : PopMode.Single));

			}
		}

		instructions.add(new ReturnInstruction(PrimitiveType.Void));
		initVMMethod.setInstructions(instructions.toArray(new IInstruction[instructions.size()]));
		initVMMethod.setMaxLocals(0);
		initVMMethod.setMaxStack(3);

		KThread mainThread = new KThread(threadManager, classPool, memoryManager, instancePool, nativeManager);
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

	public MemoryManager getMemoryManager() {
		return memoryManager;
	}

	public SignalManager getSignalManager() {
		return signalManager;
	}

}