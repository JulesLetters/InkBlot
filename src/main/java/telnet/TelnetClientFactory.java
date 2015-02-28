package telnet;

import java.io.IOException;
import java.net.SocketException;

public class TelnetClientFactory {

	private static TelnetClientWrapper telnetClientWrapper;

	public TelnetClientWrapper getInstance() {
		if (telnetClientWrapper == null) {
			telnetClientWrapper = new TelnetClientWrapper();
			try {
				telnetClientWrapper.connect("127.0.0.1", 4201);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return telnetClientWrapper;
	}
}
