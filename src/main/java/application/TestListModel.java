package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import runner.TestRunner;
import events.TestListModelUpdatedEvent;

public class TestListModel implements IParserCallback {

	private ThreadRunner threadRunner;
	private TestFileParser parser;
	private List<String> testNames = Collections.emptyList();
	private IEventBus eventBus;
	private TestRunner testRunner;

	public TestListModel(IEventBus eventBus) {
		this(eventBus, new TestFileParser(), new ThreadRunner(), new TestRunner());
	}

	TestListModel(IEventBus eventBus, TestFileParser parser, ThreadRunner runner, TestRunner testRunner) {
		this.eventBus = eventBus;
		this.parser = parser;
		this.threadRunner = runner;
		this.testRunner = testRunner;

	}

	public List<String> getTestNames() {
		return testNames;
	}

	public void loadFile(final File file) {
		threadRunner.run(() -> parser.parse(file, TestListModel.this));
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		List<String> names = new ArrayList<>();
		List<ParsedTestUnit> parsedTestUnitList = parsedTestFile.getTests();
		testRunner.setParsedUnits(parsedTestUnitList);
		for (ParsedTestUnit testUnit : parsedTestUnitList) {
			names.add(testUnit.getName());
		}
		this.testNames = names;
		eventBus.post(new TestListModelUpdatedEvent());
	}

	public void runAllTests() {
		threadRunner.run(() -> testRunner.runTests());
	}

}
