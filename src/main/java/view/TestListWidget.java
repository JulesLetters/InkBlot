package view;

import javafx.scene.Node;
import presenter.TestListPresenter;
import application.GuavaEventBus;
import application.TestListModel;

public class TestListWidget {

	private TestListView testListView;

	public TestListWidget() {
		testListView = new TestListView();
		GuavaEventBus eventBus = new GuavaEventBus();
		TestListModel testListModel = new TestListModel(eventBus);

		new TestListPresenter(testListView, testListModel, eventBus);
	}

	public Node getNode() {
		return testListView.getNode();
	}
}
