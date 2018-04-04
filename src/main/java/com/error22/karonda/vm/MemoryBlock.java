package com.error22.karonda.vm;

public class MemoryBlock {
	private long startAddress;
	private long size;
	private byte[] data;

	public MemoryBlock(long startAddress, long size) {
		this.startAddress = startAddress;
		this.size = size;
		this.data = new byte[(int) size];
	}

	public boolean isContained(long address) {
		return address >= startAddress && address < startAddress + size;
	}

	public long getStartAddress() {
		return startAddress;
	}

	public long getSize() {
		return size;
	}

	public byte[] getData() {
		return data;
	}

}
