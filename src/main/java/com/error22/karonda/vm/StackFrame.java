package com.error22.karonda.vm;

import java.util.Map;

import org.objectweb.asm.Label;

import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KMethod;

public class StackFrame {
	private KThread thread;
	private KMethod method;
	private IInstruction[] instructions;
	private Map<Label, Integer> labelMap;
	private int instructionPointer, stackPointer;
	private IObject[] locals, stack;

	public StackFrame(KThread thread, KMethod method) {
		this.thread = thread;
		this.method = method;
		instructions = method.getInstructions();
		labelMap = method.getLabelMap();
	}

	public void init(IObject[] arguments) {
		locals = new IObject[method.getMaxLocals()];
		stack = new IObject[method.getMaxStack()];
		System.arraycopy(arguments, 0, locals, 0, arguments.length);
	}

	public void step() {
		IInstruction instruction = instructions[instructionPointer];
		instructionPointer++;
		instruction.execute(this);
	}

	public void jump(Label label) {
		instructionPointer = labelMap.get(label);
	}

	public void push(IObject object) {
		System.out.println("push " + object);
		stack[stackPointer] = object;
		stackPointer++;
	}

	public IObject pop() {
		stackPointer--;
		return stack[stackPointer];
	}

	public void setLocal(int index, IObject object) {
		locals[index] = object;
	}

	public IObject getLocal(int index) {
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

	public void setInstructionPointer(int instructionPointer) {
		this.instructionPointer = instructionPointer;
	}

	public void exit(IObject result) {
		thread.exitFrame(result);
	}

}
