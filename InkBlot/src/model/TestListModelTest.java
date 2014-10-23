package model;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestListModelTest {

	@Mock
	private YamlWrapper yaml;
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
	public void testGetTestsReturnsListsFromFile() throws Exception {
		List<String> expectedList = Arrays.asList("A", "B");
		String filename = "Some File";
		when(fileReaderFactory.create(filename)).thenReturn(fileReader);
		when(yaml.load(fileReader)).thenReturn(expectedList);
		testListModel = new TestListModel(yaml, fileReaderFactory, filename);

		List<String> actualList = testListModel.getTests();

		assertTrue(expectedList.equals(actualList));
	}

	@Test
	public void testGetTestsIntegrationTest() throws Exception {
		List<String> expectedList = Arrays.asList("Larry", "Moe", "Curly");
		String filename = "test/Model/ThreeItems.txt";
		testListModel = new TestListModel(new YamlWrapper(), new FileReaderFactory(), filename);

		List<String> actualList = testListModel.getTests();

		assertTrue(expectedList.equals(actualList));
	}

}
