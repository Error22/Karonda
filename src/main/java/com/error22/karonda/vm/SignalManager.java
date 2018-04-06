package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;

public class SignalManager {
	private Map<String, Integer> nameMap;

	public SignalManager() {
		nameMap = new HashMap<String, Integer>();
	}

	public void registerDefaults() {
		registerName("HUP", 1);
		registerName("INT", 2);
		registerName("QUIT", 3);
		registerName("ILL", 4);
		registerName("TRAP", 5);
		registerName("ABRT", 6);
		registerName("BUS", 7);
		registerName("FPE", 8);
		registerName("SEFV", 11);
		registerName("TERM", 15);
		registerName("CHLD", 17);
		registerName("RECONFIG", 58);
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
