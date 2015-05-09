package parser;

import java.util.Optional;

public class TestCommandFactory {

	public IParsedTestCommand parse(String command) {
		IParsedTestCommand filler = () -> Optional.empty();
		return filler;
	}

}
