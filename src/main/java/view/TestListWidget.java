package view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.ParsedTestModel;
import presenter.TestListPresenter;
import application.IEventBus;
import application.TestListModel;
import events.RunButtonClicked;

public class TestListWidget {

	final GridPane gridPane = new GridPane();

	public TestListWidget(TestListModel testListModel, ParsedTestModel parsedTestModel) {
		TestListView testListView = new TestListView();
		gridPane.add(testListView.getNode(), 0, 0);

		Button runAllButton = new Button("Run All");
		gridPane.add(runAllButton, 0, 1);

		IEventBus eventBus = testListModel.getEventBus();
		runAllButton.setOnAction(event -> eventBus.post(new RunButtonClicked()));

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus);

	}

	public Node getNode() {
		return gridPane;
	}
}
