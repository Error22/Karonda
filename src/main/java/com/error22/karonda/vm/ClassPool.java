package com.error22.karonda.vm;

import com.error22.karonda.ir.KClass;

public class ClassPool {
	private BootstrapClassLoader bootstrapClassLoader;

	public ClassPool(BootstrapClassLoader bootstrapClassLoader) {
		this.bootstrapClassLoader = bootstrapClassLoader;
	}

	public KClass getClass(String className, KClass currentClass) {
		// TODO: check class loader
		// TODO: cache result
		return bootstrapResolve(className);
	}

	public KClass bootstrapResolve(String name) {
		KClass kClass = bootstrapClassLoader.getClass(name);
		kClass.bootstrapResolve(this);
		return kClass;
	}

}
