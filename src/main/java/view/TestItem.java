package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestItem {

	private StringProperty name = new SimpleStringProperty(this, "name");
	private StringProperty status = new SimpleStringProperty(this, "status");

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
		return name;
	}

	public StringProperty statusProperty() {
		return status;
	}

}
