package view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import telnet.ITextChangeListener;
import telnet.LineBuffer;

public class BufferTextWidget {

	private Text text;

	public BufferTextWidget(Composite parent, int style) {
		text = new Text(parent, style);
	}

	public void setBuffer(final LineBuffer lineBuffer) {
		lineBuffer.addListener(new ITextChangeListener() {

			@Override
			public void textChanged() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						text.setText(lineBuffer.getText());
					}
				});

			}
		});
	}

	public Control getControl() {
		return text;
	}

}
