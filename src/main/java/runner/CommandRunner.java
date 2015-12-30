package runner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import application.ExecutorServiceFactory;
import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunner {

	static final String THREAD_NAME = "Command Runner";
	private static final long COMMAND_TIMEOUT_MILLIS = 3000;
	private ScheduledExecutorService scheduledExecutorService;
	private CommandRunnerListenerFactory commandRunnerListenerFactory;

	public CommandRunner() {
		this(new ExecutorServiceFactory(), new CommandRunnerListenerFactory());
	}

	CommandRunner(ExecutorServiceFactory executorServiceFactory,
			CommandRunnerListenerFactory commandRunnerListenerFactory) {
		this.scheduledExecutorService = executorServiceFactory.newSingleThreadScheduledExecutor(THREAD_NAME);
		this.commandRunnerListenerFactory = commandRunnerListenerFactory;
	}

	public CommandResult runCommand(IParsedTestCommand command, LineBuffer lineBuffer, TelnetLineWriter lineWriter) {
		CommandResultListener listener = commandRunnerListenerFactory.create();
		command.execute(lineBuffer, lineWriter, listener);

		ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> {
			if (listener.ignoreRegularStatus()) {
				command.timeout(lineBuffer, lineWriter, listener);
			}
		}, COMMAND_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

		CommandResult commandResult = listener.getStatus();
		schedule.cancel(false);
		command.stop(lineBuffer, lineWriter, listener);
		return commandResult;
	}
}