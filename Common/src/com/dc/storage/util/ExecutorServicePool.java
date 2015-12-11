package com.dc.storage.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ExecutorServicePool {
	private static ExecutorServicePool instance;
	private ExecutorService executor = Executors
			.newCachedThreadPool(new ThreadFactory() {
				private int index = 0;

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "pool" + (index++));
				}
			});

	private ExecutorServicePool() {
	}

	public static ExecutorServicePool getInstance() {
		if (instance == null) {
			synchronized (ExecutorServicePool.class) {
				if (instance == null) {
					instance = new ExecutorServicePool();
				}
			}
		}
		return instance;
	}

	public Future<?> submit(Runnable r) {
		return executor.submit(r);
	}

	public Future<?> submit(Callable<?> c) {
		return executor.submit(c);
	}
}
