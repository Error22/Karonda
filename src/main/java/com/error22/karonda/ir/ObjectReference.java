package com.error22.karonda.ir;

import com.error22.karonda.vm.ObjectInstance;

public class ObjectReference implements IObject {
	private KClass kClass;
	private ObjectInstance instance;

	public ObjectReference(KClass kClass, ObjectInstance instance) {
		this.kClass = kClass;
		this.instance = instance;
	}

	public ObjectInstance getInstance() {
		return instance;
	}

	public KClass getKClass() {
		return kClass;
	}

}
