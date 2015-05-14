package view;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import telnet.LineBuffer;

public class BufferTextWidget {

	private TextArea node = new TextArea();

	public BufferTextWidget() {
		node.setEditable(false);
	}

	public void setBuffer(final LineBuffer lineBuffer) {
		lineBuffer.addListener(() -> setTextFromBuffer(lineBuffer));
	}

	private void setTextFromBuffer(final LineBuffer lineBuffer) {
		Platform.runLater(() -> node.setText(lineBuffer.getText()));
	}

	public Node getNode() {
		return node;
	}

}
