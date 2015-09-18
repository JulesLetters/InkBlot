package telnet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LineBufferTest {

	@Mock
	private ILineReader lineReader;

	private LineBuffer lineBuffer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		lineBuffer = new LineBuffer();
	}

	@Test
	public void testListenerRegisteredOnSetLineReader() {
		lineBuffer.setLineReader(lineReader);
		verify(lineReader).addListener(lineBuffer);
	}

	@Test
	public void testGetTextStartsEmpty() {
		assertEquals("", lineBuffer.getText());
	}

	@Test
	public void testGetTextGetsTextFromLineReceived() {
		String expectedString = "Hello";

		lineBuffer.lineReceived(expectedString);

		assertEquals(expectedString, lineBuffer.getText());
	}

	@Test
	public void testBufferedTextIsSeparatedByNewlines() {
		String hello = "Hello";
		String expectedString = hello + "\n" + hello;

		lineBuffer.lineReceived(hello);
		lineBuffer.lineReceived(hello);

		assertEquals(expectedString, lineBuffer.getText());
	}

	@Test
	public void testGetTextIsResetWhenClearTextCalled() {
		String hello = "Hello";

		lineBuffer.lineReceived(hello);
		lineBuffer.clearText();

		assertEquals("", lineBuffer.getText());
	}

	@Test
	public void testListenerCalledWhenTextUpdated() {
		String hello = "Hello";
		ITextChangeListener listener = mock(ITextChangeListener.class);

		lineBuffer.addListener(listener);
		lineBuffer.lineReceived(hello);

		verify(listener).textChanged();
	}

	@Test
	public void testListenerOnlyCalledWhenTextUpdated() {
		ITextChangeListener listener = mock(ITextChangeListener.class);

		lineBuffer.addListener(listener);

		verify(listener, never()).textChanged();
	}

	@Test
	public void testAllListenersCalledWhenTextUpdated() {
		String hello = "Hello";
		ITextChangeListener listener1 = mock(ITextChangeListener.class);
		ITextChangeListener listener2 = mock(ITextChangeListener.class);

		lineBuffer.addListener(listener1);
		lineBuffer.addListener(listener2);
		lineBuffer.lineReceived(hello);

		verify(listener1).textChanged();
		verify(listener2).textChanged();
	}

	@Test
	public void testRemovedListenersNotCalledWhenTextUpdated() throws Exception {
		String hello = "Hello";
		ITextChangeListener listener1 = mock(ITextChangeListener.class);
		ITextChangeListener listener2 = mock(ITextChangeListener.class);

		lineBuffer.addListener(listener1);
		lineBuffer.addListener(listener2);
		lineBuffer.removeListener(listener1);
		lineBuffer.lineReceived(hello);

		verify(listener1, never()).textChanged();
		verify(listener2).textChanged();
	}

}
