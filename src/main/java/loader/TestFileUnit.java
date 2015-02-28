package loader;

import java.util.List;

public class TestFileUnit {

	private String name;

	 private List<TestUnitCommand> commands;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	 public List<TestUnitCommand> getCommands() {
	 return commands;
	 }
	
	 public void setCommands(List<TestUnitCommand> commands) {
	 this.commands = commands;
	 }

}
