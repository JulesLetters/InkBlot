package view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TestListWidget {

	private ListViewer listViewer;

	public TestListWidget(Composite parent, int style) {
		listViewer = new ListViewer(parent, style);

		listViewer.setContentProvider(ArrayContentProvider.getInstance());

		// listViewer.setInput(new TestListModel().getTestNames());
	}

	public Control getControl() {
		return listViewer.getControl();
	}

}
