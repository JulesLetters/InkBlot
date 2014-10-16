package model;

import java.io.IOException;
import java.net.SocketException;

public class TelnetClientFactory {

	public TelnetClientWrapper create() {
		TelnetClientWrapper telnetClientWrapper = new TelnetClientWrapper();
		try {
			telnetClientWrapper.connect("127.0.0.1", 4201);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return telnetClientWrapper;
	}
}
