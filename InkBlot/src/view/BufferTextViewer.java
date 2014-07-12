package view;

import model.ITextChangeListener;
import model.LineBuffer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class BufferTextViewer {

	private Text text;

	public BufferTextViewer(Composite composite, int style) {
		text = new Text(composite, style);
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

}
