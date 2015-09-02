package runner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunner {

	private ExecutorService executorService;
	private CommandRunnerTaskFactory callableFactory;

	public CommandRunner() {
		this(Executors.newSingleThreadExecutor(), new CommandRunnerTaskFactory());
	}

	CommandRunner(ExecutorService executorService, CommandRunnerTaskFactory callableFactory) {
		this.executorService = executorService;
		this.callableFactory = callableFactory;
	}

	public CommandResult runCommand(IParsedTestCommand command, LineBuffer lineBuffer, TelnetLineWriter lineWriter) {
		CommandRunnerTask callable = callableFactory.create(command, lineBuffer, lineWriter);
		Future<String> future = executorService.submit(callable);

		try {
			return new CommandResult(future.get(3000L, TimeUnit.MILLISECONDS));
		} catch (TimeoutException e) {
			return new CommandResult(command.getTimeoutStatus());
		} catch (InterruptedException | ExecutionException e) {
			return new CommandResult(CommandResult.EXCEPTION);
		}
	}
}