package view;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

public class TestListView {

	private TreeTableView<TestItem> treeTableView = new TreeTableView<>();

	public void setInput(final Collection<TestItem> testItems) {
		TreeItem<TestItem> rootItem = new TreeItem<>(new TestItem("Tests", ""));
		rootItem.setExpanded(true);

		treeTableView.setRoot(rootItem);

		for (TestItem testItem : testItems) {
			rootItem.getChildren().add(new TreeItem<>(testItem));
		}

		TreeTableColumn<TestItem, String> statusColumn = new TreeTableColumn<>("Status");
		statusColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));

		TreeTableColumn<TestItem, String> nameColumn = new TreeTableColumn<>("Test Name");
		nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

		List<TreeTableColumn<TestItem, String>> columns = Arrays.asList(statusColumn, nameColumn);
		treeTableView.getColumns().setAll(columns);
	}

	public TreeTableView<TestItem> getNode() {
		return treeTableView;
	}

}
