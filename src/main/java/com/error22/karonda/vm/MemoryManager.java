package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.Iterator;
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

	public void free(long address) {
		Iterator<MemoryBlock> iterator = blocks.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getStartAddress() == address) {
				iterator.remove();
				return;
			}
		}
		throw new IllegalArgumentException();
	}

	public void store(long address, long value) {
		byte[] byteData = new byte[8];
		byteData[0] = (byte) (value >> 56);
		byteData[1] = (byte) (value >> 48);
		byteData[2] = (byte) (value >> 40);
		byteData[3] = (byte) (value >> 32);
		byteData[4] = (byte) (value >> 24);
		byteData[5] = (byte) (value >> 16);
		byteData[6] = (byte) (value >> 8);
		byteData[7] = (byte) (value);
		store(address, byteData);
	}

	public void store(long address, int[] data) {
		byte[] byteData = new byte[data.length * 4];
		for (int i = 0; i < data.length; i++) {
			int value = data[i];
			int base = i * 4;
			byteData[base] = (byte) (value >> 24);
			byteData[base + 1] = (byte) (value >> 16);
			byteData[base + 2] = (byte) (value >> 8);
			byteData[base + 3] = (byte) (value);
		}
		store(address, byteData);
	}

	public void store(long address, byte[] data) {
		for (MemoryBlock block : blocks) {
			if (block.isContained(address)) {
				block.store(address, data);
			}
		}
	}

	public byte[] load(long address, int size) {
		for (MemoryBlock block : blocks) {
			if (block.isContained(address)) {
				return block.load(address, size);
			}
		}
		throw new IllegalArgumentException();
	}

}
