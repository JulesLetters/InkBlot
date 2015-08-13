package telnet;

public class TelnetLineReaderSingleton {

	private static TelnetLineReader INSTANCE = new TelnetLineReader();

	public TelnetLineReader getInstance() {
		return INSTANCE;
	}

}
