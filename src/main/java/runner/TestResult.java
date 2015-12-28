package runner;

public class TestResult {

	public static final String SUCCESS = "TestSuccess";
	public static final String FAILURE = "TestFailure";
	public static final String EXCEPTION = "TestException";
	public static final String LOADED = "TestLoaded";
	private String status;

	public TestResult(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
