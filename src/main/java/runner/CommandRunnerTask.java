package runner;

import java.util.concurrent.Callable;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunnerTask implements Callable<String> {

	private IParsedTestCommand parsedTestCommand;
	private LineBuffer lineBuffer;
	private TelnetLineWriter lineWriter;

	public CommandRunnerTask(IParsedTestCommand parsedTestCommand, LineBuffer lineBuffer, TelnetLineWriter lineWriter) {
		this.parsedTestCommand = parsedTestCommand;
		this.lineBuffer = lineBuffer;
		this.lineWriter = lineWriter;
	}

	@Override
	public String call() throws Exception {
		return parsedTestCommand.execute(lineBuffer, lineWriter);
	}

}