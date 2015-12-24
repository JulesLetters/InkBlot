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
import runner.TestRunnerFactory;
import view.TestItem;
import events.TestListModelUpdatedEvent;

public class TestListModel implements IParserCallback {

	private ThreadRunner threadRunner;
	private TestFileParser parser;
	private IEventBus eventBus;
	private TestRunner testRunner;
	private List<ParsedTestUnit> parsedTestUnitList = Collections.emptyList();
	private List<TestItem> unitToItem = new ArrayList<>();

	public TestListModel(IEventBus eventBus) {
		this(eventBus, new TestFileParser(), new ThreadRunner(), new TestRunnerFactory().getTestRunner());
	}

	TestListModel(IEventBus eventBus, TestFileParser parser, ThreadRunner runner, TestRunner testRunner) {
		this.eventBus = eventBus;
		this.parser = parser;
		this.threadRunner = runner;
		this.testRunner = testRunner;
	}

	public List<TestItem> getTests() {
		return unitToItem;
	}

	public void loadFile(final File file) {
		threadRunner.run(() -> parser.parse(file, TestListModel.this), "File Parser");
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		parsedTestUnitList = parsedTestFile.getTests();

		for (ParsedTestUnit parsedTestUnit : parsedTestUnitList) {
			String name = parsedTestUnit.getName();
			unitToItem.add(new TestItem(name, ""));
		}

		eventBus.post(new TestListModelUpdatedEvent());
	}

	public void runAllTests() {
		threadRunner.run(() -> testRunner.runTests(parsedTestUnitList), "Test Runner");
	}

}
