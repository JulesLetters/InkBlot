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

	private TestFileParser parser;
	private TestRunner testRunner;
	private ThreadRunner threadRunner;
	private IEventBus eventBus;
	private List<String> testNames = Collections.emptyList();

	public TestListModel(IEventBus eventBus) {
		this(new TestFileParser(), new TestRunner(), new ThreadRunner(), eventBus);
	}

	protected TestListModel(TestFileParser parser, TestRunner testRunner, ThreadRunner threadRunner, IEventBus eventBus) {
		this.parser = parser;
		this.testRunner = testRunner;
		this.threadRunner = threadRunner;
		this.eventBus = eventBus;
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
		testRunner.addParsedUnits(parsedTestUnitList);
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
