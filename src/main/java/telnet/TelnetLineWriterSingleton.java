package telnet;

public class TelnetLineWriterSingleton {

	private static TelnetLineWriter INSTANCE = new TelnetLineWriter();

	public TelnetLineWriter getInstance() {
		return INSTANCE;
	}

}
