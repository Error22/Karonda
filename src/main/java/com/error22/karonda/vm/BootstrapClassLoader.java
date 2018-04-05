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

public abstract class BootstrapClassLoader {
	protected Map<String, KClass> loadedMap;

	public BootstrapClassLoader() {
		loadedMap = new ConcurrentHashMap<String, KClass>();
	}

	public KClass getClass(String className) {
		if (loadedMap.containsKey(className))
			return loadedMap.get(className);

		KClass clazz = loadClass(className);
		if (clazz != null) {
			loadedMap.put(className, clazz);
			return clazz;
		}
		return null;
	}

	protected abstract KClass loadClass(String className);

	protected KClass loadFile(File file) throws FileNotFoundException, IOException {
		ClassReader classReader = new ClassReader(new FileInputStream(file));
		ClassConverter converter = new ClassConverter();
		classReader.accept(converter, 0);
		return converter.getkClass();
	}

}
