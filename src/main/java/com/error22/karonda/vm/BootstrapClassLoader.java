package com.error22.karonda.vm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassReader;

import com.error22.karonda.converter.ClassConverter;
import com.error22.karonda.ir.KClass;

public class BootstrapClassLoader {
	private File searchDir;
	private Map<String, KClass> loadedMap;

	public BootstrapClassLoader(File searchDir) {
		this.searchDir = searchDir;
		loadedMap = new ConcurrentHashMap<String, KClass>();
	}

	public KClass getClass(String className) {
		System.out.println("BootstrapClassLoader: get " + className);
		if (loadedMap.containsKey(className))
			return loadedMap.get(className);

		try {
			KClass kClass = loadFile(new File(searchDir, className + ".class"));
			loadedMap.put(className, kClass);
			return kClass;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private KClass loadFile(File file) throws FileNotFoundException, IOException {
		ClassReader classReader = new ClassReader(new FileInputStream(file));
		ClassConverter converter = new ClassConverter();
		classReader.accept(converter, 0);
		return converter.getkClass();
	}

}
