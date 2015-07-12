package presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import view.TestListView;
import application.GuavaEventBus;
import application.IEventBus;
import application.TestListModel;
import events.RunButtonClicked;
import events.TestListModelUpdatedEvent;

public class TestListPresenterTest {

	@Mock
	private TestListView testListView;
	@Mock
	private TestListModel testListModel;

	private IEventBus eventBus = new GuavaEventBus();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void onConstructionModelLoadsFile() {
		ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);

		new TestListPresenter(testListView, testListModel, eventBus);

		verify(testListModel).loadFile(fileCaptor.capture());
		assertEquals("Tests.txt", fileCaptor.getValue().getName());
	}

	@Test
	public void testEventFromModelUpdatesView() throws Exception {
		List<String> expectedList = Arrays.asList("A", "B", "C");
		when(testListModel.getTestNames()).thenReturn(expectedList);
		new TestListPresenter(testListView, testListModel, eventBus);

		eventBus.post(new TestListModelUpdatedEvent());

		verify(testListView).setInput(expectedList);
	}

	@Test
	public void testListenForFileLoadedBeforeLoadingFile() throws Exception {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				eventBus.post(new TestListModelUpdatedEvent());
				return null;
			}
		}).when(testListModel).loadFile(any(File.class));

		List<String> expectedList = Arrays.asList("D", "E", "F");
		when(testListModel.getTestNames()).thenReturn(expectedList);

		new TestListPresenter(testListView, testListModel, eventBus);

		verify(testListView).setInput(expectedList);
	}

	@Test
	public void testWhenRunButtonClickedMakeModelRunTests() throws Exception {
		new TestListPresenter(testListView, testListModel, eventBus);
		verify(testListModel, never()).runAllTests();

		eventBus.post(new RunButtonClicked());

		verify(testListModel).runAllTests();
	}

}
