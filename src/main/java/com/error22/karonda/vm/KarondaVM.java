package com.error22.karonda.vm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KarondaVM {
	private List<KThread> threads;

	public KarondaVM() {
		threads = new CopyOnWriteArrayList<KThread>();
	}

}
