package runner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	@Mock
	private ITesterCallback testerCallback;

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

		testRunner.runTests(parsedTestList, testerCallback);

		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
	}

	@Test
	public void testSingleTestRunnerForAllUnits() throws Exception {
		parsedTestList.add(parsedTestUnit1);
		parsedTestList.add(parsedTestUnit2);

		testRunner.runTests(parsedTestList, testerCallback);

		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
		verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit2);
	}

	@Test
	public void testClearBufferBeforeEachTestIsRun() throws Exception {
		parsedTestList.add(parsedTestUnit1);
		parsedTestList.add(parsedTestUnit2);

		testRunner.runTests(parsedTestList, testerCallback);

		InOrder inOrder = Mockito.inOrder(lineBuffer, singleTestRunner);
		inOrder.verify(lineBuffer).clearText();
		inOrder.verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit1);
		inOrder.verify(lineBuffer).clearText();
		inOrder.verify(singleTestRunner).runTest(lineBuffer, lineWriter, parsedTestUnit2);
	}

	@Test
	public void invokeCallbackAfterTestCompletes() throws Exception {
		TestResult testResult = mock(TestResult.class);
		when(singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit1)).thenReturn(testResult);
		parsedTestList.add(parsedTestUnit1);

		testRunner.runTests(parsedTestList, testerCallback);

		verify(testerCallback).testCompleted(parsedTestUnit1, testResult);
	}

	@Test
	public void invokeCallbackAfterEachTestCompletes() throws Exception {
		TestResult testResult1 = mock(TestResult.class);
		TestResult testResult2 = mock(TestResult.class);
		when(singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit1)).thenReturn(testResult1);
		when(singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit2)).thenReturn(testResult2);
		parsedTestList.add(parsedTestUnit1);
		parsedTestList.add(parsedTestUnit2);

		testRunner.runTests(parsedTestList, testerCallback);

		verify(testerCallback).testCompleted(parsedTestUnit1, testResult1);
		verify(testerCallback).testCompleted(parsedTestUnit2, testResult2);
	}

}
