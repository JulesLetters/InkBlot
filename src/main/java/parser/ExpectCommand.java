package parser;

import java.util.Optional;

import loader.TestUnitCommand;

public class ExpectCommand implements IParsedTestCommand {

	public ExpectCommand(TestUnitCommand command) {
	}

	@Override
	public Optional<String> getError() {
		return Optional.empty();
	}

}
