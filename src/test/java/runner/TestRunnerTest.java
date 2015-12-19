package runner;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestUnit;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class TestRunnerTest {

	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private TelnetLineWriter lineWriter;
	@Mock
	private SingleTestRunner singleTestRunner;
	@Mock
	private ParsedTestUnit parsedTestUnit1;
	@Mock
	private ParsedTestUnit parsedTestUnit2;

	private List<ParsedTestUnit> parsedTestList = new ArrayList<>();

	private TestRunner testRunner;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testRunner = new TestRunner(lineBuffer, lineWriter, singleTestRunner);
	}

	@Test
	public void testSingleTestRunnerIsInvoked() throws Exception {
		parsedTestList.add(parsedTestUnit1);

		testRunner.runTests(parsedTestList);

		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
	}

	@Test
	public void testSingleTestRunnerForAllUnits() throws Exception {
		parsedTestList.add(parsedTestUnit1);
		parsedTestList.add(parsedTestUnit2);

		testRunner.runTests(parsedTestList);

		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit2);
	}

	@Test
	public void testClearBufferBeforeEachTestIsRun() throws Exception {
		parsedTestList.add(parsedTestUnit1);
		parsedTestList.add(parsedTestUnit2);

		testRunner.runTests(parsedTestList);

		InOrder inOrder = Mockito.inOrder(lineBuffer, singleTestRunner);
		inOrder.verify(lineBuffer).clearText();
		inOrder.verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
		inOrder.verify(lineBuffer).clearText();
		inOrder.verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit2);
	}

}
