package telnet;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.eventbus.EventBus;

import events.EnterPressedEvent;

public class InputTextModelTest {

	@Mock
	private TelnetLineWriter telnetLineWriter;
	@Captor
	private ArgumentCaptor<IWriteCallback> writeCallbackCaptor;

	private EventBus eventBus = new EventBus();
	private InputTextModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new InputTextModel(telnetLineWriter);
	}

	@Test
	public void testOnEnterPressedSendToTelnet() throws IOException {
		eventBus.register(model);
		String text = "Hello";
		verifyZeroInteractions(telnetLineWriter);

		eventBus.post(new EnterPressedEvent(text));

		verify(telnetLineWriter).write(eq(text), writeCallbackCaptor.capture());
	}

}
