package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;

public class SignalManager {
	private Map<String, Integer> nameMap;

	public SignalManager() {
		nameMap = new HashMap<String, Integer>();
	}

	public void registerName(String name, int id) {
		nameMap.put(name, id);
	}

	public int findSignal(String name) {
		return nameMap.containsKey(name) ? nameMap.get(name) : -1;
	}

	public long registerHandle(int signal, long newHandle) {
		// TODO: Implement
		return 1;
	}

}
