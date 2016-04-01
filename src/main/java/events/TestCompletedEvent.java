package events;

import parser.ParsedTestUnit;

public class TestCompletedEvent {

	private ParsedTestUnit unit;

	public TestCompletedEvent(ParsedTestUnit unit) {
		this.unit = unit;
	}

	public ParsedTestUnit getTestUnit() {
		return unit;
	}

}
