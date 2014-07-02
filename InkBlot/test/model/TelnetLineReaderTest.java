package model;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TelnetLineReaderTest {

	@Mock
	private TelnetClientWrapper telnetClient;
	@Mock
	private InputStream inputStream;
	@Mock
	private ITelnetLineReaderListener listener;

	private TelnetLineReader telnetLineReader;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(telnetClient.getInputStream()).thenReturn(inputStream);
		telnetLineReader = new TelnetLineReader(telnetClient);
	}

	@Test
	public void testListenerAddedToTelnetClientOnConstruction() {
		verify(telnetClient).registerInputListener(telnetLineReader);
	}

	@Test
	public void testListenersNotifiedWhenNewlineArrives() throws Exception {
		when(inputStream.read(any(byte[].class))).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				byte[] byteArray = (byte[]) args[0];
				byteArray[0] = '\n';
				return 1;
			}
		});

		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("");
	}

	@Test
	public void testListenersNotifiedWhenTextEndingInNewlineArrives()
			throws Exception {
		when(inputStream.read(any(byte[].class))).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				byte[] byteArray = (byte[]) args[0];
				byteArray[0] = 'a';
				byteArray[1] = '\n';
				return 2;
			}
		});

		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("a");
	}

	@Test
	public void testListenerDoesNotReadUntilInputAvailable() throws Exception {

		telnetLineReader.addListener(listener);

		verify(inputStream, never()).read(any(byte[].class));
		verify(listener, never()).lineReceived(anyString());
	}

	@Test
	public void testAllListenersNotifiedWhenTextEndingInNewlineArrives()
			throws Exception {
		when(inputStream.read(any(byte[].class))).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				byte[] byteArray = (byte[]) args[0];
				byteArray[0] = 'b';
				byteArray[1] = '\n';
				return 2;
			}
		});

		ITelnetLineReaderListener listener2 = mock(ITelnetLineReaderListener.class);
		telnetLineReader.addListener(listener);
		telnetLineReader.addListener(listener2);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("b");
		verify(listener2).lineReceived("b");
	}

}
