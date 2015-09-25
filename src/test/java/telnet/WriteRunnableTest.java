package telnet;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class WriteRunnableTest {

	@Mock
	private IWriteCallback writeCallback;
	@Mock
	private WritableByteChannel writeChannel;
	@Captor
	private ArgumentCaptor<ByteBuffer> byteBufferCaptor;

	private WriteRunnable writeRunnable;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRunnableWritesStringPlusNewlineToChannel() throws IOException {
		String stringToWrite = "think Hello";
		String expectedWrittenText = stringToWrite + "\n";
		writeRunnable = new WriteRunnable(stringToWrite, writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeChannel).write(byteBufferCaptor.capture());
		ByteBuffer byteBuffer = byteBufferCaptor.getValue();
		byte[] writtenByteArray = new byte[expectedWrittenText.length()];
		byteBuffer.get(writtenByteArray);
		assertArrayEquals(expectedWrittenText.getBytes(), writtenByteArray);
	}

	@Test
	public void testAfterWritingInvokeCallbackWithEmptyOptional() throws Exception {
		writeRunnable = new WriteRunnable("think Hi There!", writeCallback, writeChannel);

		writeRunnable.run();

		InOrder inOrder = Mockito.inOrder(writeChannel, writeCallback);
		inOrder.verify(writeChannel).write(byteBufferCaptor.capture());
		inOrder.verify(writeCallback).call(Optional.empty());
	}

	@Test
	public void testCallCallbackWithExceptionWhenChannelNotOpen() throws Exception {
		NonWritableChannelException exception = new NonWritableChannelException();
		when(writeChannel.write(any())).thenThrow(exception);
		writeRunnable = new WriteRunnable("think Exceptions!", writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeCallback).call(Optional.of(exception));
		verify(writeCallback, never()).call(Optional.empty());
	}

	@Test
	public void testCallCallbackWithExceptionWhenChannelClosed() throws Exception {
		ClosedChannelException exception = new ClosedChannelException();
		when(writeChannel.write(any())).thenThrow(exception);
		writeRunnable = new WriteRunnable("think Exceptions!", writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeCallback).call(Optional.of(exception));
		verify(writeCallback, never()).call(Optional.empty());
	}

	@Test
	public void testCallCallbackWithExceptionWhenAsynchronousCloseOccurs() throws Exception {
		AsynchronousCloseException exception = new AsynchronousCloseException();
		when(writeChannel.write(any())).thenThrow(exception);
		writeRunnable = new WriteRunnable("think Exceptions!", writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeCallback).call(Optional.of(exception));
		verify(writeCallback, never()).call(Optional.empty());
	}

	@Test
	public void testCallCallbackWithExceptionWhenChannelClosedByInterrupt() throws Exception {
		String additionalNote = "think This one also deals with shutdown()!";
		ClosedByInterruptException exception = new ClosedByInterruptException();
		when(writeChannel.write(any())).thenThrow(exception);
		writeRunnable = new WriteRunnable(additionalNote, writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeCallback).call(Optional.of(exception));
		verify(writeCallback, never()).call(Optional.empty());
	}

	@Test
	public void testCallCallbackWithExceptionWhenIOExceptionOccurs() throws Exception {
		IOException exception = new IOException();
		when(writeChannel.write(any())).thenThrow(exception);
		writeRunnable = new WriteRunnable("think Exceptions!", writeCallback, writeChannel);

		writeRunnable.run();

		verify(writeCallback).call(Optional.of(exception));
		verify(writeCallback, never()).call(Optional.empty());
	}

}
