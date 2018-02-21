package com.error22.karonda.vm;

public interface IMethodHook {

	void invoke(KThread thread, StackFrame stackFrame, int[] arguments);

}
