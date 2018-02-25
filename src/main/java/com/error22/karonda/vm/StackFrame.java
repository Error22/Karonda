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
	private boolean[] localsObjectMap, stackObjectMap;
	private boolean nativeRan;

	public StackFrame(KThread thread, KMethod method) {
		this.thread = thread;
		this.method = method;
		instructions = method.getInstructions();
		labelMap = method.getLabelMap();
	}

	public void init(int[] arguments, boolean[] argumentsObjectMap) {
		if (method.isNative()) {
			locals = new int[arguments.length];
			System.arraycopy(arguments, 0, locals, 0, arguments.length);
			localsObjectMap = new boolean[argumentsObjectMap.length];
			System.arraycopy(argumentsObjectMap, 0, localsObjectMap, 0, argumentsObjectMap.length);
			return;
		}
		locals = new int[method.getMaxLocals()];
		localsObjectMap = new boolean[method.getMaxLocals()];
		stack = new int[method.getMaxStack()];
		stackObjectMap = new boolean[method.getMaxStack()];
		System.arraycopy(arguments, 0, locals, 0, arguments.length);
		System.arraycopy(argumentsObjectMap, 0, localsObjectMap, 0, argumentsObjectMap.length);
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

	public void push(int object, boolean isObject) {
		stack[stackPointer] = object;
		stackObjectMap[stackPointer] = isObject;
		stackPointer++;
	}

	public void pushInt(int object) {
		stack[stackPointer] = object;
		stackObjectMap[stackPointer] = false;
		stackPointer++;
	}

	public void pushObject(int object) {
		stack[stackPointer] = object;
		stackObjectMap[stackPointer] = true;
		stackPointer++;
	}

	public void pushFloat(float object) {
		stack[stackPointer] = Float.floatToRawIntBits(object);
		stackObjectMap[stackPointer] = false;
		stackPointer++;
	}

	public void push(int[] object, boolean isObject) {
		for (int i = object.length - 1; i >= 0; i--) {
			stack[stackPointer] = object[i];
			stackObjectMap[stackPointer] = isObject;
			stackPointer++;
		}
	}

	public void pushLong(long object) {
		stack[stackPointer] = (int) object;
		stackObjectMap[stackPointer] = false;
		stack[stackPointer + 1] = (int) (object >> 32);
		stackObjectMap[stackPointer + 1] = false;
		stackPointer += 2;
	}

	public void pushDouble(double object) {
		long value = Double.doubleToRawLongBits(object);
		stack[stackPointer] = (int) value;
		stackObjectMap[stackPointer] = false;
		stack[stackPointer + 1] = (int) (value >> 32);
		stackObjectMap[stackPointer + 1] = false;
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

	public float popFloat() {
		stackPointer--;
		return Float.intBitsToFloat(stack[stackPointer]);
	}

	public long popLong() {
		int a = stack[stackPointer - 1];
		int b = stack[stackPointer - 2];
		stackPointer -= 2;
		return (long) a << 32 | b & 0xFFFFFFFFL;
	}

	public double popDouble() {
		int a = stack[stackPointer - 1];
		int b = stack[stackPointer - 2];
		stackPointer -= 2;
		return Double.longBitsToDouble((long) a << 32 | b & 0xFFFFFFFFL);
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

	public int[] getStack() {
		return stack;
	}

	public int getStackPointer() {
		return stackPointer;
	}

	public void setStackPointer(int stackPointer) {
		this.stackPointer = stackPointer;
	}

	public boolean[] getStackObjectMap() {
		return stackObjectMap;
	}

	public int[] getLocals() {
		return locals;
	}

	public boolean[] getLocalsObjectMap() {
		return localsObjectMap;
	}

	public void exit() {
		thread.exitFrame();
	}

	public void exit(int[] result, boolean isObject) {
		thread.exitFrame(result, isObject);
	}

	@Override
	public String toString() {
		return "StackFrame [thread=" + thread + ", method=" + method + ", instructionPointer=" + instructionPointer
				+ ", stackPointer=" + stackPointer + "]";
	}

}
