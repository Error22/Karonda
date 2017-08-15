package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.ObjectReference;

public class ObjectInstance {
	private UUID id;
	private KClass kClass;
	private Map<FieldSignature, IObject> fields;

	public ObjectInstance(UUID id, KClass kClass) {
		this.id = id;
		this.kClass = kClass;
		fields = new HashMap<FieldSignature, IObject>();
	}

	public IObject getField(FieldSignature name) {
		return fields.get(name);
	}

	public void setField(FieldSignature name, IObject value) {
		fields.put(name, value);
	}

	public ObjectReference makeReference() {
		return new ObjectReference(kClass, this);
	}

}
