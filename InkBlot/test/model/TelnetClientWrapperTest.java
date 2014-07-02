package model;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetInputListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TelnetClientWrapperTest {

	@Mock
	private TelnetClient telnetClient;
	@Mock
	private TelnetInputListener listener;

	private TelnetClientWrapper telnetClientWrapper;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		telnetClientWrapper = new TelnetClientWrapper(telnetClient);
	}

	@Test
	public void testConnectCallsConnect() throws Exception {
		String expectedHost = "x.x.x.x";
		int expectedPort = 9999;

		telnetClientWrapper.connect(expectedHost, expectedPort);

		verify(telnetClient).connect(expectedHost, expectedPort);
	}

	@Test
	public void testDisconnectCallsDisconnect() throws Exception {

		telnetClientWrapper.disconnect();

		verify(telnetClient).disconnect();
	}

	@Test
	public void testRegisterInputListenerRegistersWithTelnetClient()
			throws Exception {

		telnetClientWrapper.registerInputListener(listener);

		verify(telnetClient).registerInputListener(listener);
	}

	@Test
	public void getInputStreamReturnsInputStreamFromTelnetClient()
			throws Exception {
		InputStream expectedStream = mock(InputStream.class);
		when(telnetClient.getInputStream()).thenReturn(expectedStream);

		InputStream actualStream = telnetClientWrapper.getInputStream();

		assertSame(expectedStream, actualStream);
	}

}
