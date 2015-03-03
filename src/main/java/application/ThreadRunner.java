package application;

public class ThreadRunner {

	public void run(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}

}
