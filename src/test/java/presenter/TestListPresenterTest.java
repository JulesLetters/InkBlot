package presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;
import view.TestListView;
import application.GuavaEventBus;
import application.IEventBus;
import application.TestListModel;
import events.FileLoadedEvent;
import events.RunButtonClicked;
import events.TestCompletedEvent;

public class TestListPresenterTest {

	@Mock
	private TestListView testListView;
	@Mock
	private TestListModel testListModel;
	@Mock
	private TestItemFactory testItemFactory;

	private IEventBus eventBus = new GuavaEventBus();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void updateViewWhenFileLoads() throws Exception {
		ParsedTestFile file = mock(ParsedTestFile.class);
		TestItem testItem = mock(TestItem.class);
		when(testItemFactory.create(file)).thenReturn(testItem);

		new TestListPresenter(testListView, testListModel, eventBus, testItemFactory);

		eventBus.post(new FileLoadedEvent(file));

		verify(testListView).addAllItems(Collections.singletonList(testItem));
	}

	@Test
	public void testWhenRunButtonClickedMakeModelRunTests() throws Exception {
		new TestListPresenter(testListView, testListModel, eventBus, testItemFactory);
		verify(testListModel, never()).runAllTests(any());
		List<ParsedTestUnit> expectedTestList = Collections.singletonList(mock(ParsedTestUnit.class));
		when(testItemFactory.getTests()).thenReturn(expectedTestList);

		eventBus.post(new RunButtonClicked());

		verify(testListModel).runAllTests(expectedTestList);
	}

	@Test
	public void onTestCompletionUpdateViewItem() throws Exception {
		new TestListPresenter(testListView, testListModel, eventBus, testItemFactory);
		ParsedTestUnit unit = mock(ParsedTestUnit.class);
		verify(testItemFactory, never()).setUnitStatus(any(), any());

		eventBus.post(new TestCompletedEvent(unit, new TestResult(TestResult.SUCCESS)));

		verify(testItemFactory).setUnitStatus(unit, TestResult.SUCCESS);
	}

}
