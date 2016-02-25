package model;

import static org.junit.Assert.assertEquals;
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
	public void addFileAddsAllUnitTestsFromFileInOrder() throws Exception {
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
	public void getTestsReturnsCopy() throws Exception {
		List<ParsedTestUnit> testList = Collections.singletonList(parsedTestUnit1);
		when(parsedTestFile.getTests()).thenReturn(testList);
		when(parsedTestUnit1.getName()).thenReturn("Larry");

		parsedTestModel.addFile(parsedTestFile);

		List<ParsedTestUnit> tests1 = parsedTestModel.getTests();
		List<ParsedTestUnit> tests2 = parsedTestModel.getTests();

		assertEquals(1, tests1.size());
		assertEquals(1, tests2.size());
		tests1.clear();
		assertEquals(1, tests2.size());
	}

	@Test
	public void unitStatusDefaultsToLoaded() throws Exception {
		List<ParsedTestUnit> testList = Collections.singletonList(parsedTestUnit1);
		when(parsedTestFile.getTests()).thenReturn(testList);

		parsedTestModel.addFile(parsedTestFile);
		String status = parsedTestModel.getUnitStatus(parsedTestUnit1);

		assertEquals(TestResult.LOADED, status);
	}

	@Test
	public void setUnitStatusChangesStatus() throws Exception {
		List<ParsedTestUnit> testList = Collections.singletonList(parsedTestUnit1);
		when(parsedTestFile.getTests()).thenReturn(testList);

		parsedTestModel.addFile(parsedTestFile);
		parsedTestModel.setUnitStatus(parsedTestUnit1, new TestResult(TestResult.SUCCESS));

		String status = parsedTestModel.getUnitStatus(parsedTestUnit1);
		assertEquals(TestResult.SUCCESS, status);
	}

	@Test
	public void unitStatusesAreSeparate() throws Exception {
		List<ParsedTestUnit> testList = Arrays.asList(parsedTestUnit1, parsedTestUnit2);
		when(parsedTestFile.getTests()).thenReturn(testList);

		parsedTestModel.addFile(parsedTestFile);
		parsedTestModel.setUnitStatus(parsedTestUnit1, new TestResult(TestResult.FAILURE));

		assertEquals(TestResult.FAILURE, parsedTestModel.getUnitStatus(parsedTestUnit1));
		assertEquals(TestResult.LOADED, parsedTestModel.getUnitStatus(parsedTestUnit2));
	}

}
