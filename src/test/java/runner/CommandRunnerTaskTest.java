package runner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunnerTaskTest {

	@Mock
	private IParsedTestCommand command;
	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private TelnetLineWriter lineWriter;

	private CommandRunnerTask task;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		task = new CommandRunnerTask(command, lineBuffer, lineWriter);
	}

	@Test
	public void testSuccessFromCommandYieldsSucess() throws Exception {
		when(command.execute(lineBuffer, lineWriter)).thenReturn(CommandResult.SUCCESS);
		assertEquals(CommandResult.SUCCESS, task.call());
	}

	@Test
	public void testFailureFromCommandYieldsFailure() throws Exception {
		when(command.execute(lineBuffer, lineWriter)).thenReturn(CommandResult.FAILURE);
		assertEquals(CommandResult.FAILURE, task.call());
	}

	@Test
	public void testExceptionFromCommandYieldsException() throws Exception {
		when(command.execute(lineBuffer, lineWriter)).thenReturn(CommandResult.EXCEPTION);
		assertEquals(CommandResult.EXCEPTION, task.call());
	}

}
