package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestItem {

	private StringProperty name;
	private StringProperty status;

	public TestItem(String name, String status) {
		setName(name);
		setStatus(status);
	}

	public void setName(String name) {
		nameProperty().set(name);
	}

	public String getName() {
		return nameProperty().get();
	}

	public void setStatus(String status) {
		statusProperty().set(status);
	}

	public String getStatus() {
		return statusProperty().get();
	}

	public StringProperty nameProperty() {
		if (name == null) {
			name = new SimpleStringProperty(this, "name");
		}
		return name;
	}

	public StringProperty statusProperty() {
		if (status == null) {
			status = new SimpleStringProperty(this, "status");
		}
		return status;
	}

}
