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
	private ArgumentCaptor<IParserCallback> callbackCaptor;
	@Captor
	private ArgumentCaptor<Runnable> runnableCaptor;

	private TestListModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new TestListModel(parser, testRunner, threadRunner, eventBus);
	}

	@Test
	public void testNoFilesLoaded() {
		assertEquals(Collections.EMPTY_LIST, model.getTestNames());
	}

	@Test
	public void testOnLoadFileSubmitRunnableToThreadRunner() {
		verify(threadRunner, never()).run(any(Runnable.class));

		model.loadFile(file);

		verify(threadRunner).run(runnableCaptor.capture());
	}

	@Test
	public void testWhenParserCompletesTestNamesAreAddedToList() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		ParsedTestUnit parsedTestUnit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit parsedTestUnit2 = mock(ParsedTestUnit.class);
		when(parsedTestFile.getTests()).thenReturn(Arrays.asList(parsedTestUnit1, parsedTestUnit2));
		when(parsedTestUnit1.getName()).thenReturn("Larry");
		when(parsedTestUnit2.getName()).thenReturn("Moe");
		List<String> expectedTestNames = Arrays.asList("Larry", "Moe");

		model.loadFile(file);
		verify(parser, never()).parse(any(File.class), any(IParserCallback.class));

		verify(threadRunner).run(runnableCaptor.capture());
		runnableCaptor.getValue().run();

		verify(parser).parse(eq(file), callbackCaptor.capture());
		callbackCaptor.getValue().parseCompleted(parsedTestFile);

		List<String> actualTestNames = model.getTestNames();
		assertEquals(expectedTestNames, actualTestNames);
	}

	@Test
	public void testWhenParserCompletesEventIsPosted() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);

		model.loadFile(file);
		verify(threadRunner).run(runnableCaptor.capture());
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), callbackCaptor.capture());

		verifyZeroInteractions(eventBus);
		callbackCaptor.getValue().parseCompleted(parsedTestFile);
		verify(eventBus).post(isA(TestListModelUpdatedEvent.class));
	}

	@Test
	public void testWhenParserCompletesParsedTestsAreAddedToTestRunner() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		ParsedTestUnit parsedTestUnit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit parsedTestUnit2 = mock(ParsedTestUnit.class);
		when(parsedTestFile.getTests()).thenReturn(Arrays.asList(parsedTestUnit1, parsedTestUnit2));

		model.loadFile(file);
		verify(threadRunner).run(runnableCaptor.capture());
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), callbackCaptor.capture());

		verifyZeroInteractions(testRunner);
		callbackCaptor.getValue().parseCompleted(parsedTestFile);
		verify(testRunner).addParsedUnits(Arrays.asList(parsedTestUnit1, parsedTestUnit2));
	}

	@Test
	public void testRunAllTestsCallsTestRunner() throws Exception {
		model.runAllTests();

		verify(testRunner, never()).runTests();
		verify(threadRunner).run(runnableCaptor.capture());

		runnableCaptor.getValue().run();

		verify(testRunner).runTests();
	}

}
