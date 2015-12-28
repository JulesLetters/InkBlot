package runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommandResultListenerTest {

	@Mock
	private CommandResult commandResult1;
	@Mock
	private CommandResult commandResult2;

	private CommandResultListener commandResultListener;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		commandResultListener = new CommandResultListener();
	}

	@Test
	public void testGetStatusReturnsSetStatus() throws Exception {
		commandResultListener.setStatus(commandResult1);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testGetStatusReturnsSetTimeoutStatus() throws Exception {
		commandResultListener.setTimeoutStatus(commandResult1);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testSetStatusTwiceDoesNotOverwriteOldStatus() throws Exception {
		commandResultListener.setStatus(commandResult1);
		commandResultListener.setStatus(commandResult2);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testSetTimeoutStatusTwiceDoesNotOverwriteOldStatus() throws Exception {
		commandResultListener.setTimeoutStatus(commandResult1);
		commandResultListener.setTimeoutStatus(commandResult2);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testSetStatusThenTimeoutStatusDoesNotOverwriteOldStatus() throws Exception {
		commandResultListener.setStatus(commandResult1);
		commandResultListener.setTimeoutStatus(commandResult2);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testSetTimeoutStatusThenStatusDoesNotOverwriteOldStatus() throws Exception {
		commandResultListener.setTimeoutStatus(commandResult1);
		commandResultListener.setStatus(commandResult2);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult1, actualResult);
	}

	@Test
	public void testIgnoreCausesSetStatusToBeIgnoredAndReturnsTrue() throws Exception {
		assertTrue(commandResultListener.ignoreRegularStatus());
		commandResultListener.setStatus(commandResult1);
		commandResultListener.setTimeoutStatus(commandResult2);
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(commandResult2, actualResult);
	}

	@Test
	public void testIgnoreReturnsFalseIfStatusAlreadySet() throws Exception {
		commandResultListener.setStatus(commandResult1);
		assertFalse(commandResultListener.ignoreRegularStatus());
	}

	@Test
	public void testGetStatusReturnsExceptionCommandResultIfPreemptivelyInterrupted() throws Exception {
		Thread.currentThread().interrupt();
		CommandResult actualResult = commandResultListener.getStatus();
		assertEquals(CommandResult.EXCEPTION, actualResult.getStatus());
	}

	@Test
	public void testGetStatusReturnsExceptionCommandResultIfInterrupted() throws Exception {
		// Rarely, this test's threads could run in non-deterministic order.
		// This test attempts to check that after making the getStatus() call,
		// interrupting the thread gives CommandResult.EXCEPTION as the result.

		final BlockingQueue<CommandResult> futureResultQueue = new ArrayBlockingQueue<>(1);

		Thread takingThread = new Thread(() -> {
			futureResultQueue.add(commandResultListener.getStatus());
		});
		takingThread.start();

		Thread interrupter = new Thread(() -> {
			Thread.yield();
			takingThread.interrupt();
		});
		interrupter.start();

		CommandResult actualResult = futureResultQueue.take();
		assertEquals(CommandResult.EXCEPTION, actualResult.getStatus());
	}

}
