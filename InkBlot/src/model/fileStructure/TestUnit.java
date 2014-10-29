package model.fileStructure;

import java.util.List;

public class TestUnit {

	private String name;
	private List<TestCommandText> commands;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestCommandText> getCommands() {
		return commands;
	}

	public void setCommands(List<TestCommandText> commands) {
		this.commands = commands;
	}

}
