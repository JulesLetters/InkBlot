package view;

import model.TestListModel;
import model.fileStructure.TestFile;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TestListWidget {

	private ListViewer listViewer;

	public TestListWidget(Composite parent, int style, TestListModel testListModel) {
		listViewer = new ListViewer(parent, style);

		listViewer.setContentProvider(ArrayContentProvider.getInstance());

		TestFile tests = testListModel.getTests();
		listViewer.setInput(tests.getTests());
	}

	public Control getControl() {
		return listViewer.getControl();
	}

}
