package model;

import java.util.LinkedList;
import java.util.List;

public class LineBuffer implements ILineReaderListener {

	private List<ITextChangeListener> listenerList = new LinkedList<>();
	private String buffer = "";

	public void setLineReader(ILineReader lineReader) {
		lineReader.addListener(this);
	}

	@Override
	public void lineReceived(String string) {
		if (buffer == "") {
			buffer = string;
		} else {
			buffer = buffer + "\n" + string;
		}

		for (ITextChangeListener listener : listenerList) {
			listener.textChanged();
		}
	}

	public String getText() {
		return buffer;
	}

	public void clearText() {
		buffer = "";
	}

	public void addListener(ITextChangeListener listener) {
		listenerList.add(listener);
	}
}
