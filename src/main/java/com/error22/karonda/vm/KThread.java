package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.ir.IObject;
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

	public void callMethod(KMethod method, IObject... arguments) {
		if (!instancePool.hasStaticInit(method.getKClass()))
			throw new IllegalStateException("Class has not been staticly initialized");

		if (method.isNative()) {
			nativeManager.invokeNative(method.getSignature(), this, arguments);
			return;
		}
		StackFrame frame = new StackFrame(this, method);
		frame.init(arguments);
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

	public ClassPool getClassPool() {
		return classPool;
	}

	public InstancePool getInstancePool() {
		return instancePool;
	}

}
