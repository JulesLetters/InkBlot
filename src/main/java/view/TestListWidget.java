package view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import presenter.TestListPresenter;
import application.GuavaEventBus;
import application.TestListModel;

public class TestListWidget {

	private TestListView testListView;

	public TestListWidget(Composite parent, int style) {
		testListView = new TestListView(parent, style);
		GuavaEventBus eventBus = new GuavaEventBus();
		TestListModel testListModel = new TestListModel(eventBus);

		new TestListPresenter(testListView, testListModel, eventBus);
	}

	public Control getControl() {
		return testListView.getControl();
	}
}
