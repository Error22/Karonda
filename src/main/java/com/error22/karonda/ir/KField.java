package com.error22.karonda.ir;

public class KField {
	private FieldSignature signature;
	private boolean isStatic;
	private int index;

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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSignature(FieldSignature signature) {
		this.signature = signature;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

}
