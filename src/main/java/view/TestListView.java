package view;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class TestListView {

	private ListViewer listViewer;

	public TestListView(Composite parent, int style) {
		listViewer = new ListViewer(parent, style);
		listViewer.setContentProvider(ArrayContentProvider.getInstance());
	}

	public void setInput(final List<String> testNames) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				listViewer.setInput(testNames);
			}
		});

	}

	public Control getControl() {
		return listViewer.getControl();
	}

}
