package telnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TelnetLineWriterTest {

	@Mock
	private ExecutorServiceFactory executorServiceFactory;
	@Mock
	private ExecutorService executorService1;
	@Mock
	private ExecutorService executorService2;
	@Mock
	private TelnetClientWrapper telnetClient;
	@Mock
	private IWriteCallback writeCallback;
	@Captor
	private ArgumentCaptor<WriteRunnable> writeRunnableCaptor;
	@Captor
	private ArgumentCaptor<Optional<Exception>> exceptionOptionalCaptor;

	private TelnetLineWriter lineWriter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(executorServiceFactory.newSingleThreadExecutor()).thenReturn(executorService1, executorService2);
		lineWriter = new TelnetLineWriter(telnetClient, executorServiceFactory);
	}

	@Test
	public void testWriteSubmitsWriteRunnableToExecutorService() throws Exception {
		lineWriter.write("Think Test", writeCallback);
		verify(executorService1).submit(writeRunnableCaptor.capture());

		WriteRunnable writeRunnable = writeRunnableCaptor.getValue();

		assertEquals("Think Test", writeRunnable.getStringToWrite());
		assertEquals(writeCallback, writeRunnable.getCallback());
		assertEquals(telnetClient, writeRunnable.getTelnetClient());
	}

	@Test
	public void testInterruptCancelsAllIncompleteFutures() throws Exception {
		WriteRunnable writeRunnable1 = mock(WriteRunnable.class);
		WriteRunnable writeRunnable2 = mock(WriteRunnable.class);
		List<Runnable> runnableList = Arrays.asList(writeRunnable1, writeRunnable2);
		when(executorService1.shutdownNow()).thenReturn(runnableList);
		IWriteCallback callback1 = mock(IWriteCallback.class);
		IWriteCallback callback2 = mock(IWriteCallback.class);
		when(writeRunnable1.getCallback()).thenReturn(callback1);
		when(writeRunnable2.getCallback()).thenReturn(callback2);

		lineWriter.write("Test1", callback1);
		lineWriter.write("Test2", callback2);
		lineWriter.interrupt();

		verify(callback1).call(exceptionOptionalCaptor.capture());
		assertTrue(exceptionOptionalCaptor.getValue().get() instanceof ClosedChannelException);
		verify(callback2).call(exceptionOptionalCaptor.capture());
		assertTrue(exceptionOptionalCaptor.getValue().get() instanceof ClosedChannelException);
	}

	@Test
	public void testUseNewExecutorServiceAfterInterrupt() throws Exception {
		lineWriter.write("Think Test", writeCallback);

		verify(executorService1).submit(writeRunnableCaptor.capture());

		lineWriter.interrupt();

		lineWriter.write("Think Test", writeCallback);

		verify(executorService2).submit(writeRunnableCaptor.capture());
	}

	@Test
	public void testUseTheSameExecutorServiceUntilInterrupted() throws Exception {
		lineWriter.write("Think Test1", writeCallback);
		verify(executorService1).submit(writeRunnableCaptor.capture());

		lineWriter.write("Think Test2", writeCallback);
		verify(executorService1, times(2)).submit(writeRunnableCaptor.capture());

		verifyZeroInteractions(executorService2);
	}

}
