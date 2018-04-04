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

	public void store(long address, byte[] data) {
		if (address < startAddress || address + data.length > startAddress + size) {
			throw new IllegalArgumentException();
		}
		long base = address - startAddress;
		for (int i = 0; i < data.length; i++) {
			this.data[(int) (base + i)] = data[i];
		}
	}

	public byte[] load(long address, int size) {
		if (address < startAddress || address + size > startAddress + this.size) {
			throw new IllegalArgumentException();
		}
		byte[] fetched = new byte[size];
		System.arraycopy(data, (int) (address - startAddress), fetched, 0, size);
		return fetched;
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
