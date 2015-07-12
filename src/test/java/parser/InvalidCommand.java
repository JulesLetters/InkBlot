package parser;

import java.util.Optional;

import telnet.TelnetClientWrapper;

public class InvalidCommand implements IParsedTestCommand {

	@Override
	public Optional<String> getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(TelnetClientWrapper telnet) {
		// TODO Auto-generated method stub

	}

}
