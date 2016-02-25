package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class TestItemFactoryTest {

	@Mock
	private ParsedTestUnit unit;

	TestItemFactory testObject;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testObject = new TestItemFactory();
	}

	@Test
	public void testCreate() throws Exception {
		when(unit.getName()).thenReturn("This is a test name");

		TestItem testItem = testObject.create(unit);

		assertEquals("This is a test name", testItem.getName());
		assertEquals(TestResult.LOADED, testItem.getStatus());
	}

	@Test
	public void setUnitStatusAltersRetainedTestItem() throws Exception {
		TestItem testItem = testObject.create(unit);

		testObject.setUnitStatus(unit, TestResult.FAILURE);

		assertEquals(TestResult.FAILURE, testItem.getStatus());
	}

	@Test
	public void setUnitStatusWorksForMultipleUnits() throws Exception {
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);

		TestItem testItem1 = testObject.create(unit);
		TestItem testItem2 = testObject.create(unit2);

		testObject.setUnitStatus(unit, TestResult.EXCEPTION);
		testObject.setUnitStatus(unit2, TestResult.SUCCESS);

		assertEquals(TestResult.EXCEPTION, testItem1.getStatus());
		assertEquals(TestResult.SUCCESS, testItem2.getStatus());
	}

}
