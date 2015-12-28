package parser;

import java.util.List;
import java.util.Optional;

public class ParsedTestUnit {

	private String name;
	private List<IParsedTestCommand> commands;
	private Optional<String> error;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IParsedTestCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<IParsedTestCommand> commands) {
		this.commands = commands;
	}

	public Optional<String> getError() {
		return error;
	}

	public void setError(Optional<String> error) {
		this.error = error;
	}

}
