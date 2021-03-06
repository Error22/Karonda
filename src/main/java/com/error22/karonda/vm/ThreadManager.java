package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
	private List<KThread> threads;
	private KThread mainThread;

	public ThreadManager() {
		threads = new ArrayList<KThread>();
	}

	public void addThread(KThread thread) {
		threads.add(thread);
	}

	public KThread getLockOwner(int id) {
		for (KThread thread : threads) {
			if (thread.isLocked(id)) {
				return thread;
			}
		}
		return null;
	}

	public KThread getThreadByRef(int id) {
		for (KThread thread : threads) {
			if (thread.getThreadObjRef() == id) {
				return thread;
			}
		}
		return null;
	}

	public void notifyAll(int id) {
		// TODO: Implement
	}

	public void setMainThread(KThread thread) {
		this.mainThread = thread;
		threads.add(mainThread);
	}

	public KThread getMainThread() {
		return mainThread;
	}

}
