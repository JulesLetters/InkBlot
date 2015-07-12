package parser;

public class TestCommandFactory {

	public IParsedTestCommand parse(String command) {

		String commandArgs = command.substring(6);
		if (command.startsWith("Output")) {
			return new OutputCommand(commandArgs);
		} else if (command.startsWith("Expect")) {
			return new ExpectCommand(commandArgs);
		}
		return new InvalidCommand();
	}
}
