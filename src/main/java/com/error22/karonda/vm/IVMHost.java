package com.error22.karonda.vm;

import java.util.Properties;

public interface IVMHost {

	Properties getSunProperties();

	String mapLibraryName(String name);

	String findBuiltinLib(String name);

	boolean loadLibrary(String name, boolean builtIn);

	void writeData(int fd, byte[] data, boolean append);

}
