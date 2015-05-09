package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import events.TestListModelUpdatedEvent;

public class TestListModel implements IParserCallback {

	private ThreadRunner runner;
	private TestFileParser parser;
	private List<String> testNames = Collections.emptyList();
	private IEventBus eventBus;

	public TestListModel(IEventBus eventBus) {
		this(new TestFileParser(), new ThreadRunner(), eventBus);
	}

	protected TestListModel(TestFileParser parser, ThreadRunner runner, IEventBus eventBus) {
		this.runner = runner;
		this.parser = parser;
		this.eventBus = eventBus;
	}

	public List<String> getTestNames() {
		return testNames;
	}

	public void loadFile(final File file) {
		runner.run(() -> parser.parse(file, TestListModel.this));
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		List<String> names = new ArrayList<>();
		for (ParsedTestUnit testUnit : parsedTestFile.getTests()) {
			names.add(testUnit.getName());
		}
		this.testNames = names;
		eventBus.post(new TestListModelUpdatedEvent());
	}

}
