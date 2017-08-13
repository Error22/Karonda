package com.error22.karonda.vm;

import com.error22.karonda.ir.IObject;

public interface IMethodHook {

	void invoke(KThread thread, IObject[] arguments);

}
