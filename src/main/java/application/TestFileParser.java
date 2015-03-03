package application;

import java.io.File;

public class TestFileParser {

	public void parse(File file, IParserCallback callback) {
		callback.parseCompleted(new ParsedTestFile());
	}

}
