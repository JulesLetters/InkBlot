package loader;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestFileLoaderTest {

	@Test
	public void loadYamlIntegrationTest() throws Exception {
		List<String> expectedTestList = Arrays.asList("CompleteExampleTest", "CommaTest", "MultiLineTest");
		List<String> expectedCommands1 = Arrays.asList("Command lines enclosed in quotes.",
				"Delimit lines with commas.", "Use \\n \n for newlines.",
				"More documentation to follow when I make commands!");
		List<String> expectedCommands2 = Arrays
				.asList("Comma , In text,\nMerged with this line", "Comma at end", "End");
		List<String> expectedCommands3 = Arrays.asList("MultiMatch Blah\nBlah\n\nBlah", "Line Two");

		TestFileLoader testFileLoader = new TestFileLoader();
		URL fileUrl = getClass().getResource("Tests.txt");
		File file = new File(fileUrl.toURI());

		TestFile testFile = testFileLoader.loadTestfile(file);

		List<TestFileUnit> actualTestList = testFile.getTests();
		assertEquals(3, actualTestList.size());
		assertEquals(expectedTestList.get(0), actualTestList.get(0).getName());
		assertEquals(expectedTestList.get(1), actualTestList.get(1).getName());
		assertEquals(expectedTestList.get(2), actualTestList.get(2).getName());

		List<TestUnitCommand> actualCommands1 = actualTestList.get(0).getCommands();
		assertEquals(4, actualCommands1.size());
		assertEquals(expectedCommands1.get(0), actualCommands1.get(0).getCommandText());
		assertEquals(expectedCommands1.get(1), actualCommands1.get(1).getCommandText());
		assertEquals(expectedCommands1.get(2), actualCommands1.get(2).getCommandText());
		assertEquals(expectedCommands1.get(3), actualCommands1.get(3).getCommandText());

		List<TestUnitCommand> actualCommands2 = actualTestList.get(1).getCommands();
		assertEquals(3, actualCommands2.size());
		assertEquals(expectedCommands2.get(0), actualCommands2.get(0).getCommandText());
		assertEquals(expectedCommands2.get(1), actualCommands2.get(1).getCommandText());
		assertEquals(expectedCommands2.get(2), actualCommands2.get(2).getCommandText());

		List<TestUnitCommand> actualCommands3 = actualTestList.get(2).getCommands();
		assertEquals(2, actualCommands3.size());
		assertEquals(expectedCommands3.get(0), actualCommands3.get(0).getCommandText());
		assertEquals(expectedCommands3.get(1), actualCommands3.get(1).getCommandText());
	}

}
