package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.List;

public class MemoryManager {
	private List<MemoryBlock> blocks;
	private long lastAddress;

	public MemoryManager() {
		blocks = new ArrayList<MemoryBlock>();
		lastAddress = 1;
	}

	public long allocate(long size) {
		long startAddress = lastAddress;
		lastAddress += size;
		blocks.add(new MemoryBlock(startAddress, size));
		return startAddress;
	}

}
