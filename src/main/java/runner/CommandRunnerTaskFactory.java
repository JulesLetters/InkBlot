package runner;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunnerTaskFactory {

	public CommandRunnerTask create(IParsedTestCommand parsedTestCommand, LineBuffer lineBuffer,
			TelnetLineWriter lineWriter) {
		return new CommandRunnerTask(parsedTestCommand, lineBuffer, lineWriter);
	}

}
