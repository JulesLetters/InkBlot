package parser;

import static org.junit.Assert.assertFalse;
import loader.TestUnitCommand;

import org.junit.Test;

public class ExpectCommandTest {

	@Test
	public void testGetParserErrorReturnsEmptyOptional() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("Irrelevant");
		ExpectCommand expectCommand = new ExpectCommand(testUnitCommand);

		assertFalse(expectCommand.getParserError().isPresent());
	}
}
