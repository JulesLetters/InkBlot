package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.telnet.TelnetInputListener;

public class TelnetLineReader implements TelnetInputListener, ILineReader {

	private InputStream inputStream;
	private List<ILineReaderListener> listenerList = new LinkedList<>();
	private String buffer = "";

	public TelnetLineReader() {
		this(new TelnetClientFactory().getInstance());
	}

	protected TelnetLineReader(TelnetClientWrapper telnetClientWrapper) {
		telnetClientWrapper.registerInputListener(this);
		inputStream = telnetClientWrapper.getInputStream();
	}

	@Override
	public void addListener(ILineReaderListener listener) {
		listenerList.add(listener);
	}

	@Override
	public void telnetInputAvailable() {
		byte[] byteArray = new byte[1024];

		int bytesRead = 0;
		try {
			bytesRead = inputStream.read(byteArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bytesRead != 0) {
			String readString = new String(byteArray, 0, bytesRead);

			buffer = buffer.concat(readString);

			if (readString.endsWith("\n")) {
				for (ILineReaderListener listener : listenerList) {
					String withoutNewline = buffer.substring(0, buffer.length() - 1);
					listener.lineReceived(withoutNewline);
				}
				buffer = "";
			}

		}

	}

}
