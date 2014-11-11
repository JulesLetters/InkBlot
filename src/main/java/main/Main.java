package main;

import model.LineBuffer;
import model.TelnetLineReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.BufferTextWidget;
import view.InputTextWidget;
import view.TestListWidget;

public class Main {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Composite mainComposite = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, true);
		mainComposite.setLayout(gridLayout);

		TelnetLineReader telnetLineReader = new TelnetLineReader();
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		BufferTextWidget mainHistory = new BufferTextWidget(mainComposite, SWT.MULTI | SWT.READ_ONLY);
		mainHistory.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainHistory.setBuffer(lineBuffer);

		TestListWidget testListWidget = new TestListWidget(mainComposite, SWT.NONE);
		GridData layoutData = new GridData(SWT.RIGHT, SWT.FILL, true, true);
		layoutData.verticalSpan = 2;
		testListWidget.getControl().setLayoutData(layoutData);

		InputTextWidget inputTextWidget = new InputTextWidget(mainComposite, SWT.SINGLE | SWT.BORDER);
		inputTextWidget.getControl().setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
