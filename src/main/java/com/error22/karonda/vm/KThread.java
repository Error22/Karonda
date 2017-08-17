package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KMethod;

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

	public void initAndCall(KMethod method, boolean instructionPushBack, IObject... arguments) {
		boolean pushed = staticInit(method.getKClass(), instructionPushBack);
		callMethod(method, pushed, arguments);
	}

	public boolean staticInit(KClass clazz, boolean instructionPushBack) {
		KMethod sinit = instancePool.staticInit(clazz);
		if (sinit != null) {
			if (instructionPushBack && !frames.isEmpty())
				frames.peek().moveInstructionPointer(-1);
			callMethod(sinit);
			return true;
		}
		return false;
	}

	public void callMethod(KMethod method, IObject... arguments) {
		callMethod(method, false, arguments);
	}

	public void callMethod(KMethod method, boolean pushBack, IObject... arguments) {
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

	public void exitFrame(IObject result) {
		frames.pop();
		if (result != null)
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
