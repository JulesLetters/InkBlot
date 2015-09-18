package runner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunner {

	private static final long COMMAND_TIMEOUT_MILLIS = 3000;
	private ScheduledExecutorService scheduledExecutorService;
	private CommandRunnerListenerFactory commandRunnerListenerFactory;

	public CommandRunner() {
		this(Executors.newSingleThreadScheduledExecutor(), new CommandRunnerListenerFactory());
	}

	CommandRunner(ScheduledExecutorService scheduledExecutorService,
			CommandRunnerListenerFactory commandRunnerListenerFactory) {
		this.scheduledExecutorService = scheduledExecutorService;
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