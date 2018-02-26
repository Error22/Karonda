package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.PrimitiveType;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntCollection;

public class KThread {
	private ThreadManager threadManager;
	private ClassPool classPool;
	private InstancePool instancePool;
	private NativeManager nativeManager;
	private Stack<StackFrame> frames;
	private Int2IntMap lockCounts;
	private int threadObjRef;

	public KThread(ThreadManager threadManager, ClassPool classPool, InstancePool instancePool,
			NativeManager nativeManager) {
		this.threadManager = threadManager;
		this.classPool = classPool;
		this.instancePool = instancePool;
		this.nativeManager = nativeManager;
		frames = new Stack<StackFrame>();
		lockCounts = new Int2IntOpenHashMap();
	}

	public void initAndCall(KMethod method, boolean instructionPushBack, int[] arguments,
			boolean[] argumentsObjectMap) {
		boolean pushed = staticInit(method.getKClass(), instructionPushBack);
		callMethod(method, pushed, arguments, argumentsObjectMap);
	}

	public boolean staticInit(KClass clazz, boolean instructionPushBack) {
		KMethod sinit = instancePool.staticInit(clazz);
		if (sinit != null) {
			if (instructionPushBack && !frames.isEmpty())
				frames.peek().moveInstructionPointer(-1);
			callMethod(sinit, new int[0], new boolean[0]);
			return true;
		}
		return false;
	}

	public void callMethod(KMethod method, int[] arguments, boolean[] argumentsObjectMap) {
		callMethod(method, false, arguments, argumentsObjectMap);
	}

	public void callMethod(KMethod method, boolean pushBack, int[] arguments, boolean[] argumentsObjectMap) {
		if (!instancePool.hasStaticInit(method.getKClass()))
			throw new IllegalStateException("Class has not been staticly initialized");

		StackFrame frame = new StackFrame(this, method);
		frame.init(arguments, argumentsObjectMap);
		if (pushBack) {
			StackFrame top = frames.pop();
			frames.push(frame);
			frames.push(top);
		} else
			frames.push(frame);

	}

	public void step() {
		frames.peek().step();
	}

	public void exitFrame() {
		StackFrame frame = frames.pop();
		if (!frame.getMethod().getSignature().getReturnType().equals(PrimitiveType.Void))
			throw new IllegalArgumentException("Only void frames can be exited without return data");
	}

	public void exitFrame(int[] result, boolean isObject) {
		StackFrame frame = frames.pop();
		if (frame.getMethod().getSignature().getReturnType().equals(PrimitiveType.Void))
			throw new IllegalArgumentException("Only non-void frames can be exited with return data");
		if (!frames.isEmpty())
			frames.peek().push(result, isObject);
	}

	public StackFrame getCurrentFrame() {
		return frames.peek();
	}

	public boolean hasFrame() {
		return !frames.isEmpty();
	}

	public Stack<StackFrame> getFrames() {
		return frames;
	}

	public void lock(int id) {
		lockCounts.put(id, lockCounts.getOrDefault(id, 0) + 1);
	}

	public void unlock(int id) {
		int count = lockCounts.get(id);
		if (count == 1)
			lockCounts.remove(id);
		else
			lockCounts.put(id, count - 1);
	}

	public boolean isLocked(int id) {
		return lockCounts.containsKey(id);
	}

	public IntCollection getLocks() {
		return lockCounts.values();
	}

	public ThreadManager getThreadManager() {
		return threadManager;
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

	public void setThreadObjRef(int threadObjRef) {
		this.threadObjRef = threadObjRef;
	}

	public int getThreadObjRef() {
		return threadObjRef;
	}

}
