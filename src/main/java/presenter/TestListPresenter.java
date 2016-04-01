package presenter;

import java.util.Collections;

import model.ParsedTestModel;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
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
	private ParsedTestModel parsedTestModel;

	public TestListPresenter(TestListView testListView, TestListModel testListModel, ParsedTestModel parsedTestModel,
			IEventBus eventBus) {
		this(testListView, testListModel, parsedTestModel, eventBus, new TestItemFactory());
	}

	public TestListPresenter(TestListView testListView, TestListModel testListModel, ParsedTestModel parsedTestModel,
			IEventBus eventBus, TestItemFactory testItemFactory) {
		this.testListView = testListView;
		this.testListModel = testListModel;
		this.parsedTestModel = parsedTestModel;
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
		testListModel.runAllTests();
	}

	@Subscribe
	public void testCompleted(TestCompletedEvent event) {
		ParsedTestUnit unit = event.getTestUnit();
		String unitStatus = parsedTestModel.getUnitStatus(unit);
		testItemFactory.setUnitStatus(unit, unitStatus);
	}

}
