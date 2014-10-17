package application;

import model.LineBuffer;
import model.TelnetLineReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.BufferTextViewer;
import view.InputTextWidget;

public class Application {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Composite mainComposite = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, true);
		mainComposite.setLayout(gridLayout);

		TelnetLineReader telnetLineReader = new TelnetLineReader();
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		BufferTextViewer mainHistory = new BufferTextViewer(mainComposite, SWT.MULTI | SWT.READ_ONLY);
		mainHistory.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainHistory.setBuffer(lineBuffer);

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
