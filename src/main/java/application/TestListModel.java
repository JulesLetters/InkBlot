package application;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import runner.TestRunner;
import runner.TestRunnerFactory;
import events.TestListModelUpdatedEvent;

public class TestListModel implements IParserCallback {

	private ThreadRunner threadRunner;
	private TestFileParser parser;
	private IEventBus eventBus;
	private TestRunner testRunner;
	private List<ParsedTestUnit> parsedTestUnitList = Collections.emptyList();

	public TestListModel(IEventBus eventBus) {
		this(eventBus, new TestFileParser(), new ThreadRunner(), new TestRunnerFactory().getTestRunner());
	}

	TestListModel(IEventBus eventBus, TestFileParser parser, ThreadRunner runner, TestRunner testRunner) {
		this.eventBus = eventBus;
		this.parser = parser;
		this.threadRunner = runner;
		this.testRunner = testRunner;
	}

	public List<String> getTestNames() {
		return parsedTestUnitList.stream().map((testUnit) -> {
			return testUnit.getName();
		}).collect(Collectors.toList());
	}

	public void loadFile(final File file) {
		threadRunner.run(() -> parser.parse(file, TestListModel.this), "File Parser");
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		parsedTestUnitList = parsedTestFile.getTests();
		eventBus.post(new TestListModelUpdatedEvent());
	}

	public void runAllTests() {
		threadRunner.run(() -> testRunner.runTests(parsedTestUnitList), "Test Runner");
	}

}
