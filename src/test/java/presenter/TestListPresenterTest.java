package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.ParsedTestModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;
import view.TestListView;
import application.GuavaEventBus;
import application.IEventBus;
import application.TestListModel;
import events.RunButtonClicked;
import events.TestCompletedEvent;
import events.FileLoadedEvent;

public class TestListPresenterTest {

	@Mock
	private TestListView testListView;
	@Mock
	private TestListModel testListModel;
	@Mock
	private ParsedTestModel parsedTestModel;
	@Mock
	private TestItemFactory testItemFactory;

	private IEventBus eventBus = new GuavaEventBus();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void onConstructionModelLoadsFile() {
		ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);

		verify(testListModel).loadFile(fileCaptor.capture());
		assertEquals("Tests.txt", fileCaptor.getValue().getName());
	}

	@Test
	public void testEventFromModelUpdatesView() throws Exception {
		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		ParsedTestUnit unit2 = mock(ParsedTestUnit.class);
		when(parsedTestModel.getTests()).thenReturn(Arrays.asList(unit1, unit2));
		TestItem testItem1 = mock(TestItem.class);
		TestItem testItem2 = mock(TestItem.class);
		when(testItemFactory.create(unit1)).thenReturn(testItem1);
		when(testItemFactory.create(unit2)).thenReturn(testItem2);

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);

		eventBus.post(new FileLoadedEvent());

		verify(testListView).setInput(Arrays.asList(testItem1, testItem2));
	}

	@Test
	public void emptyModelSendsEmptyCollectionToView() throws Exception {
		when(parsedTestModel.getTests()).thenReturn(Collections.emptyList());

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);

		eventBus.post(new FileLoadedEvent());

		verify(testListView).setInput(Collections.emptyList());
	}

	@Test
	public void testListenForFileLoadedBeforeLoadingFile() throws Exception {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				eventBus.post(new FileLoadedEvent());
				return null;
			}
		}).when(testListModel).loadFile(any(File.class));

		ParsedTestUnit unit1 = mock(ParsedTestUnit.class);
		List<ParsedTestUnit> expectedItems = Collections.singletonList(unit1);
		when(parsedTestModel.getTests()).thenReturn(expectedItems);
		TestItem testItem1 = mock(TestItem.class);
		when(testItemFactory.create(unit1)).thenReturn(testItem1);

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);

		verify(testListView).setInput(Arrays.asList(testItem1));
	}

	@Test
	public void testWhenRunButtonClickedMakeModelRunTests() throws Exception {
		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);
		verify(testListModel, never()).runAllTests();

		eventBus.post(new RunButtonClicked());

		verify(testListModel).runAllTests();
	}

	@Test
	public void onTestCompletionUpdateViewItem() throws Exception {
		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		when(parsedTestModel.getUnitStatus(unit)).thenReturn(TestResult.SUCCESS);
		verify(testItemFactory, never()).setUnitStatus(any(), any());

		eventBus.post(new TestCompletedEvent(unit));

		verify(testItemFactory).setUnitStatus(unit, TestResult.SUCCESS);
	}

}
