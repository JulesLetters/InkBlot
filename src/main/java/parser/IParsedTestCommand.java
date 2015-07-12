package parser;

import java.util.Optional;

import telnet.TelnetClientWrapper;

public interface IParsedTestCommand {

	Optional<String> getCommand();

	Optional<String> getError();

	void execute(TelnetClientWrapper telnet);

}
