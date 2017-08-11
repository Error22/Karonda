package com.error22.karonda.types;

import java.util.HashMap;
import java.util.Map;

public class KObject implements IObject {
	private Map<String, IObject> fields;

	public KObject() {
		fields = new HashMap<String, IObject>();
	}

	@Override
	public IObject getField(String name) {
		return fields.get(name);
	}

	@Override
	public void setField(String name, IObject value) {
		fields.put(name, value);
	}

}
