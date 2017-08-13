package com.error22.karonda.vm;

import java.util.HashMap;
import java.util.Map;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.MethodSignature;

public class NativeManager {
	private Map<MethodSignature, IMethodHook> hooks;

	public NativeManager() {
		hooks = new HashMap<MethodSignature, IMethodHook>();
	}

	public void addHook(MethodSignature signature, IMethodHook hook) {
		hooks.put(signature, hook);
	}

	public void invokeNative(MethodSignature signature, KThread thread, IObject[] arguments) {
		IMethodHook hook = hooks.get(signature);
		if (hook == null)
			throw new RuntimeException("Missing hook " + signature);
		hook.invoke(thread, arguments);
	}

}
