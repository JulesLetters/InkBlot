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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import runner.TestRunner;
import view.TestItem;
import events.TestListModelUpdatedEvent;

public class TestListModelTest {

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

	private TestListModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new TestListModel(eventBus, parser, threadRunner, testRunner);
	}

	@Test
	public void testNoFilesLoaded() {
		assertEquals(Collections.EMPTY_LIST, model.getTests());
	}

	@Test
	public void testOnLoadFileSubmitRunnableToThreadRunner() {
		verify(threadRunner, never()).run(any(Runnable.class), any());

		model.loadFile(file);

		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
	}

	@Test
	public void testWhenParserCompletesTestNamesAreAddedToList() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		ParsedTestUnit parsedTestUnit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit parsedTestUnit2 = mock(ParsedTestUnit.class);
		when(parsedTestFile.getTests()).thenReturn(Arrays.asList(parsedTestUnit1, parsedTestUnit2));
		when(parsedTestUnit1.getName()).thenReturn("Larry");
		when(parsedTestUnit2.getName()).thenReturn("Moe");

		model.loadFile(file);

		verify(parser, never()).parse(any(File.class), any(IParserCallback.class));
		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();

		verify(parser).parse(eq(file), parserCallbackCaptor.capture());
		IParserCallback value = parserCallbackCaptor.getValue();
		value.parseCompleted(parsedTestFile);

		List<TestItem> tests = model.getTests();

		assertEquals(2, tests.size());
		assertEquals("Larry", tests.get(0).getName());
		assertEquals("Moe", tests.get(1).getName());
	}

	@Test
	public void testWhenParserCompletesEventIsPosted() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);

		model.loadFile(file);
		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), parserCallbackCaptor.capture());

		verifyZeroInteractions(eventBus);
		parserCallbackCaptor.getValue().parseCompleted(parsedTestFile);
		verify(eventBus).post(isA(TestListModelUpdatedEvent.class));
	}

	@Test
	public void testRunAllTestsCallsTestRunnerWithParsedTests() throws Exception {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		ParsedTestUnit parsedTestUnit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit parsedTestUnit2 = mock(ParsedTestUnit.class);
		List<ParsedTestUnit> expectedTests = Arrays.asList(parsedTestUnit1, parsedTestUnit2);
		when(parsedTestFile.getTests()).thenReturn(expectedTests);

		model.loadFile(file);
		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), parserCallbackCaptor.capture());
		parserCallbackCaptor.getValue().parseCompleted(parsedTestFile);

		model.runAllTests();

		verify(testRunner, never()).runTests(any());
		verify(threadRunner).run(runnableCaptor.capture(), eq("Test Runner"));

		runnableCaptor.getValue().run();

		verify(testRunner).runTests(expectedTests);
	}
}
