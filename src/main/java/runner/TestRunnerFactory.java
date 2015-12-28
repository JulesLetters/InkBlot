package runner;

import telnet.LineBuffer;
import telnet.TelnetLineReader;
import telnet.TelnetLineReaderSingleton;
import telnet.TelnetLineWriter;
import telnet.TelnetLineWriterSingleton;

public class TestRunnerFactory {

	public TestRunner getTestRunner() {
		TelnetLineReader telnetLineReader = new TelnetLineReaderSingleton().getInstance();
		LineBuffer lineBuffer = new LineBuffer();
		lineBuffer.setLineReader(telnetLineReader);

		TelnetLineWriter telnetLineWriter = new TelnetLineWriterSingleton().getInstance();

		return new TestRunner(lineBuffer, telnetLineWriter);
	}

}
