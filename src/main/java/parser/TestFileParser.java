package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import loader.TestFile;
import loader.TestFileLoader;
import loader.TestFileUnit;

import org.yaml.snakeyaml.error.YAMLException;

public class TestFileParser {

	private TestFileLoader testFileLoader;
	private TestUnitParser testUnitParser;

	public TestFileParser() {
		this(new TestFileLoader(), new TestUnitParser());
	}

	TestFileParser(TestFileLoader testFileLoader, TestUnitParser testUnitParser) {
		this.testFileLoader = testFileLoader;
		this.testUnitParser = testUnitParser;
	}

	public void parse(File file, IParserCallback callback) {
		ParsedTestFile parsedTestFile = new ParsedTestFile();
		parsedTestFile.setFile(file);
		parsedTestFile.setTests(Collections.emptyList());

		try {
			TestFile testFile = testFileLoader.loadTestfile(file);
			parsedTestFile.setTests(parseTests(testFile));
			parsedTestFile.setError(Optional.empty());
		} catch (FileNotFoundException e) {
			parsedTestFile.setError(Optional.of("File Not Found: " + file.getAbsolutePath()));
		} catch (YAMLException e) {
			parsedTestFile.setError(Optional.of("Yaml Loading Failed:\r\n" + e.getMessage()));
		}

		callback.parseCompleted(parsedTestFile);
	}

	private List<ParsedTestUnit> parseTests(TestFile testFile) {
		List<ParsedTestUnit> parsedTests = new ArrayList<ParsedTestUnit>();
		List<TestFileUnit> tests = testFile.getTests();
		if (tests != null) {
			for (TestFileUnit testFileUnit : tests) {
				parsedTests.add(testUnitParser.parse(testFileUnit));
			}
		}
		return parsedTests;

	}

}
