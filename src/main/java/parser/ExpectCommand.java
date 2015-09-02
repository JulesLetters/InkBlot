package parser;

import java.util.Optional;

import loader.TestUnitCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class ExpectCommand implements IParsedTestCommand {

	public ExpectCommand(TestUnitCommand command) {
	}

	@Override
	public Optional<String> getParserError() {
		return Optional.empty();
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
