package view;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import model.TestListModel;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TestListWidget {

	private ListViewer listViewer;

	public TestListWidget(Composite parent, int style) {
		listViewer = new ListViewer(parent, style);

		listViewer.setContentProvider(ArrayContentProvider.getInstance());

		List<LinkedHashMap<String, List<String>>> tests = new TestListModel().getTests();
		listViewer.setInput(tests);

		Iterator<LinkedHashMap<String, List<String>>> testsIterator = tests.iterator();
		Collection<List<String>> nameOrCommands = testsIterator.next().values();

		Iterator<List<String>> nameOrCommandsIterator = nameOrCommands.iterator();
		nameOrCommandsIterator.next();
		List<String> commands = nameOrCommandsIterator.next();

		for (String string : commands) {
			System.err.println("Command: " + string);
		}

		// ArrayList<LinkedHashMap<String, ArrayList<String>>>
	}

	public Control getControl() {
		return listViewer.getControl();
	}

}
