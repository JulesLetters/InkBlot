package parser;

import java.util.Optional;

import telnet.TelnetClientWrapper;

public class ExpectCommand implements IParsedTestCommand {

	private String command;

	public ExpectCommand(String commandArgs) {
		this.command = commandArgs;
	}

	@Override
	public Optional<String> getCommand() {
		return Optional.of(value);
	}

	@Override
	public Optional<String> getError() {
		return null;
	}

	@Override
	public void execute(TelnetClientWrapper telnet) {
	}

}
