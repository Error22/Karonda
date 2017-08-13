package com.error22.karonda.ir;

import java.util.Map;

import org.objectweb.asm.Label;

import com.error22.karonda.instructions.IInstruction;

public class KMethod {
	private MethodSignature signature;
	private boolean isSynchronized, isNative;
	private int maxStack, maxLocals;
	private IInstruction[] instructions;
	private Map<Label, Integer> labelMap;

	public KMethod(MethodSignature signature, boolean isSynchronized, boolean isNative) {
		this.signature = signature;
		this.isSynchronized = isSynchronized;
		this.isNative = isNative;
	}

	public MethodSignature getSignature() {
		return signature;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public void setInstructions(IInstruction[] instructions) {
		this.instructions = instructions;
	}

	public IInstruction[] getInstructions() {
		return instructions;
	}

	public void setLabelMap(Map<Label, Integer> labelMap) {
		this.labelMap = labelMap;
	}

}
