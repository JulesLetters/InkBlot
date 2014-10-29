package view;

import model.TestListModel;
import model.fileStructure.TestFile;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TestListWidget {

	private ListViewer listViewer;

	public TestListWidget(Composite parent, int style) {
		listViewer = new ListViewer(parent, style);

		listViewer.setContentProvider(ArrayContentProvider.getInstance());

		TestFile tests = new TestListModel().getTests();
		listViewer.setInput(tests.getTests());
	}

	public Control getControl() {
		return listViewer.getControl();
	}

}
