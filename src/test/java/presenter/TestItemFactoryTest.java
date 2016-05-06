package presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class TestItemFactoryTest {

	@Mock
	private ParsedTestFile file;

	TestItemFactory testObject;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		File actualFile = mock(File.class);
		when(file.getFile()).thenReturn(actualFile);
		when(file.getError()).thenReturn(Optional.empty());
		when(actualFile.getName()).thenReturn("Tests.yaml");

		testObject = new TestItemFactory();
	}

	@Test
	public void createWithOneUnit() throws Exception {
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Collections.singletonList(unit));
		when(unit.getName()).thenReturn("This is a test name");

		TestItem testItem = testObject.create(file);

		assertEquals("Tests.yaml", testItem.getName());
		List<TestItem> children = testItem.getChildren();
		assertEquals(1, children.size());
		assertEquals("This is a test name", children.get(0).getName());
		assertEquals(TestResult.LOADED, children.get(0).getStatus());
	}

	@Test
	public void createWithMultipleUnits() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Arrays.asList(unit1, unit2));
		when(unit1.getName()).thenReturn("First test");
		when(unit2.getName()).thenReturn("Second test");

		TestItem testItem = testObject.create(file);

		List<TestItem> children = testItem.getChildren();
		assertEquals(2, children.size());
		assertEquals("First test", children.get(0).getName());
		assertEquals(TestResult.LOADED, children.get(0).getStatus());
		assertEquals("Second test", children.get(1).getName());
		assertEquals(TestResult.LOADED, children.get(1).getStatus());
	}

	@Test
	public void setUnitStatusAltersRetainedTestItem() throws Exception {
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Collections.singletonList(unit));
		TestItem testItem = testObject.create(file);

		testObject.setUnitStatus(unit, TestResult.FAILURE);

		List<TestItem> children = testItem.getChildren();
		assertEquals(1, children.size());
		assertEquals(TestResult.FAILURE, children.get(0).getStatus());
	}

	@Test
	public void setUnitStatusWorksForMultipleUnits() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Arrays.asList(unit1, unit2));

		TestItem testItem = testObject.create(file);

		testObject.setUnitStatus(unit1, TestResult.EXCEPTION);
		testObject.setUnitStatus(unit2, TestResult.SUCCESS);

		List<TestItem> children = testItem.getChildren();
		assertEquals(2, children.size());
		assertEquals(TestResult.EXCEPTION, children.get(0).getStatus());
		assertEquals(TestResult.SUCCESS, children.get(1).getStatus());
	}

	@Test
	public void getTestsReturnsAllIngestedUnits() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Arrays.asList(unit1, unit2));

		testObject.create(file);
		List<ParsedTestUnit> actualTests = testObject.getTests();

		List<ParsedTestUnit> expectedTests = Arrays.asList(unit1, unit2);
		assertEquals(expectedTests, actualTests);
	}

	@Test
	public void getTestsReturnsAllIngestedUnitsFromMultipleFiles() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);
		ParsedTestFile file1 = mock(ParsedTestFile.class);
		ParsedTestFile file2 = mock(ParsedTestFile.class);
		when(file1.getTests()).thenReturn(Arrays.asList(unit1));
		when(file2.getTests()).thenReturn(Arrays.asList(unit2));
		when(file1.getFile()).thenReturn(mock(File.class));
		when(file2.getFile()).thenReturn(mock(File.class));
		when(file1.getError()).thenReturn(Optional.empty());
		when(file2.getError()).thenReturn(Optional.empty());

		testObject.create(file1);
		testObject.create(file2);
		List<ParsedTestUnit> actualTests = testObject.getTests();

		List<ParsedTestUnit> expectedTests = Arrays.asList(unit1, unit2);
		assertEquals(expectedTests, actualTests);
	}

	@Test
	public void getTestsReturnsCopiesOfList() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Arrays.asList(unit1));

		testObject.create(file);
		List<ParsedTestUnit> copy1 = testObject.getTests();
		List<ParsedTestUnit> copy2 = testObject.getTests();

		List<ParsedTestUnit> expectedTests = Collections.singletonList(unit1);
		assertEquals(expectedTests, copy1);
		assertEquals(expectedTests, copy2);
		copy1.clear();
		assertTrue(copy1.isEmpty());
		assertEquals(expectedTests, copy2);
	}

	@Test
	public void validFileShowsLoadedStatus() throws Exception {
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Collections.singletonList(unit));
		when(unit.getName()).thenReturn("This is a test name");

		TestItem testItem = testObject.create(file);

		assertEquals("FileLoaded", testItem.getStatus());
	}

	@Test
	public void fileWithNoTestsShowsInvalidStatus() throws Exception {
		when(file.getTests()).thenReturn(Collections.emptyList());

		TestItem testItem = testObject.create(file);

		assertEquals("Tests.yaml", testItem.getName());
		assertEquals("FileInvalid", testItem.getStatus());
		List<TestItem> children = testItem.getChildren();
		assertEquals(0, children.size());
	}

	@Test
	public void fileWithErrorShowsExceptionStatus() throws Exception {
		when(file.getError()).thenReturn(Optional.of("Turbo Fail."));

		TestItem testItem = testObject.create(file);

		assertEquals("Tests.yaml", testItem.getName());
		assertEquals("FileException", testItem.getStatus());
		List<TestItem> children = testItem.getChildren();
		assertEquals(0, children.size());
	}
}
