package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		testObject = new TestItemFactory();
	}

	@Test
	public void createWithOneUnit() throws Exception {
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		when(file.getTests()).thenReturn(Collections.singletonList(unit));
		when(unit.getName()).thenReturn("This is a test name");

		TestItem testItem = testObject.create(file);

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

}
