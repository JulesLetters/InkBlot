package telnet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactory {

	public ExecutorService newSingleThreadExecutor() {
		return Executors.newSingleThreadExecutor();
	}

}
