package view;

import javafx.application.Platform;

public class JavaFXThreadRunner {

	public void runLater(Runnable runnable) {
		Platform.runLater(runnable);
	}

}
