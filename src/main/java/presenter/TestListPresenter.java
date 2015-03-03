package presenter;

import java.io.File;

import view.TestListView;
import application.IEventBus;
import application.TestListModel;

import com.google.common.eventbus.Subscribe;

import events.TestListModelUpdatedEvent;

public class TestListPresenter {

	private TestListView testListView;
	private TestListModel testListModel;

	public TestListPresenter(TestListView testListView, TestListModel testListModel, IEventBus eventBus) {
		this.testListView = testListView;
		this.testListModel = testListModel;
		eventBus.register(this);
		testListModel.loadFile(new File("Tests.txt"));
	}

	@Subscribe
	public void modelUpdated(TestListModelUpdatedEvent event) {
		testListView.setInput(testListModel.getTestNames());
	}

}
