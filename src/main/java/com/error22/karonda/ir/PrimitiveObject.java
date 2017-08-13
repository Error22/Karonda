package com.error22.karonda.ir;

public class PrimitiveObject implements IObject {
	private PrimitiveType type;
	private Object value;

	public PrimitiveObject(PrimitiveType type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public IObject getField(String name) {
		throw new UnsupportedOperationException("Unable to get field on primitive object");
	}

	@Override
	public void setField(String name, IObject value) {
		throw new UnsupportedOperationException("Unable to set field on primitive object");
	}

	@Override
	public String toString() {
		return "PrimitiveObject [type=" + type + ", value=" + value + "]";
	}

}
