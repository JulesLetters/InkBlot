package runner;

import telnet.LineBuffer;
import telnet.TelnetLineReader;
import telnet.TelnetLineReaderSingleton;
import telnet.TelnetLineWriter;

public class TestRunnerFactory {

	public TestRunner getTestRunner() {
		TelnetLineReader telnetLineReader = new TelnetLineReaderSingleton().getInstance();
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		TelnetLineWriter telnetLineWriter = new TelnetLineWriter();

		return new TestRunner(lineBuffer, telnetLineWriter);
	}

}
