package application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import application.ThreadFactoryFactory;

public class ExecutorServiceFactory {

	private ThreadFactoryFactory threadFactoryFactory = new ThreadFactoryFactory();

	public ExecutorService newSingleThreadExecutor(String threadName) {
		ThreadFactory factory = threadFactoryFactory.create(threadName);
		return Executors.newSingleThreadExecutor(factory);
	}

	public ScheduledExecutorService newSingleThreadScheduledExecutor(String threadName) {
		ThreadFactory factory = threadFactoryFactory.create(threadName);
		return Executors.newSingleThreadScheduledExecutor(factory);
	}

}
