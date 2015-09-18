package parser;

import java.util.Optional;

import loader.TestUnitCommand;
import runner.CommandResult;
import runner.CommandResultListener;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class OutputCommand implements IParsedTestCommand {

	private String commandArgument;

	public OutputCommand(TestUnitCommand command) {
		commandArgument = command.getCommandArgument();
	}

	@Override
	public Optional<String> getParserError() {
		return Optional.empty();
	}

	@Override
	public void execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		lineWriter.write(commandArgument, (maybeException) -> {
			String result = maybeException.isPresent() ? CommandResult.EXCEPTION : CommandResult.SUCCESS;
			listener.setStatus(new CommandResult(result));
		});
	}

	@Override
	public void timeout(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		lineWriter.interrupt();
		listener.setTimeoutStatus(new CommandResult(CommandResult.EXCEPTION));
	}

	@Override
	public void stop(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
	}

}
