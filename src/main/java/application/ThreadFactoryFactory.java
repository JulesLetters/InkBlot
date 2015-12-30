package application;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryFactory {

	public ThreadFactory create(String threadName) {
		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(runnable, threadName);
				thread.setDaemon(true);
				return thread;
			}
		};
	}

}
