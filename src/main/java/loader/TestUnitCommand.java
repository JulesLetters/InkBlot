package loader;

public class TestUnitCommand {

	private String unitCommand;

	public TestUnitCommand(String unitCommand) {
		this.unitCommand = unitCommand;
	}

	public String getCommandText() {
		return unitCommand;
	}

	public String getCommandType() {
		return unitCommand.split(" ")[0].toLowerCase();
	}

}
