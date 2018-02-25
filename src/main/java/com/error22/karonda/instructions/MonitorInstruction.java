package com.error22.karonda.instructions;

import com.error22.karonda.vm.KThread;
import com.error22.karonda.vm.StackFrame;
import com.error22.karonda.vm.ThreadManager;

public class MonitorInstruction implements IInstruction {
	public static enum MonitorOperation {
		Enter,
		Exit
	}

	private MonitorOperation op;

	public MonitorInstruction(MonitorOperation op) {
		this.op = op;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		KThread thread = stackFrame.getThread();
		ThreadManager manager = thread.getThreadManager();

		int ref = stackFrame.pop();
		KThread owner = manager.getLockOwner(ref);

		switch (op) {
		case Enter:
			if (owner != thread && owner != null) {
				stackFrame.moveInstructionPointer(-1);
				stackFrame.push(ref, true);
				return;
			}
			thread.lock(ref);
			break;
		case Exit:
			if (owner != thread) {
				throw new IllegalStateException("Current thread does not own object lock");
			}
			thread.unlock(ref);
			break;
		default:
			break;
		}
	}

}
