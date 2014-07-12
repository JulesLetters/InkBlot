package application;

import java.io.IOException;
import java.net.SocketException;

import model.LineBuffer;
import model.TelnetClientWrapper;
import model.TelnetLineReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.BufferTextViewer;

public class Application {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());

		TelnetClientWrapper telnetClientWrapper = new TelnetClientWrapper();
		try {
			telnetClientWrapper.connect("127.0.0.1", 4201);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TelnetLineReader telnetLineReader = new TelnetLineReader(telnetClientWrapper);
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		BufferTextViewer mainHistory = new BufferTextViewer(composite, SWT.MULTI);
		mainHistory.setBuffer(lineBuffer);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
