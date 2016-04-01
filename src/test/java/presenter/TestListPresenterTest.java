package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;

import model.ParsedTestModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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

		verify(testListModel, times(2)).loadFile(fileCaptor.capture());
		assertEquals("Tests.txt", fileCaptor.getAllValues().get(0).getName());
		assertEquals("Tests2.txt", fileCaptor.getAllValues().get(1).getName());
	}

	@Test
	public void updateViewWhenFileLoads() throws Exception {
		ParsedTestFile file = mock(ParsedTestFile.class);
		TestItem testItem = mock(TestItem.class);
		when(testItemFactory.create(file)).thenReturn(testItem);

		new TestListPresenter(testListView, testListModel, parsedTestModel, eventBus, testItemFactory);

		eventBus.post(new FileLoadedEvent(file));

		verify(testListView).addAllItems(Collections.singletonList(testItem));
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
