package com.error22.karonda.vm;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
	private List<KThread> threads;
	private KThread mainThread;

	public ThreadManager() {
		threads = new ArrayList<KThread>();
	}

	public void setMainThread(KThread thread) {
		this.mainThread = thread;
		threads.add(mainThread);
	}

	public KThread getMainThread() {
		return mainThread;
	}
}
