package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import loader.TestFileUnit;
import loader.TestUnitCommand;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestUnitParserTest {

	@Mock
	private TestFileUnit testUnit;
	@Mock
	private TestUnitCommand testCommand1;
	@Mock
	private TestUnitCommand testCommand2;
	@Mock
	private IParsedTestCommand parsedCommand1;
	@Mock
	private IParsedTestCommand parsedCommand2;
	@Mock
	private TestCommandFactory testCommandFactory;

	private TestUnitParser testUnitParser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testUnitParser = new TestUnitParser(testCommandFactory);
		when(parsedCommand1.getError()).thenReturn(Optional.empty());
		when(parsedCommand2.getError()).thenReturn(Optional.empty());
	}

	@Test
	public void testNameFromUnitIsSetOnParsedUnit() throws Exception {
		when(testUnit.getCommands()).thenReturn(Arrays.asList());
		when(testUnit.getName()).thenReturn("TestUnit1-Name");

		ParsedTestUnit parsedTestUnit = testUnitParser.parse(testUnit);

		assertEquals("TestUnit1-Name", parsedTestUnit.getName());
		assertFalse(parsedTestUnit.getError().isPresent());
	}

	@Test
	public void testCommandsFromTestUnitsAreAddedToParsedUnit() throws Exception {
		when(testUnit.getName()).thenReturn("TestUnit");
		when(testUnit.getCommands()).thenReturn(Arrays.asList(testCommand1, testCommand2));
		when(testCommand1.getCommandText()).thenReturn("Command1");
		when(testCommand2.getCommandText()).thenReturn("Command2");
		when(testCommandFactory.parse("Command1")).thenReturn(parsedCommand1);
		when(testCommandFactory.parse("Command2")).thenReturn(parsedCommand2);

		ParsedTestUnit parsedTestUnit = testUnitParser.parse(testUnit);

		assertEquals("TestUnit", parsedTestUnit.getName());
		List<IParsedTestCommand> commands = parsedTestUnit.getCommands();
		assertEquals(2, commands.size());
		assertEquals(parsedCommand1, commands.get(0));
		assertEquals(parsedCommand2, commands.get(1));
		assertFalse(parsedTestUnit.getError().isPresent());
	}

	@Test
	public void testParsedUnitHasErrorWhenParserFails() throws Exception {
		when(testUnit.getName()).thenReturn("TestUnitName");
		when(testUnit.getCommands()).thenReturn(Arrays.asList(testCommand1, testCommand2));
		when(testCommand1.getCommandText()).thenReturn("Command1");
		when(testCommand2.getCommandText()).thenReturn("Command2");
		when(testCommandFactory.parse("Command1")).thenReturn(parsedCommand1);
		when(testCommandFactory.parse("Command2")).thenReturn(parsedCommand2);
		when(parsedCommand2.getError()).thenReturn(Optional.of("Error!"));

		ParsedTestUnit parsedTestUnit = testUnitParser.parse(testUnit);

		assertEquals("TestUnitName", parsedTestUnit.getName());
		List<IParsedTestCommand> commands = parsedTestUnit.getCommands();
		assertEquals(2, commands.size());
		assertEquals(parsedCommand1, commands.get(0));
		assertEquals(parsedCommand2, commands.get(1));
		assertEquals(Optional.of("Invalid Command(s)"), parsedTestUnit.getError());
	}
}
