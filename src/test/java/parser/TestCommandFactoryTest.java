package parser;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import loader.TestUnitCommand;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestCommandFactoryTest {

	@Mock
	private TestUnitCommand testUnitCommand;

	private TestCommandFactory testCommandFactory;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testCommandFactory = new TestCommandFactory();
	}

	@Test
	public void testOutputCommand() throws Exception {
		when(testUnitCommand.getCommandType()).thenReturn("output");
		IParsedTestCommand command = testCommandFactory.parse(testUnitCommand);
		assertTrue(command instanceof OutputCommand);
	}

	@Test
	public void testExpectCommand() throws Exception {
		when(testUnitCommand.getCommandType()).thenReturn("expect");
		IParsedTestCommand command = testCommandFactory.parse(testUnitCommand);
		assertTrue(command instanceof ExpectCommand);
	}

	@Test
	public void testInvalidCommandWhenNoMatchesAreFound() throws Exception {
		when(testUnitCommand.getCommandType()).thenReturn("unknown_command");
		IParsedTestCommand command = testCommandFactory.parse(testUnitCommand);
		assertTrue(command instanceof InvalidCommand);
	}

}
