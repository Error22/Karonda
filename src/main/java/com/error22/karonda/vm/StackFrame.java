package com.error22.karonda.vm;

import java.util.Map;

import org.objectweb.asm.Label;

import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.ir.KMethod;

public class StackFrame {
	private KThread thread;
	private KMethod method;
	private IInstruction[] instructions;
	private Map<Label, Integer> labelMap;
	private int instructionPointer, stackPointer;
	private int[] locals, stack;
	private boolean nativeRan;

	public StackFrame(KThread thread, KMethod method) {
		this.thread = thread;
		this.method = method;
		instructions = method.getInstructions();
		labelMap = method.getLabelMap();
	}

	public void init(int[] arguments) {
		if (method.isNative()) {
			locals = new int[arguments.length];
			System.arraycopy(arguments, 0, locals, 0, arguments.length);
			return;
		}
		locals = new int[method.getMaxLocals()];
		stack = new int[method.getMaxStack()];
		System.arraycopy(arguments, 0, locals, 0, arguments.length);
	}

	public void step() {
		if (method.isNative()) {
			if (nativeRan)
				throw new IllegalStateException();
			thread.getNativeManager().invokeNative(method.getSignature(), thread, this, locals);
			nativeRan = true;
			return;
		}

		IInstruction instruction = instructions[instructionPointer];
		instructionPointer++;
		instruction.execute(this);
	}

	public void jump(Label label) {
		instructionPointer = labelMap.get(label);
	}

	public void push(int object) {
		stack[stackPointer] = object;
		stackPointer++;
	}

	public void push(int[] object) {
		for (int i = object.length - 1; i >= 0; i--) {
			stack[stackPointer] = object[i];
			stackPointer++;
		}
	}

	public void push(long object) {
		stack[stackPointer] = (int) object;
		stack[stackPointer + 1] = (int) (object >> 32);
		stackPointer += 2;
	}

	public int pop() {
		stackPointer--;
		return stack[stackPointer];
	}

	public int[] pop(int size) {
		int[] object = new int[size];
		for (int i = 0; i < object.length; i++) {
			stackPointer--;
			object[i] = stack[stackPointer];
		}
		return object;
	}

	public long popLong() {
		int a = pop();
		int b = pop();
		return (long) a << 32 | b & 0xFFFFFFFFL;
	}

	public void setLocal(int index, int object) {
		locals[index] = object;
	}

	public int getLocal(int index) {
		return locals[index];
	}

	public KThread getThread() {
		return thread;
	}

	public KMethod getMethod() {
		return method;
	}

	public void moveInstructionPointer(int delta) {
		this.instructionPointer += delta;
	}

	public int getInstructionPointer() {
		return instructionPointer;
	}

	public void setInstructionPointer(int instructionPointer) {
		this.instructionPointer = instructionPointer;
	}

	public void exit() {
		thread.exitFrame();
	}

	public void exit(int[] result) {
		thread.exitFrame(result);
	}

	@Override
	public String toString() {
		return "StackFrame [thread=" + thread + ", method=" + method + ", instructionPointer=" + instructionPointer
				+ ", stackPointer=" + stackPointer + "]";
	}

}
