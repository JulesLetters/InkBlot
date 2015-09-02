package parser;

import java.util.Optional;

import loader.TestUnitCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class InvalidCommand implements IParsedTestCommand {

	public InvalidCommand(TestUnitCommand command) {
	}

	@Override
	public Optional<String> getError() {
		return Optional.of("Invalid Command");
	}

	@Override
	public String execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimeoutStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
