package presenter;

import java.util.Collections;
import java.util.List;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;
import view.TestListView;
import application.IEventBus;
import application.TestListModel;

import com.google.common.eventbus.Subscribe;

import events.FileLoadedEvent;
import events.RunButtonClicked;
import events.TestCompletedEvent;

public class TestListPresenter {

	private TestListView testListView;
	private TestListModel testListModel;
	private TestItemFactory testItemFactory;

	public TestListPresenter(TestListView testListView, TestListModel testListModel, IEventBus eventBus) {
		this(testListView, testListModel, eventBus, new TestItemFactory());
	}

	public TestListPresenter(TestListView testListView, TestListModel testListModel, IEventBus eventBus,
			TestItemFactory testItemFactory) {
		this.testListView = testListView;
		this.testListModel = testListModel;
		this.testItemFactory = testItemFactory;
		eventBus.register(this);
	}

	@Subscribe
	public void fileLoaded(FileLoadedEvent event) {
		ParsedTestFile parsedTestFile = event.getParsedTestFile();
		TestItem testItem = testItemFactory.create(parsedTestFile);
		testListView.addAllItems(Collections.singletonList(testItem));
	}

	@Subscribe
	public void runButtonClicked(RunButtonClicked event) {
		List<ParsedTestUnit> tests = testItemFactory.getTests();
		testListModel.runAllTests(tests);
	}

	@Subscribe
	public void testCompleted(TestCompletedEvent event) {
		ParsedTestUnit unit = event.getTestUnit();
		TestResult result = event.getResult();
		testItemFactory.setUnitStatus(unit, result.getStatus());
	}

}
