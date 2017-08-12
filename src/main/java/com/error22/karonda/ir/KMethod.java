package com.error22.karonda.ir;

public class KMethod {
	private MethodSignature signature;
	private boolean isSynchronized, isNative;
	private int maxStack, maxLocals;

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

	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}

}
