package com.error22.karonda.ir;

import java.util.Map;

import org.objectweb.asm.Label;

import com.error22.karonda.instructions.IInstruction;

public class KMethod {
	private KClass kClass;
	private MethodSignature signature;
	private boolean isAbstract, isSynchronized, isNative;
	private int maxStack, maxLocals;
	private IInstruction[] instructions;
	private Map<Label, Integer> labelMap;
	private int index;

	public KMethod(KClass kClass, MethodSignature signature, boolean isAbstract, boolean isSynchronized,
			boolean isNative) {
		this.kClass = kClass;
		this.signature = signature;
		this.isAbstract = isAbstract;
		this.isSynchronized = isSynchronized;
		this.isNative = isNative;
	}

	public KClass getKClass() {
		return kClass;
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

	public Map<Label, Integer> getLabelMap() {
		return labelMap;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isNative() {
		return isNative;
	}

	@Override
	public String toString() {
		return "KMethod [kClass=" + kClass + ", signature=" + signature + ", isAbstract=" + isAbstract
				+ ", isSynchronized=" + isSynchronized + ", isNative=" + isNative + ", maxStack=" + maxStack
				+ ", maxLocals=" + maxLocals + "]";
	}

}
