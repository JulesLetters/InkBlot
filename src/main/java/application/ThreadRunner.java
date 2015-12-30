package application;

public class ThreadRunner {

	private ThreadFactoryFactory threadFactoryFactory;

	public ThreadRunner() {
		this(new ThreadFactoryFactory());
	}

	ThreadRunner(ThreadFactoryFactory threadFactoryFactory) {
		this.threadFactoryFactory = threadFactoryFactory;
	}

	public void run(Runnable runnable, String threadName) {
		Thread thread = threadFactoryFactory.create(threadName).newThread(runnable);
		thread.start();
	}

}
