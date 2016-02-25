package presenter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import model.ParsedTestModel;
import parser.ParsedTestUnit;
import view.TestItem;
import view.TestListView;
import application.IEventBus;
import application.TestListModel;

import com.google.common.eventbus.Subscribe;

import events.RunButtonClicked;
import events.TestCompletedEvent;
import events.TestListModelUpdatedEvent;

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
		testListModel.loadFile(new File("Tests.txt"));
	}

	@Subscribe
	public void modelUpdated(TestListModelUpdatedEvent event) {
		List<ParsedTestUnit> tests = parsedTestModel.getTests();
		List<TestItem> testItems = tests.stream().map(testItemFactory::create).collect(Collectors.toList());
		testListView.setInput(testItems);
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
