package runner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunnerTest {

	@Mock
	private ExecutorService executorService;
	@Mock
	private CommandRunnerTaskFactory taskFactory;
	@Mock
	private CommandRunnerTask task;
	@Mock
	private Future<String> future;
	@Mock
	private CommandResult commandResult;
	@Mock
	private IParsedTestCommand command;
	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private TelnetLineWriter lineWriter;

	private CommandRunner commandRunner;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(taskFactory.create(command, lineBuffer, lineWriter)).thenReturn(task);
		when(executorService.submit(task)).thenReturn(future);
		commandRunner = new CommandRunner(executorService, taskFactory);
	}

	@Test
	public void testSuccessfulFutureYieldsSuccess() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenReturn(CommandResult.SUCCESS);
		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);
		assertEquals(CommandResult.SUCCESS, result.getStatus());
	}

	@Test
	public void testFailedFutureYieldsFailure() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenReturn(CommandResult.FAILURE);
		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);
		assertEquals(CommandResult.FAILURE, result.getStatus());
	}

	@Test
	public void testExceptionedFutureYieldsException() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenReturn(CommandResult.EXCEPTION);
		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);
		assertEquals(CommandResult.EXCEPTION, result.getStatus());
	}

	@Test
	public void testTimeoutYieldsFailureWhenTimeoutStatusIsFailure() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
		when(command.getTimeoutStatus()).thenReturn(CommandResult.FAILURE);

		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);

		assertEquals(CommandResult.FAILURE, result.getStatus());
	}

	@Test
	public void testTimeoutYieldsExceptionWhenTimeoutStatusIsException() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
		when(command.getTimeoutStatus()).thenReturn(CommandResult.EXCEPTION);

		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);

		assertEquals(CommandResult.EXCEPTION, result.getStatus());
	}

	@Test
	public void testExecutionExceptionYieldsException() throws Exception {
		ExecutionException executionException = new ExecutionException(new Exception());
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenThrow(executionException);

		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);

		assertEquals(CommandResult.EXCEPTION, result.getStatus());
	}

	@Test
	public void testInterruptedExceptionYieldsException() throws Exception {
		when(future.get(3000L, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());

		CommandResult result = commandRunner.runCommand(command, lineBuffer, lineWriter);

		assertEquals(CommandResult.EXCEPTION, result.getStatus());
	}

}
