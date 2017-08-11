package com.error22.karonda.types;

public class PrimitiveObject<T> implements IObject {
	private T value;

	public PrimitiveObject(T value) {
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

}
