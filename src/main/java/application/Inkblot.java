package application;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import telnet.LineBuffer;
import telnet.TelnetLineReader;
import telnet.TelnetLineReaderSingleton;
import view.BufferTextWidget;
import view.InputTextWidget;
import view.TestListWidget;

public class Inkblot extends Application {

	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		GridPane gridPane = new GridPane();
		root.getChildren().add(gridPane);
		Scene scene = new Scene(root);

		TelnetLineReader telnetLineReader = new TelnetLineReaderSingleton().getInstance();
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		BufferTextWidget mainHistory = new BufferTextWidget();
		gridPane.add(mainHistory.getNode(), 0, 0);
		mainHistory.setBuffer(lineBuffer);

		GuavaEventBus eventBus = new GuavaEventBus();
		TestListModel testListModel = new TestListModel(eventBus);

		TestListWidget testListWidget = new TestListWidget(testListModel);
		gridPane.add(testListWidget.getNode(), 1, 0, 2, 1);

		InputTextWidget inputTextWidget = new InputTextWidget();
		gridPane.add(inputTextWidget.getNode(), 0, 1);

		root.autosize();
		primaryStage.setTitle("Inkblot");
		primaryStage.setScene(scene);
		primaryStage.show();

		new FileLoader(testListModel).loadFiles(Paths.get("."));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
