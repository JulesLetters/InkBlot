package model;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
	private ILineReaderListener listener;

	private StringToByteArray stringToByteArray = new StringToByteArray();

	private TelnetLineReader telnetLineReader;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		when(telnetClient.getInputStream()).thenReturn(inputStream);
		when(inputStream.read(any(byte[].class))).thenAnswer(stringToByteArray);
		telnetLineReader = new TelnetLineReader(telnetClient);
	}

	@Test
	public void testListenerAddedToTelnetClientOnConstruction() {
		verify(telnetClient).registerInputListener(telnetLineReader);
	}

	@Test
	public void testListenersNotifiedWhenNewlineArrives() throws Exception {
		setupInputStream("\n");
		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("");
	}

	@Test
	public void testListenersNotifiedWhenTextEndingInNewlineArrives() throws Exception {
		setupInputStream("a\n");
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
	public void testAllListenersNotifiedWhenTextEndingInNewlineArrives() throws Exception {
		setupInputStream("b\n");
		ILineReaderListener listener2 = mock(ILineReaderListener.class);
		telnetLineReader.addListener(listener);
		telnetLineReader.addListener(listener2);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("b");
		verify(listener2).lineReceived("b");
	}

	@Test
	public void testListenersAreNotNotifiedWhenTextDoesNotEndWithNewline() throws Exception {
		setupInputStream("a");
		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener, never()).lineReceived(any(String.class));
	}

	@Test
	public void testWhenNewlineReceivedSendPreviousCharacters() throws Exception {
		setupInputStream("a");
		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener, never()).lineReceived(any(String.class));

		setupInputStream("b\n");
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("ab");
	}

	@Test
	public void testPreviousCharactersAreDeletedAfterNewline() throws Exception {
		setupInputStream("abc\n");
		telnetLineReader.addListener(listener);
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("abc");

		setupInputStream("xyz\n");
		telnetLineReader.telnetInputAvailable();

		verify(listener).lineReceived("xyz");
	}

	private void setupInputStream(final String stringToBeRead) {
		stringToByteArray.setReadableString(stringToBeRead);
	}

	private class StringToByteArray implements Answer<Integer> {
		private String stringToBeRead;

		public void setReadableString(String stringToBeRead) {
			this.stringToBeRead = stringToBeRead;
		}

		@Override
		public Integer answer(InvocationOnMock invocation) throws Throwable {
			byte[] byteArray = (byte[]) invocation.getArguments()[0];
			byte[] stringBytes = stringToBeRead.getBytes();
			for (int i = 0; i < stringBytes.length; i++) {
				byteArray[i] = stringBytes[i];
			}

			return stringBytes.length;
		}
	}
}
