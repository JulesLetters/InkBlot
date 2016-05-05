package events;

import parser.ParsedTestUnit;
import runner.TestResult;

public class TestCompletedEvent {

	private ParsedTestUnit unit;
	private TestResult result;

	public TestCompletedEvent(ParsedTestUnit unit, TestResult result) {
		this.unit = unit;
		this.result = result;
	}

	public ParsedTestUnit getTestUnit() {
		return unit;
	}

	public TestResult getResult() {
		return result;
	}

}
