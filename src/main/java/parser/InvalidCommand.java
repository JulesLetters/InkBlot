package parser;

import java.util.Optional;

import loader.TestUnitCommand;

public class InvalidCommand implements IParsedTestCommand {

	public InvalidCommand(TestUnitCommand command) {
	}

	@Override
	public Optional<String> getError() {
		return Optional.of("Invalid Command");
	}

}
