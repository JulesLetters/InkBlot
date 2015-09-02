package runner;

import parser.IParsedTestCommand;
import parser.ParsedTestUnit;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class SingleTestRunner {

	private CommandRunner commandRunner;

	public SingleTestRunner() {
		this(new CommandRunner());
	}

	SingleTestRunner(CommandRunner commandRunner) {
		this.commandRunner = commandRunner;
	}

	public TestResult runTest(LineBuffer lineBuffer, TelnetLineWriter lineWriter, ParsedTestUnit parsedTestUnit) {
		for (IParsedTestCommand command : parsedTestUnit.getCommands()) {
			CommandResult commandResult = commandRunner.runCommand(command, lineBuffer, lineWriter);
			if (CommandResult.EXCEPTION.equals(commandResult.getStatus())) {
				return new TestResult(TestResult.EXCEPTION);
			}
			if (CommandResult.FAILURE.equals(commandResult.getStatus())) {
				return new TestResult(TestResult.FAILURE);
			}
		}
		return new TestResult(TestResult.SUCCESS);
	}

}
