package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.ObjectReference;

public class ObjectInstance {
	private UUID id;
	private boolean isArray;
	private KClass kClass;
	private Map<FieldSignature, IObject> fields;
	private ArrayType arrayType;
	private int arraySize;
	private IObject[] arrayData;

	public ObjectInstance(UUID id, KClass kClass) {
		isArray = false;
		this.id = id;
		this.kClass = kClass;
		fields = new HashMap<FieldSignature, IObject>();
		ArrayList<KField> classFields = new ArrayList<KField>();
		kClass.getAllFields(classFields);
		for (KField field : classFields) {
			fields.put(field.getSignature(), field.getSignature().getType().getDefaultValue());
		}
	}

	public ObjectInstance(ArrayType arrayType, UUID id, int arraySize) {
		isArray = true;
		this.arrayType = arrayType;
		this.id = id;
		this.arraySize = arraySize;
		this.arrayData = new IObject[arraySize];
	}

	public IObject getField(FieldSignature name) {
		if (!fields.containsKey(name))
			throw new IllegalArgumentException("Field not found! " + name);
		return fields.get(name);
	}

	public void setField(FieldSignature name, IObject value) {
		if (!fields.containsKey(name))
			throw new IllegalArgumentException("Field not found! " + name);
		fields.put(name, value);
	}

	public boolean isArray() {
		return isArray;
	}
	
	public ArrayType getArrayType() {
		return arrayType;
	}

	public int getArraySize() {
		return arraySize;
	}

	public IObject getArrayElement(int index) {
		return arrayData[index];
	}

	public void setArrayElement(int index, IObject data) {
		arrayData[index] = data;
	}

	public ObjectReference makeReference() {
		return new ObjectReference(this);
	}

	public KClass getKClass(){
		return kClass;
	}
	
	public UUID getId() {
		return id;
	}

}
