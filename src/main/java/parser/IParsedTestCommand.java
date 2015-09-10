package parser;

import java.util.Optional;

import runner.CommandResultListener;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public interface IParsedTestCommand {

	Optional<String> getParserError();

	void execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener);

	void timeout(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener);

	void stop(LineBuffer lineBuffer, TelnetLineWriter lineWriter, CommandResultListener listener);

}
