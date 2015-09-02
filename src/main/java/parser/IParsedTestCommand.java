package parser;

import java.util.Optional;

import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public interface IParsedTestCommand {

	Optional<String> getParserError();

	String execute(LineBuffer lineBuffer, TelnetLineWriter lineWriter);

	String getTimeoutStatus();

}
