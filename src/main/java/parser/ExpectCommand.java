package parser;

import java.util.Optional;

import loader.TestUnitCommand;
import runner.CommandResultListener;
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
	public void execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeout(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener) {
		// TODO Auto-generated method stub

	}

}
