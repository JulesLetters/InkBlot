package view;

import org.eclipse.swt.widgets.Display;

public class SWTThreadRunner {

	public void asyncExec(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

}
