package presenter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import parser.ParsedTestUnit;
import view.TestItem;
import view.TestListView;
import application.IEventBus;
import application.TestListModel;

import com.google.common.eventbus.Subscribe;

import events.RunButtonClicked;
import events.TestListModelUpdatedEvent;

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
		testListModel.loadFile(new File("Tests.txt"));
	}

	@Subscribe
	public void modelUpdated(TestListModelUpdatedEvent event) {
		List<ParsedTestUnit> tests = testListModel.getTests();

		List<TestItem> testItems = tests.stream().map(testItemFactory::create).collect(Collectors.toList());

		testListView.setInput(testItems);
	}

	@Subscribe
	public void runButtonClicked(RunButtonClicked event) {
		testListModel.runAllTests();
	}

}
