package view;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import telnet.InputTextModel;
import application.GuavaEventBus;
import application.IEventBus;
import events.EnterPressedEvent;

public class InputTextWidget {

	private TextField text = new TextField();
	private IEventBus eventBus = new GuavaEventBus();

	public InputTextWidget() {
		text.setOnAction((ActionEvent) -> postAndClearText());
		InputTextModel model = new InputTextModel();
		eventBus.register(model);
	}

	private void postAndClearText() {
		if (eventBus != null) {
			eventBus.post(new EnterPressedEvent(text.getText()));
		}
		Platform.runLater(() -> text.setText(""));
	}

	public Node getNode() {
		return text;
	}

}
