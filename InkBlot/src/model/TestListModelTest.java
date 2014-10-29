package model;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.util.List;

import model.fileStructure.TestCommandText;
import model.fileStructure.TestFile;
import model.fileStructure.TestUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestListModelTest {

	@Mock
	private FileReaderFactory fileReaderFactory;
	@Mock
	private FileReader fileReader;

	private TestListModel testListModel;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetTestsIntegrationTest() throws Exception {
		String filename = "test/Model/ThreeItems.txt";
		testListModel = new TestListModel(filename);

		TestFile testFile = testListModel.getTests();

		List<TestUnit> tests = testFile.getTests();
		assertEquals(3, tests.size());
		TestUnit testUnit1 = tests.get(0);
		TestUnit testUnit2 = tests.get(1);
		TestUnit testUnit3 = tests.get(2);
		List<TestCommandText> commands1 = testUnit1.getCommands();
		List<TestCommandText> commands2 = testUnit2.getCommands();
		List<TestCommandText> commands3 = testUnit3.getCommands();

		assertEquals("CompleteExampleTest", testUnit1.getName());
		assertEquals(4, commands1.size());
		assertEquals("Command lines enclosed in quotes.", commands1.get(0).getCommandText());
		assertEquals("Delimit lines with commas.", commands1.get(1).getCommandText());
		assertEquals("Use \\n \n for newlines.", commands1.get(2).getCommandText());
		assertEquals("More documentation to follow!", commands1.get(3).getCommandText());

		assertEquals("CommaTest", testUnit2.getName());
		assertEquals(3, commands2.size());
		assertEquals("Comma , In text,\nMerged with this line", commands2.get(0).getCommandText());
		assertEquals("Comma at end", commands2.get(1).getCommandText());
		assertEquals("End", commands2.get(2).getCommandText());

		assertEquals("MultiLineTest", testUnit3.getName());
		assertEquals(2, commands3.size());
		assertEquals("MultiMatch Blah\nBlah\n\nBlah", commands3.get(0).getCommandText());
		assertEquals("Line Two", commands3.get(1).getCommandText());
	}

}
