package parser;

import java.util.Optional;

import loader.TestUnitCommand;

public class OutputCommand implements IParsedTestCommand {

	public OutputCommand(TestUnitCommand command) {
	}

	@Override
	public Optional<String> getError() {
		return Optional.empty();
	}

}
