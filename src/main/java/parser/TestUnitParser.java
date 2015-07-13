package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import loader.TestFileUnit;
import loader.TestUnitCommand;

public class TestUnitParser {

	private TestCommandFactory testCommandFactory;

	public TestUnitParser() {
		this(new TestCommandFactory());
	}

	TestUnitParser(TestCommandFactory testCommandFactory) {
		this.testCommandFactory = testCommandFactory;
	}

	public ParsedTestUnit parse(TestFileUnit testUnit) {
		ParsedTestUnit parsedTestUnit = new ParsedTestUnit();
		parsedTestUnit.setError(Optional.empty());
		parsedTestUnit.setName(testUnit.getName());
		parsedTestUnit.setCommands(parseCommands(testUnit, parsedTestUnit));

		return parsedTestUnit;
	}

	private List<IParsedTestCommand> parseCommands(TestFileUnit testUnit, ParsedTestUnit parsedTestUnit) {
		List<IParsedTestCommand> commands = new ArrayList<IParsedTestCommand>();
		for (TestUnitCommand command : testUnit.getCommands()) {
			IParsedTestCommand parsedCommand = testCommandFactory.parse(command);
			if (parsedCommand.getError().isPresent()) {
				parsedTestUnit.setError(Optional.of("Invalid Command(s)"));
			}
			commands.add(parsedCommand);
		}

		return commands;
	}
}
