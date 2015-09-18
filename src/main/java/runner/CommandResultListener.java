package runner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CommandResultListener {

	private boolean regularStatusLocked;
	private BlockingQueue<CommandResult> queue = new ArrayBlockingQueue<>(1);

	public CommandResult getStatus() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return new CommandResult(CommandResult.EXCEPTION);
		}
	}

	public synchronized boolean lockOutRegularStatus() {
		regularStatusLocked = true;
		return queue.isEmpty();
	}

	public synchronized void setStatus(CommandResult commandResult) {
		if (!regularStatusLocked) {
			internalSetStatus(commandResult);
		}
	}

	public synchronized void setTimeoutStatus(CommandResult commandResult) {
		internalSetStatus(commandResult);
	}

	private void internalSetStatus(CommandResult commandResult) {
		queue.offer(commandResult);
	}

}
