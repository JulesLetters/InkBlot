package model;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.eventbus.EventBus;

import events.EnterPressedEvent;

public class InputTextModelTest {

	@Mock
	private TelnetClientFactory telnetClientFactory;
	@Mock
	private TelnetClientWrapper telnetClient;
	@Mock
	private OutputStream outputStream;

	private EventBus eventBus = new EventBus();
	private InputTextModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(telnetClientFactory.getInstance()).thenReturn(telnetClient);
		when(telnetClient.getOutputStream()).thenReturn(outputStream);
		model = new InputTextModel(telnetClientFactory);
	}

	@Test
	public void testOnEnterPressedSendToTelnet() throws IOException {
		eventBus.register(model);
		String text = "Hello";
		String textWritten = text + "\n";

		verifyZeroInteractions(outputStream);

		eventBus.post(new EnterPressedEvent(text));

		InOrder inOrder = Mockito.inOrder(outputStream);
		inOrder.verify(outputStream).write(textWritten.getBytes());
		inOrder.verify(outputStream).flush();
	}
}
