package application;

import java.io.File;
import java.util.List;

import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import runner.ITesterCallback;
import runner.TestResult;
import runner.TestRunner;
import runner.TestRunnerFactory;
import events.FileLoadedEvent;
import events.TestCompletedEvent;

public class TestListModel implements IParserCallback, ITesterCallback {

	private ThreadRunner threadRunner;
	private TestFileParser parser;
	private IEventBus eventBus;
	private TestRunner testRunner;

	public TestListModel(IEventBus eventBus) {
		this(eventBus, new TestFileParser(), new ThreadRunner(), new TestRunnerFactory().getTestRunner());
	}

	TestListModel(IEventBus eventBus, TestFileParser parser, ThreadRunner runner, TestRunner testRunner) {
		this.eventBus = eventBus;
		this.parser = parser;
		this.threadRunner = runner;
		this.testRunner = testRunner;
	}

	public void loadFile(final File file) {
		threadRunner.run(() -> parser.parse(file, TestListModel.this), "File Parser");
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		eventBus.post(new FileLoadedEvent(parsedTestFile));
	}

	public void runAllTests(List<ParsedTestUnit> tests) {
		threadRunner.run(() -> testRunner.runTests(tests, TestListModel.this), "Test Runner");
	}

	@Override
	public void testCompleted(ParsedTestUnit parsedTestUnit, TestResult result) {
		eventBus.post(new TestCompletedEvent(parsedTestUnit, result));
	}

	public IEventBus getEventBus() {
		return eventBus;
	}

}
