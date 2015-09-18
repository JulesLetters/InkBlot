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

	@Test
	public void testGetArgumentReturnsStringAfterSpace() throws Exception {
		String commandArg = new TestUnitCommand("hello there world!").getCommandArgument();
		assertEquals("there world!", commandArg);
	}

	@Test
	public void testGetArgumentPreservesFormatting() throws Exception {
		String commandArg = new TestUnitCommand("terrible FoRmAt.").getCommandArgument();
		assertEquals("FoRmAt.", commandArg);
	}

	@Test
	public void testGetArgumentReturnsEmptyStringIfNoSpace() throws Exception {
		String commandArg = new TestUnitCommand("the_whole_thing").getCommandArgument();
		assertEquals("", commandArg);
	}

	@Test
	public void testGetArgumentReturnsEmptyStringIfSpaceIsLastCharacter() throws Exception {
		String commandArg = new TestUnitCommand("ends_with_space ").getCommandArgument();
		assertEquals("", commandArg);
	}

}
