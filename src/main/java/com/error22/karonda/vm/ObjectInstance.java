package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.error22.karonda.ir.ArrayType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;

public class ObjectInstance {
	private int id;
	private boolean isArray;
	private KClass kClass;
	private Map<FieldSignature, int[]> fields;
	private ArrayType arrayType;
	private int arraySize;
	private int[][] arrayData;

	public ObjectInstance(int id, KClass kClass) {
		isArray = false;
		this.id = id;
		this.kClass = kClass;
		fields = new HashMap<FieldSignature, int[]>();
		ArrayList<KField> classFields = new ArrayList<KField>();
		kClass.getAllFields(classFields);
		for (KField field : classFields) {
			fields.put(field.getSignature(), field.getSignature().getType().getDefaultValue());
		}
	}

	public ObjectInstance(ArrayType arrayType, int id, int arraySize) {
		isArray = true;
		this.arrayType = arrayType;
		this.id = id;
		this.arraySize = arraySize;
		this.arrayData = new int[arraySize][];
	}

	public int[] getField(FieldSignature name) {
		if (!fields.containsKey(name))
			throw new IllegalArgumentException("Field not found! " + name);
		return fields.get(name);
	}

	public void setField(FieldSignature name, int[] value) {
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

	public int[] getArrayElement(int index) {
		return arrayData[index];
	}

	public void setArrayElement(int index, int[] data) {
		arrayData[index] = data;
	}

	public KClass getKClass() {
		return kClass;
	}

	public int getId() {
		return id;
	}

}
