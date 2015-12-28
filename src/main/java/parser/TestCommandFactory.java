package parser;

import loader.TestUnitCommand;

public class TestCommandFactory {

	public IParsedTestCommand parse(TestUnitCommand command) {
		if ("output".equals(command.getCommandType())) {
			return new OutputCommand(command);
		} else if ("expect".equals(command.getCommandType())) {
			return new ExpectCommand(command);
		}
		return new InvalidCommand(command);
	}

}
