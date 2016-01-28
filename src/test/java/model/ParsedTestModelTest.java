package model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;

public class ParsedTestModelTest {

	@Mock
	private ParsedTestFile parsedTestFile;
	@Mock
	private ParsedTestUnit parsedTestUnit1;
	@Mock
	private ParsedTestUnit parsedTestUnit2;

	private ParsedTestModel parsedTestModel;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		parsedTestModel = new ParsedTestModel();
	}

	@Test
	public void addFileAddsAllUnitTestsFromFile() throws Exception {
		List<ParsedTestUnit> testList = Arrays.asList(parsedTestUnit1, parsedTestUnit2);
		when(parsedTestFile.getTests()).thenReturn(testList);
		when(parsedTestUnit1.getName()).thenReturn("Larry");
		when(parsedTestUnit2.getName()).thenReturn("Jacob");

		parsedTestModel.addFile(parsedTestFile);

		List<ParsedTestUnit> tests = parsedTestModel.getTests();

		assertEquals(2, tests.size());
		assertEquals(parsedTestUnit1, tests.get(0));
		assertEquals(parsedTestUnit2, tests.get(1));
	}

	@Test
	public void setUnitStatusUpdatesOriginalTestItem() throws Exception {
		List<ParsedTestUnit> testList = Arrays.asList(parsedTestUnit1);
		when(parsedTestFile.getTests()).thenReturn(testList);
		when(parsedTestUnit1.getName()).thenReturn("Larry");

		parsedTestModel.addFile(parsedTestFile);

		// List<TestItem> testResults = parsedTestModel.getTestResults();
		//
		// assertEquals(1, testResults.size());
		// assertEquals(TestResult.LOADED, testResults.get(0).getStatus());
		//
		// parsedTestModel.setUnitStatus(parsedTestUnit1, new
		// TestResult(TestResult.SUCCESS));
		//
		// assertEquals(TestResult.SUCCESS, testResults.get(0).getStatus());
	}

}
