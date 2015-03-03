package application;

import java.io.File;
import java.util.Collections;
import java.util.List;

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
		runner.run(new Runnable() {
			@Override
			public void run() {
				parser.parse(file, TestListModel.this);
			}
		});
	}

	@Override
	public void parseCompleted(ParsedTestFile parsedTestFile) {
		testNames = parsedTestFile.getTestNames();
		eventBus.post(new TestListModelUpdatedEvent());
	}

}
