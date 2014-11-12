package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetInputListener;

public class TelnetClientWrapper {

	private TelnetClient telnetClient;

	public TelnetClientWrapper() {
		this(new TelnetClient());
	}

	protected TelnetClientWrapper(TelnetClient telnetClient) {
		this.telnetClient = telnetClient;
	}

	public void connect(String host, int post) throws SocketException, IOException {
		telnetClient.connect(host, post);
	}

	public void disconnect() throws IOException {
		telnetClient.disconnect();
	}

	public void registerInputListener(TelnetInputListener listener) {
		telnetClient.registerInputListener(listener);
	}

	public InputStream getInputStream() {
		return telnetClient.getInputStream();
	}

	public OutputStream getOutputStream() {
		return telnetClient.getOutputStream();
	}

}
