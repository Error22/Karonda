package com.error22.karonda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.error22.karonda.ir.IType;

public class Context {
	private Map<String, IType> types;

	public Context() {
		types = new ConcurrentHashMap<String, IType>();
	}

}
