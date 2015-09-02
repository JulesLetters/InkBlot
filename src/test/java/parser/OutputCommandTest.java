package parser;

import static org.junit.Assert.assertFalse;
import loader.TestUnitCommand;

import org.junit.Test;

public class OutputCommandTest {

	@Test
	public void testGetParserErrorReturnsEmptyOptional() throws Exception {
		TestUnitCommand testUnitCommand = new TestUnitCommand("Irrelevant");
		OutputCommand outputCommand = new OutputCommand(testUnitCommand);

		assertFalse(outputCommand.getParserError().isPresent());
	}
}
