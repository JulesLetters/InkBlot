package view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestItem {

	private StringProperty name;
	private StringProperty status;
	private List<TestItem> children;

	public TestItem(String name, String status) {
		this(name, status, new ArrayList<>());
	}

	public TestItem(String name, String status, List<TestItem> children) {
		setName(name);
		setStatus(status);
		this.children = children;
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

	public List<TestItem> getChildren() {
		return children;
	}
}
