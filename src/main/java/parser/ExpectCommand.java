package parser;

import java.util.Optional;

import loader.TestUnitCommand;
import runner.CommandResult;
import runner.CommandResultListener;
import telnet.ITextChangeListener;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class ExpectCommand implements IParsedTestCommand {

	private String commandArgument;
	private ITextChangeListener textChangeListener;

	public ExpectCommand(TestUnitCommand command) {
		commandArgument = command.getCommandArgument();
	}

	@Override
	public Optional<String> getParserError() {
		return Optional.empty();
	}

	@Override
	public void execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		textChangeListener = () -> {
			String patternString = "(.*\n)?" + commandArgument + "(\n.*)?";
			if (lineBuffer.getText().matches(patternString)) {
				listener.setStatus(new CommandResult(CommandResult.SUCCESS));
			}
		};
		lineBuffer.addListener(textChangeListener);

		textChangeListener.textChanged();
	}

	@Override
	public void timeout(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		listener.setTimeoutStatus(new CommandResult(CommandResult.FAILURE));
	}

	@Override
	public void stop(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		lineBuffer.removeListener(textChangeListener);
	}

}
