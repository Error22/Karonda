package com.error22.karonda.ir;

import com.error22.karonda.vm.ObjectInstance;

public class ObjectReference implements IObject {
	private ObjectInstance instance;

	public ObjectReference(ObjectInstance instance) {
		this.instance = instance;
	}

	public ObjectInstance getInstance() {
		return instance;
	}

	public KClass getKClass() {
		return instance.getKClass();
	}

	@Override
	public IObject duplicate() {
		return new ObjectReference(instance);
	}

	@Override
	public IType getType() {
		return instance.isArray() ? instance.getArrayType() : new ObjectType(instance.getKClass().getName());
	}

}
