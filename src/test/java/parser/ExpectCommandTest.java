package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import loader.TestUnitCommand;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import runner.CommandResult;
import runner.CommandResultListener;
import telnet.ITextChangeListener;
import telnet.LineBuffer;

public class ExpectCommandTest {

	@Mock
	private LineBuffer lineBuffer;
	@Mock
	private CommandResultListener listener;
	@Captor
	private ArgumentCaptor<CommandResult> commandResultCaptor;
	@Captor
	private ArgumentCaptor<ITextChangeListener> textListenerCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetParserErrorReturnsEmptyOptional() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("expect Irrelevant");
		ExpectCommand expectCommand = new ExpectCommand(testUnitCommand);

		assertFalse(expectCommand.getParserError().isPresent());
	}

	@Test
	public void testWhenArgumentFoundInBufferSetSuccessOnListener() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect abc");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("abc");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener).setStatus(commandResultCaptor.capture());
		assertEquals(CommandResult.SUCCESS, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testWhenArgumentNotFoundInBufferDoNotSetSuccessOnListener() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect abc");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("xyz");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener, never()).setStatus(commandResultCaptor.capture());
	}

	@Test
	public void testWhenArgumentFoundAnywhereInBufferSetSuccessOnListener() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect ABC");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("XYZ\nABC\nQRS");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener).setStatus(commandResultCaptor.capture());
		assertEquals(CommandResult.SUCCESS, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testWhenWholeArgumentNotFoundAnywhereDoNotSetSuccess() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect ABC");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("XYZ\nABCD\nEABC\nQRS");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener, never()).setStatus(commandResultCaptor.capture());
	}

	@Test
	public void testWhenLineBufferUpdatesCheckForMatch() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect ABC");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener, never()).setStatus(commandResultCaptor.capture());
		verify(lineBuffer).addListener(textListenerCaptor.capture());
		ITextChangeListener textChangeListener = textListenerCaptor.getValue();
		when(lineBuffer.getText()).thenReturn("XYZ\nABC\nQRS");

		textChangeListener.textChanged();

		verify(listener).setStatus(commandResultCaptor.capture());
		assertEquals(CommandResult.SUCCESS, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testWhenLineBufferUpdatesDoNothingIfNoMatch() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect ABC");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("");

		expectCommand.execute(lineBuffer, null, listener);

		verify(listener, never()).setStatus(commandResultCaptor.capture());
		verify(lineBuffer).addListener(textListenerCaptor.capture());
		ITextChangeListener textChangeListener = textListenerCaptor.getValue();
		when(lineBuffer.getText()).thenReturn("XYZ\nABCD\nEABC\nQRS");

		textChangeListener.textChanged();

		verify(listener, never()).setStatus(commandResultCaptor.capture());
	}

	@Test
	public void testOnTimeoutSetTimeoutStatusToFailure() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect Irrelevant");
		ExpectCommand expectCommand = new ExpectCommand(command);

		expectCommand.timeout(lineBuffer, null, listener);

		verify(listener).setTimeoutStatus(commandResultCaptor.capture());
		assertEquals(CommandResult.FAILURE, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testOnStopRemoveListenerFromBuffer() throws Exception {
		TestUnitCommand command = new TestUnitCommand("expect Irrelevant");
		ExpectCommand expectCommand = new ExpectCommand(command);
		when(lineBuffer.getText()).thenReturn("");

		expectCommand.execute(lineBuffer, null, listener);

		verify(lineBuffer).addListener(textListenerCaptor.capture());

		expectCommand.stop(lineBuffer, null, listener);

		verify(lineBuffer).removeListener(textListenerCaptor.getValue());
	}

}
