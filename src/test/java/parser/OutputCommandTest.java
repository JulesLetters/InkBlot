package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import loader.TestUnitCommand;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import runner.CommandResult;
import runner.CommandResultListener;
import telnet.IWriteCallback;
import telnet.TelnetLineWriter;

public class OutputCommandTest {

	@Mock
	private TelnetLineWriter lineWriter;
	@Mock
	private CommandResultListener commandResultListener;
	@Captor
	private ArgumentCaptor<IWriteCallback> writeCallbackCaptor;
	@Captor
	private ArgumentCaptor<CommandResult> commandResultCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetParserErrorReturnsEmptyOptional() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("output Irrelevant");
		OutputCommand outputCommand = new OutputCommand(testUnitCommand);

		assertFalse(outputCommand.getParserError().isPresent());
	}

	@Test
	public void testExecuteWritesCommandArgumentAndSucceeds() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("output think Test");
		OutputCommand outputCommand = new OutputCommand(testUnitCommand);

		outputCommand.execute(null, lineWriter, commandResultListener);

		verify(lineWriter).write(eq("think Test"), writeCallbackCaptor.capture());
		verify(commandResultListener, never()).setStatus(commandResultCaptor.capture());

		writeCallbackCaptor.getValue().call(Optional.<Exception> empty());

		verify(commandResultListener).setStatus(commandResultCaptor.capture());

		assertEquals(CommandResult.SUCCESS, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testExecuteTriesToWriteArgumentButExceptions() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("output think Uh-Oh");
		OutputCommand outputCommand = new OutputCommand(testUnitCommand);

		outputCommand.execute(null, lineWriter, commandResultListener);

		verify(lineWriter).write(eq("think Uh-Oh"), writeCallbackCaptor.capture());
		verify(commandResultListener, never()).setStatus(commandResultCaptor.capture());

		writeCallbackCaptor.getValue().call(Optional.of(new Exception()));

		verify(commandResultListener).setStatus(commandResultCaptor.capture());

		assertEquals(CommandResult.EXCEPTION, commandResultCaptor.getValue().getStatus());
	}

	@Test
	public void testTimeoutInterruptsListenerAndSetsExceptionStatus() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("output Irrelevant");
		OutputCommand outputCommand = new OutputCommand(testUnitCommand);

		outputCommand.timeout(null, lineWriter, commandResultListener);

		InOrder inOrder = inOrder(lineWriter, commandResultListener);
		inOrder.verify(lineWriter).interrupt();
		inOrder.verify(commandResultListener).setTimeoutStatus(commandResultCaptor.capture());
		assertEquals(CommandResult.EXCEPTION, commandResultCaptor.getValue().getStatus());
	}

}
