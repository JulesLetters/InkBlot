package parser;

import static org.junit.Assert.assertEquals;
import loader.TestUnitCommand;

import org.junit.Test;

public class InvalidCommandTest {

	@Test
	public void testGetParserErrorReturnsInvalidCommandMessage() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("Irrelevant");
		InvalidCommand invalidCommand = new InvalidCommand(testUnitCommand);

		assertEquals("Invalid Command", invalidCommand.getParserError().get());
	}
}
