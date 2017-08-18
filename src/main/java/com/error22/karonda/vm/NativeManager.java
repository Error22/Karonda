package com.error22.karonda.vm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.error22.karonda.ir.IObject;
import com.error22.karonda.ir.IType;
import com.error22.karonda.ir.MethodSignature;

public class NativeManager {
	private Map<MethodSignature, IMethodHook> boundHooks;
	private Map<Integer, IMethodHook> unboundHooks;

	public NativeManager() {
		boundHooks = new HashMap<MethodSignature, IMethodHook>();
		unboundHooks = new HashMap<Integer, IMethodHook>();
	}

	public void addBoundHook(MethodSignature signature, IMethodHook hook) {
		boundHooks.put(signature, hook);
	}

	public void addUnboundHook(IMethodHook hook, String name, IType returnType, IType... arguments) {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(arguments);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		unboundHooks.put(result, hook);
	}

	public void invokeNative(MethodSignature signature, KThread thread, StackFrame stackFrame, IObject[] arguments) {
		IMethodHook hook = boundHooks.get(signature);
		if (hook == null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(signature.getArguments());
			result = prime * result + ((signature.getName() == null) ? 0 : signature.getName().hashCode());
			result = prime * result + ((signature.getReturnType() == null) ? 0 : signature.getReturnType().hashCode());
			hook = unboundHooks.get(result);
		}

		if (hook == null)
			throw new RuntimeException("Missing hook " + signature);
		hook.invoke(thread, stackFrame, arguments);
	}

}
