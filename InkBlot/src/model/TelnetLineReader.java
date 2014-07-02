package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.telnet.TelnetInputListener;

public class TelnetLineReader implements TelnetInputListener {

	private InputStream inputStream;
	private List<ITelnetLineReaderListener> listenerList = new LinkedList<>();

	public TelnetLineReader() {
	}

	public TelnetLineReader(TelnetClientWrapper telnetClientWrapper) {
		telnetClientWrapper.registerInputListener(this);
		inputStream = telnetClientWrapper.getInputStream();
	}

	public void addListener(ITelnetLineReaderListener listener) {
		listenerList.add(listener);
	}

	@Override
	public void telnetInputAvailable() {
		byte[] byteArray = new byte[1024];

		int bytesRead;
		try {
			bytesRead = inputStream.read(byteArray);
			if (bytesRead != 0) {
				String string = new String(byteArray, 0, bytesRead - 1);

				for (ITelnetLineReaderListener listener : listenerList) {
					listener.lineReceived(string);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
