package com.error22.karonda.ir;

public class KField {
	private FieldSignature signature;
	private boolean isStatic;

	public KField(FieldSignature signature, boolean isStatic) {
		this.signature = signature;
		this.isStatic = isStatic;
	}

	public FieldSignature getSignature() {
		return signature;
	}

	public boolean isStatic() {
		return isStatic;
	}

}
