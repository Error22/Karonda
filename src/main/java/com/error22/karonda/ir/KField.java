package com.error22.karonda.ir;

public class KField {
	private FieldSignature signature;
	private int flags;
	private boolean isStatic;
	private int index;

	public KField(FieldSignature signature, int flags, boolean isStatic) {
		this.signature = signature;
		this.flags = flags;
		this.isStatic = isStatic;
	}

	public FieldSignature getSignature() {
		return signature;
	}

	public int getFlags() {
		return flags;
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

	@Override
	public String toString() {
		return "KField [signature=" + signature + ", isStatic=" + isStatic + ", index=" + index + "]";
	}

}
