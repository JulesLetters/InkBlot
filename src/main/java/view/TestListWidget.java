package view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import presenter.TestListPresenter;
import application.GuavaEventBus;
import application.TestListModel;
import events.RunButtonClicked;

public class TestListWidget {

	final GridPane gridPane = new GridPane();

	public TestListWidget() {
		TestListView testListView = new TestListView();
		gridPane.add(testListView.getNode(), 0, 0);

		Button runAllButton = new Button("Run All");
		gridPane.add(runAllButton, 0, 1);

		GuavaEventBus eventBus = new GuavaEventBus();
		runAllButton.setOnAction(event -> eventBus.post(new RunButtonClicked()));
		TestListModel testListModel = new TestListModel(eventBus);

		new TestListPresenter(testListView, testListModel, eventBus);
	}

	public Node getNode() {
		return gridPane;
	}
}
