package application;

import java.io.File;
import java.util.List;

import model.ParsedTestModel;
import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import runner.ITesterCallback;
import runner.TestResult;
import runner.TestRunner;
import runner.TestRunnerFactory;
import events.TestListModelUpdatedEvent;

public class TestListModel implements IParserCallback, ITesterCallback {

	private ThreadRunner threadRunner;
	private TestFileParser parser;
	private IEventBus eventBus;
	private TestRunner testRunner;

	private ParsedTestModel parsedTestModel;

	public TestListModel(IEventBus eventBus) {
		this(new ParsedTestModel(), eventBus, new TestFileParser(), new ThreadRunner(), new TestRunnerFactory()
				.getTestRunner());
	}

	TestListModel(ParsedTestModel parsedTestModel, IEventBus eventBus, TestFileParser parser, ThreadRunner runner,
			TestRunner testRunner) {
		this.eventBus = eventBus;
		this.parser = parser;
		this.threadRunner = runner;
		this.testRunner = testRunner;
		this.parsedTestModel = parsedTestModel;
	}

	public List<ParsedTestUnit> getTests() {
		return parsedTestModel.getTests();
	}

	public void loadFile(final File file) {
		threadRunner.run(() -> parser.parse(file, TestListModel.this), "File Parser");
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		parsedTestModel.addFile(parsedTestFile);
		eventBus.post(new TestListModelUpdatedEvent());
	}

	public void runAllTests() {
		threadRunner.run(() -> testRunner.runTests(parsedTestModel.getTests(), TestListModel.this), "Test Runner");
	}

	@Override
	public void testCompleted(ParsedTestUnit parsedTestUnit, TestResult result) {
		parsedTestModel.setUnitStatus(parsedTestUnit, result);
	}

}
