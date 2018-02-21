package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.PrimitiveType;

public class KThread {
	private ClassPool classPool;
	private InstancePool instancePool;
	private NativeManager nativeManager;
	private Stack<StackFrame> frames;

	public KThread(ClassPool classPool, InstancePool instancePool, NativeManager nativeManager) {
		this.classPool = classPool;
		this.instancePool = instancePool;
		this.nativeManager = nativeManager;
		frames = new Stack<StackFrame>();
	}

	public void initAndCall(KMethod method, boolean instructionPushBack, int[] arguments) {
		boolean pushed = staticInit(method.getKClass(), instructionPushBack);
		callMethod(method, pushed, arguments);
	}

	public boolean staticInit(KClass clazz, boolean instructionPushBack) {
		KMethod sinit = instancePool.staticInit(clazz);
		if (sinit != null) {
			if (instructionPushBack && !frames.isEmpty())
				frames.peek().moveInstructionPointer(-1);
			callMethod(sinit, new int[0]);
			return true;
		}
		return false;
	}

	public void callMethod(KMethod method, int[] arguments) {
		callMethod(method, false, arguments);
	}

	public void callMethod(KMethod method, boolean pushBack, int[] arguments) {
		if (!instancePool.hasStaticInit(method.getKClass()))
			throw new IllegalStateException("Class has not been staticly initialized");

		StackFrame frame = new StackFrame(this, method);
		frame.init(arguments);
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

	public void exitFrame(int[] result) {
		StackFrame frame = frames.pop();
		if (frame.getMethod().getSignature().getReturnType().equals(PrimitiveType.Void))
			throw new IllegalArgumentException("Only non-void frames can be exited with return data");
		if (!frames.isEmpty())
			frames.peek().push(result);
	}

	public StackFrame getCurrentFrame() {
		return frames.peek();
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

}
