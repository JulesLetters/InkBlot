package runner;

public class CommandResult {

	public static final String SUCCESS = "CommandSuccess";
	public static final String FAILURE = "CommandFailure";
	public static final String EXCEPTION = "CommandException";
	private String status;

	public CommandResult(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
