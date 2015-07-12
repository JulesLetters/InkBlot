package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class TestCommandFactoryTest {

	private TestCommandFactory testCommandFactory;

	@Before
	public void setUp() {
		testCommandFactory = new TestCommandFactory();
	}

	@Test
	public void testOutputCommand() throws Exception {
		IParsedTestCommand command = testCommandFactory.parse("Output think Hello");

		assertTrue(command instanceof OutputCommand);
		assertEquals("Think Hello", command.getCommand().get());
		assertEquals(Optional.empty(), command.getError());
	}

	@Test
	public void testExpectCommand() throws Exception {
		IParsedTestCommand command = testCommandFactory.parse("Expect Hello");

		assertTrue(command instanceof ExpectCommand);
		assertEquals("Hello", command.getCommand().get());
		assertEquals(Optional.empty(), command.getError());
	}

	@Test
	public void testInvalidCommandWhenNoMatchesAreFound() throws Exception {
		IParsedTestCommand command = testCommandFactory.parse("UnknownCommand Arg");

		assertTrue(command instanceof InvalidCommand);
		assertEquals("Arg", command.getCommand());
		assertEquals("Invalid command.", command.getError().get());
	}

	// @Test
	// public void testName() throws Exception {
	//
	// }

}
