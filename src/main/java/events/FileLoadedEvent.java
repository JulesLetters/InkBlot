package events;

import parser.ParsedTestFile;

public class FileLoadedEvent {

	private ParsedTestFile parsedTestFile;

	public FileLoadedEvent(ParsedTestFile parsedTestFile) {
		this.parsedTestFile = parsedTestFile;
	}

	public ParsedTestFile getParsedTestFile() {
		return parsedTestFile;
	}

}
