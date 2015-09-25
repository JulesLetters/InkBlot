package telnet;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class TelnetLineWriter {

	private WritableByteChannel outChannel;
	private ExecutorServiceFactory executorServiceFactory;
	private ExecutorService executorService;

	public TelnetLineWriter() {
		this(new TelnetClientFactory().getInstance(), new ExecutorServiceFactory());
	}

	TelnetLineWriter(TelnetClientWrapper telnetClientWrapper, ExecutorServiceFactory executorServiceFactory) {
		outChannel = telnetClientWrapper.getOutputChannel();
		this.executorServiceFactory = executorServiceFactory;
		executorService = executorServiceFactory.newSingleThreadExecutor();
	}

	public void write(String stringToWrite, IWriteCallback writeCallback) {
		executorService.submit(new WriteRunnable(stringToWrite, writeCallback, outChannel));
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
