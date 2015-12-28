package runner;

import java.util.List;

import parser.ParsedTestUnit;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class TestRunner {

	private SingleTestRunner singleTestRunner;
	private LineBuffer lineBuffer;
	private TelnetLineWriter lineWriter;

	public TestRunner(LineBuffer lineBuffer, TelnetLineWriter lineWriter) {
		this(lineBuffer, lineWriter, new SingleTestRunner());
	}

	TestRunner(LineBuffer lineBuffer, TelnetLineWriter lineWriter, SingleTestRunner singleTestRunner) {
		this.singleTestRunner = singleTestRunner;
		this.lineBuffer = lineBuffer;
		this.lineWriter = lineWriter;
	}

	public void runTests(List<ParsedTestUnit> parsedTestList, ITesterCallback callback) {
		for (ParsedTestUnit parsedTestUnit : parsedTestList) {
			lineBuffer.clearText();
			TestResult result = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);
			callback.testCompleted(parsedTestUnit, result);
		}
	}

}
