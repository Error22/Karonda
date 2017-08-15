package com.error22.karonda.ir;

public class PrimitiveObject implements IObject {
	private PrimitiveType type;
	private Object value;

	public PrimitiveObject(PrimitiveType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public PrimitiveType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "PrimitiveObject [type=" + type + ", value=" + value + "]";
	}

}
