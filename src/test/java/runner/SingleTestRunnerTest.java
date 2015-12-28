package runner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.IParsedTestCommand;
import parser.ParsedTestUnit;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class SingleTestRunnerTest {

	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private TelnetLineWriter lineWriter;
	@Mock
	private ParsedTestUnit parsedTestUnit;
	@Mock
	private IParsedTestCommand command1;
	@Mock
	private IParsedTestCommand command2;
	@Mock
	private CommandResult commandResult1;
	@Mock
	private CommandResult commandResult2;
	@Mock
	private CommandRunner commandRunner;

	private List<IParsedTestCommand> commandList = new ArrayList<>();

	private SingleTestRunner singleTestRunner;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(parsedTestUnit.getCommands()).thenReturn(commandList);
		singleTestRunner = new SingleTestRunner(commandRunner);
	}

	@Test
	public void testSuccessfulCommandYieldsSuccessfulTest() throws Exception {
		commandList.add(command1);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandResult1.getStatus()).thenReturn(CommandResult.SUCCESS);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.SUCCESS, testResult.getStatus());
	}

	@Test
	public void testFailedCommandYieldsFailedTest() throws Exception {
		commandList.add(command1);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandResult1.getStatus()).thenReturn(CommandResult.FAILURE);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.FAILURE, testResult.getStatus());
	}

	@Test
	public void testExceptionedCommandYieldsExceptionedTest() throws Exception {
		commandList.add(command1);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandResult1.getStatus()).thenReturn(CommandResult.EXCEPTION);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.EXCEPTION, testResult.getStatus());
	}

	@Test
	public void testSuccessThenFailureYieldsFailedTest() throws Exception {
		commandList.add(command1);
		commandList.add(command2);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandRunner.runCommand(command2, lineBuffer, lineWriter)).thenReturn(commandResult2);
		when(commandResult1.getStatus()).thenReturn(CommandResult.SUCCESS);
		when(commandResult2.getStatus()).thenReturn(CommandResult.FAILURE);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.FAILURE, testResult.getStatus());
	}

	@Test
	public void testSuccessThenExceptionYieldsExceptionedTest() throws Exception {
		commandList.add(command1);
		commandList.add(command2);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandRunner.runCommand(command2, lineBuffer, lineWriter)).thenReturn(commandResult2);
		when(commandResult1.getStatus()).thenReturn(CommandResult.SUCCESS);
		when(commandResult2.getStatus()).thenReturn(CommandResult.EXCEPTION);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.EXCEPTION, testResult.getStatus());
	}

	@Test
	public void testCommandsAreNotRunAfterFailure() throws Exception {
		commandList.add(command1);
		commandList.add(command2);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandResult1.getStatus()).thenReturn(CommandResult.FAILURE);
		when(commandResult2.getStatus()).thenReturn(CommandResult.SUCCESS);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.FAILURE, testResult.getStatus());
		verify(commandRunner, never()).runCommand(command2, lineBuffer, lineWriter);
	}

	@Test
	public void testCommandsAreNotRunAfterException() throws Exception {
		commandList.add(command1);
		commandList.add(command2);
		when(commandRunner.runCommand(command1, lineBuffer, lineWriter)).thenReturn(commandResult1);
		when(commandResult1.getStatus()).thenReturn(CommandResult.EXCEPTION);
		when(commandResult2.getStatus()).thenReturn(CommandResult.SUCCESS);

		TestResult testResult = singleTestRunner.runTest(lineBuffer, lineWriter, parsedTestUnit);

		assertEquals(TestResult.EXCEPTION, testResult.getStatus());
		verify(commandRunner, never()).runCommand(command2, lineBuffer, lineWriter);
	}
}
