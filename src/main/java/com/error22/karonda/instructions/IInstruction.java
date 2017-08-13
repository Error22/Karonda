package com.error22.karonda.instructions;

import com.error22.karonda.vm.StackFrame;

public interface IInstruction {

	void execute(StackFrame stackFrame);

}
