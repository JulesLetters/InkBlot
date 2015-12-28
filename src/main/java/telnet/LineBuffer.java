package telnet;

import java.util.ArrayList;
import java.util.List;

public class LineBuffer implements ILineReaderListener {

	private List<ITextChangeListener> listenerList = new ArrayList<>();
	private String buffer = "";

	public void setLineReader(ILineReader lineReader) {
		lineReader.addListener(this);
	}

	@Override
	public void lineReceived(String string) {
		String stringToAppend = string.replaceAll("\r", "");

		if (buffer.isEmpty()) {
			buffer = stringToAppend;
		} else {
			buffer = buffer + "\n" + stringToAppend;
		}

		listenerList.forEach(listener -> listener.textChanged());
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

	public void removeListener(ITextChangeListener listener) {
		listenerList.remove(listener);
	}
}
