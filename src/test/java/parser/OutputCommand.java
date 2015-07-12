package parser;

import java.util.Optional;

import telnet.TelnetClientWrapper;

public class OutputCommand implements IParsedTestCommand {

	private String command;

	public OutputCommand(String command) {
		this.command = command;
	}

	@Override
	public Optional<String> getCommand() {
		return Optional.of(command);
	}

	@Override
	public Optional<String> getError() {
		return null;
	}

	@Override
	public void execute(TelnetClientWrapper telnet) {
	}

}
