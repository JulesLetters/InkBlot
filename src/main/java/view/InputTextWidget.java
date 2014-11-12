package view;

import model.InputTextModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import application.GuavaEventBus;
import application.IEventBus;
import events.EnterPressedEvent;

public class InputTextWidget {

	private Text text;
	private IEventBus eventBus = new GuavaEventBus();

	public InputTextWidget(Composite parent, int style) {
		text = new Text(parent, style);
		text.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					if (eventBus != null) {
						eventBus.post(new EnterPressedEvent(text.getText()));
					}
					text.setText("");
				}
			}
		});

		InputTextModel model = new InputTextModel();
		eventBus.register(model);
	}

	public Control getControl() {
		return text;
	}

}
