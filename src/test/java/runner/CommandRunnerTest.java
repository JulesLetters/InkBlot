package runner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import parser.IParsedTestCommand;
import telnet.LineBuffer;
import telnet.TelnetLineWriter;

public class CommandRunnerTest {

	@Mock
	private ScheduledExecutorService scheduledExecutorService;
	@Mock
	private CommandRunnerListenerFactory listenerFactory;
	@Mock
	private CommandResultListener commandResultListener;
	@Mock
	private IParsedTestCommand command;
	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private TelnetLineWriter lineWriter;
	@Mock
	private CommandResult commandResult;
	@Mock
	@SuppressWarnings("rawtypes")
	private ScheduledFuture future;
	@Captor
	private ArgumentCaptor<Runnable> runnableCaptor;

	private CommandRunner commandRunner;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(listenerFactory.create()).thenReturn(commandResultListener);
		when(scheduledExecutorService.schedule(runnableCaptor.capture(), eq(3000L), eq(TimeUnit.MILLISECONDS)))
				.thenReturn(future);
		commandRunner = new CommandRunner(scheduledExecutorService, listenerFactory);
	}

	@Test
	public void testCommandIsRunAndResultFromListenerIsReturned() throws Exception {
		doAnswer((InvocationOnMock invocation) -> {
			when(commandResultListener.getStatus()).thenReturn(commandResult);
			return null;
		}).when(command).execute(lineBuffer, lineWriter, commandResultListener);

		CommandResult actualResult = commandRunner.runCommand(command, lineBuffer, lineWriter);

		assertEquals(commandResult, actualResult);
	}

	@Test
	public void testTimeoutCommandAfterDelay() throws Exception {
		when(commandResultListener.ignoreRegularStatus()).thenReturn(true);
		commandRunner.runCommand(command, lineBuffer, lineWriter);

		verify(scheduledExecutorService).schedule(runnableCaptor.capture(), eq(3000L), eq(TimeUnit.MILLISECONDS));
		verify(command, never()).timeout(lineBuffer, lineWriter, commandResultListener);

		runnableCaptor.getValue().run();

		verify(command).timeout(lineBuffer, lineWriter, commandResultListener);
	}

	@Test
	public void testDoNotTimeoutCommandIfListenerCannotLockMutex() throws Exception {
		when(commandResultListener.ignoreRegularStatus()).thenReturn(false);

		commandRunner.runCommand(command, lineBuffer, lineWriter);

		verify(scheduledExecutorService).schedule(runnableCaptor.capture(), eq(3000L), eq(TimeUnit.MILLISECONDS));

		runnableCaptor.getValue().run();

		verify(command, never()).timeout(lineBuffer, lineWriter, commandResultListener);
	}

	@Test
	public void testScheduleTimeoutPriorToListeningForResult() throws Exception {
		commandRunner.runCommand(command, lineBuffer, lineWriter);

		InOrder inOrder = Mockito.inOrder(scheduledExecutorService, commandResultListener);
		inOrder.verify(scheduledExecutorService).schedule(runnableCaptor.capture(), eq(3000L),
				eq(TimeUnit.MILLISECONDS));
		inOrder.verify(commandResultListener).getStatus();
	}

	@Test
	public void testCancelScheduledFutureAfterStatusIsRetrieved() throws Exception {
		commandRunner.runCommand(command, lineBuffer, lineWriter);

		InOrder inOrder = Mockito.inOrder(commandResultListener, future);
		inOrder.verify(commandResultListener).getStatus();
		inOrder.verify(future).cancel(false);
	}

	@Test
	public void testStopCommandAfterStatusIsRetrieved() throws Exception {
		commandRunner.runCommand(command, lineBuffer, lineWriter);

		InOrder inOrder = Mockito.inOrder(commandResultListener, command);
		inOrder.verify(commandResultListener).getStatus();
		inOrder.verify(command).stop(lineBuffer, lineWriter, commandResultListener);
	}

}
