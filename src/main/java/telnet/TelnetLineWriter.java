package telnet;

import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class TelnetLineWriter {

	private ExecutorServiceFactory executorServiceFactory;
	private ExecutorService executorService;
	private TelnetClientWrapper telnetClientWrapper;

	public TelnetLineWriter() {
		this(new TelnetClientFactory().getInstance(), new ExecutorServiceFactory());
	}

	TelnetLineWriter(TelnetClientWrapper telnetClientWrapper, ExecutorServiceFactory executorServiceFactory) {
		this.telnetClientWrapper = telnetClientWrapper;
		this.executorServiceFactory = executorServiceFactory;
		executorService = executorServiceFactory.newSingleThreadExecutor();
	}

	public void write(String stringToWrite, IWriteCallback writeCallback) {
		executorService.submit(new WriteRunnable(stringToWrite, writeCallback, telnetClientWrapper));
	}

	public void interrupt() {
		List<Runnable> runnableList = executorService.shutdownNow();
		for (Runnable runnable : runnableList) {
			WriteRunnable writeRunnable = (WriteRunnable) runnable;
			writeRunnable.getCallback().call(Optional.of(new ClosedChannelException()));
		}

		executorService = executorServiceFactory.newSingleThreadExecutor();
	}
}
