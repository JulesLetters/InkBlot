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

import events.TestListModelUpdatedEvent;

public class TestListModelTest {

	@Mock
	private ThreadRunner runner;
	@Mock
	private TestFileParser parser;
	@Mock
	private GuavaEventBus eventBus;
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
		model = new TestListModel(parser, runner, eventBus);
	}

	@Test
	public void testNoFilesLoaded() {
		assertEquals(Collections.EMPTY_LIST, model.getTestNames());
	}

	@Test
	public void testOnLoadFileSubmitRunnableToThreadRunner() {
		verify(runner, never()).run(any(Runnable.class));

		model.loadFile(file);

		verify(runner).run(runnableCaptor.capture());
	}

	@Test
	public void testWhenParserCompletesTestNameIsAddedToList() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);
		List<String> expectedTestNames = Arrays.asList("Larry", "Moe", "Curly");
		when(parsedTestFile.getTestNames()).thenReturn(expectedTestNames);

		model.loadFile(file);
		verify(parser, never()).parse(any(File.class), any(IParserCallback.class));

		verify(runner).run(runnableCaptor.capture());
		runnableCaptor.getValue().run();

		verify(parser).parse(eq(file), callbackCaptor.capture());
		callbackCaptor.getValue().parseCompleted(parsedTestFile);

		List<String> actualTestNames = model.getTestNames();
		assertEquals(expectedTestNames, actualTestNames);
	}

	@Test
	public void testEventIsPostedWhenParserCompletesListenerIsCalled() {
		ParsedTestFile parsedTestFile = mock(ParsedTestFile.class);

		model.loadFile(file);
		verify(runner).run(runnableCaptor.capture());
		runnableCaptor.getValue().run();
		verify(parser).parse(eq(file), callbackCaptor.capture());

		verifyZeroInteractions(eventBus);
		callbackCaptor.getValue().parseCompleted(parsedTestFile);
		verify(eventBus).post(isA(TestListModelUpdatedEvent.class));
	}

}
