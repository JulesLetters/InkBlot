package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestUnit;
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
		assertEquals("Loaded", testItem.getStatus());
	}

}
