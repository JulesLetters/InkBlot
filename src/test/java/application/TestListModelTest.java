package application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;
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
import runner.ITesterCallback;
import runner.TestResult;
import runner.TestRunner;
import events.FileLoadedEvent;
import events.TestCompletedEvent;

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
	@Captor
	private ArgumentCaptor<ITesterCallback> testerCallbackCaptor;

	private TestListModel model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new TestListModel(eventBus, parser, threadRunner, testRunner);
	}

	@Test
	public void whenParserCompletesEventIsPosted() {
		model.loadFile(file);

		verify(threadRunner).run(runnableCaptor.capture(), eq("File Parser"));
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), parserCallbackCaptor.capture());

		verifyZeroInteractions(eventBus);

		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		parserCallbackCaptor.getValue().parseCompleted(parsedTestFile);

		ArgumentCaptor<FileLoadedEvent> eventCaptor = ArgumentCaptor.forClass(FileLoadedEvent.class);
		verify(eventBus).post(eventCaptor.capture());
		assertEquals(parsedTestFile, eventCaptor.getValue().getParsedTestFile());
	}

	@Test
	public void whenTestRunnerCompletesTestSetStatusOnModel() throws Exception {
		ParsedTestUnit parsedTestUnit = mock(ParsedTestUnit.class);
		TestResult testResult = mock(TestResult.class);
		List<ParsedTestUnit> expectedTests = Collections.singletonList(parsedTestUnit);

		model.runAllTests(expectedTests);

		verifyZeroInteractions(testRunner);
		verify(threadRunner).run(runnableCaptor.capture(), eq("Test Runner"));

		runnableCaptor.getValue().run();

		verify(testRunner).runTests(eq(expectedTests), testerCallbackCaptor.capture());
		testerCallbackCaptor.getValue().testCompleted(parsedTestUnit, testResult);

		ArgumentCaptor<TestCompletedEvent> eventCaptor = ArgumentCaptor.forClass(TestCompletedEvent.class);
		verify(eventBus).post(eventCaptor.capture());
		TestCompletedEvent testCompletedEvent = eventCaptor.getValue();
		assertEquals(parsedTestUnit, testCompletedEvent.getTestUnit());
		assertEquals(testResult, testCompletedEvent.getResult());
	}

}
