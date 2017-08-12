package com.error22.karonda.ir;

public class KMethod {
	private MethodSignature signature;
	private boolean isSynchronized, isNative;

	public KMethod(MethodSignature signature, boolean isSynchronized, boolean isNative) {
		this.signature = signature;
		this.isSynchronized = isSynchronized;
		this.isNative = isNative;
	}

	public MethodSignature getSignature() {
		return signature;
	}

}
