package view;

import java.util.Collection;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TestListView {

	private TreeView<String> treeView = new TreeView<>();

	public void setInput(final Collection<String> testNameList) {
		TreeItem<String> rootItem = new TreeItem<String>("Tests");
		rootItem.setExpanded(true);
		for (String testName : testNameList) {
			TreeItem<String> item = new TreeItem<String>(testName);
			rootItem.getChildren().add(item);
		}
		treeView.setRoot(rootItem);
	}

	public TreeView<String> getNode() {
		return treeView;
	}

}
