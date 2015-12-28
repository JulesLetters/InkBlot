package view;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import telnet.LineBuffer;

public class BufferTextWidget {

	private TextArea node = new TextArea();

	public BufferTextWidget() {
		node.setEditable(false);
		node.textProperty().addListener((observable, oldValue, newValue) -> node.setScrollTop(Double.MAX_VALUE));
	}

	public void setBuffer(final LineBuffer lineBuffer) {
		lineBuffer.addListener(() -> setTextFromBuffer(lineBuffer));
	}

	private void setTextFromBuffer(final LineBuffer lineBuffer) {
		new JavaFXThreadRunner().runLater(() -> {
			node.setText(lineBuffer.getText());
			node.appendText("");
		});
	}

	public Node getNode() {
		return node;
	}

}
