package application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;
import java.util.List;

import model.ParsedTestModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.IParserCallback;
import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import parser.TestFileParser;
import runner.ITesterCallback;
import runner.TestResult;
import runner.TestRunner;
import view.TestItem;
import events.TestListModelUpdatedEvent;

public class TestListModelTest {

	@Mock
	private ParsedTestModel parsedTestModel;
	@Mock
	private ThreadRunner threadRunner;
	@Mock
	private TestFileParser parser;
	@Mock
	private GuavaEventBus eventBus;
	@Mock
	private TestRunner testRunner;
	@Mock
	private File file;
	@Captor
	private ArgumentCaptor<IParserCallback> parserCallbackCaptor;
	@Captor
	private ArgumentCaptor<Runnable> runnableCaptor;
	@Captor
	private ArgumentCaptor<ITesterCallback> testerCallbackCaptor;

	private TestListModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new TestListModel(parsedTestModel, eventBus, parser, threadRunner, testRunner);
	}

	@Test
	public void getTestsReturnsTestResultsFromModel() {
		List<TestItem> expectedResults = Collections.singletonList(mock(TestItem.class));
		when(parsedTestModel.getTestResults()).thenReturn(expectedResults);

		List<TestItem> actualResults = model.getTests();

		assertEquals(expectedResults, actualResults);
	}

	@Test
	public void whenParserCompletesTestNamesAreAddedToList() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);

		model.loadFile(file);

		verify(parser, never()).parse(any(File.class), any(IParserCallback.class));
		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();

		verify(parsedTestModel, never()).addFile(any(ParsedTestFile.class));
		verify(parser).parse(eq(file), parserCallbackCaptor.capture());
		parserCallbackCaptor.getValue().parseCompleted(parsedTestFile);

		verify(parsedTestModel).addFile(parsedTestFile);
	}

	@Test
	public void whenParserCompletesEventIsPosted() {
		model.loadFile(file);

		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), parserCallbackCaptor.capture());

		verifyZeroInteractions(eventBus);

		parserCallbackCaptor.getValue().parseCompleted(mock(ParsedTestFile.class));

		verify(eventBus).post(isA(TestListModelUpdatedEvent.class));
	}

	@Test
	public void whenTestRunnerCompletesTestSetStatusOnModel() throws Exception {
		ParsedTestUnit parsedTestUnit = mock(ParsedTestUnit.class);
		TestResult testResult = mock(TestResult.class);
		List<ParsedTestUnit> expectedTests = Collections.singletonList(parsedTestUnit);
		when(parsedTestModel.getTests()).thenReturn(expectedTests);

		model.runAllTests();

		verifyZeroInteractions(testRunner);
		verify(threadRunner).run(runnableCaptor.capture(), eq("Test Runner"));

		runnableCaptor.getValue().run();

		verify(testRunner).runTests(eq(expectedTests), testerCallbackCaptor.capture());
		verify(parsedTestModel, never()).setUnitStatus(any(), any());
		testerCallbackCaptor.getValue().testCompleted(parsedTestUnit, testResult);

		verify(parsedTestModel).setUnitStatus(parsedTestUnit, testResult);
	}

}
