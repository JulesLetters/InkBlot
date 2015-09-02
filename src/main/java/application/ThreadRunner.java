package application;

public class ThreadRunner {

	public void run(Runnable runnable, String threadName) {
		Thread thread = new Thread(runnable, threadName);
		thread.setDaemon(true);
		thread.start();
	}

}
