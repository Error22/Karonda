package com.error22.karonda.vm;

import java.util.Stack;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KMethod;

public class KThread {
	private ClassPool pool;
	private Stack<StackFrame> frames;

	public KThread(ClassPool pool) {
		this.pool = pool;
		frames = new Stack<StackFrame>();
	}

	public void callMethod(KMethod method, IObject... arguments) {
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

	public ClassPool getPool() {
		return pool;
	}

}
