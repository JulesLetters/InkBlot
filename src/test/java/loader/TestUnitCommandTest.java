package loader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestUnitCommandTest {

	@Test
	public void testGetCommandTypeReturnsStringBeforeSpace() throws Exception {
		String commandType = new TestUnitCommand("hello world!").getCommandType();
		assertEquals("hello", commandType);
	}

	@Test
	public void testGetCommandTypeReturnsFormattedString() throws Exception {
		String commandType = new TestUnitCommand("teRRibLE format.").getCommandType();
		assertEquals("terrible", commandType);
	}

	@Test
	public void testGetCommandTypeReturnsEntireStringIfNoSpace() throws Exception {
		String commandType = new TestUnitCommand("the_entire_string").getCommandType();
		assertEquals("the_entire_string", commandType);
	}

}
