package com.error22.karonda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
	private Map<String, IType> types;

	public Context() {
		types = new ConcurrentHashMap<String, IType>();
	}

}
