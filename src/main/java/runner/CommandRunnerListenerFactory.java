package runner;

public class CommandRunnerListenerFactory {

	public CommandResultListener create() {
		return new CommandResultListener();
	}
}
