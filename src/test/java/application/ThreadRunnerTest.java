package application;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ThreadFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ThreadRunnerTest {

	@Mock
	private ThreadFactoryFactory threadFactoryFactory;
	@Mock
	private ThreadFactory threadFactory;
	@Mock
	private Thread thread;
	@Mock
	private Runnable runnable;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createdThreadIsStarted() throws Exception {
		String threadName = "Thread Name";
		when(threadFactoryFactory.create(threadName)).thenReturn(threadFactory);
		when(threadFactory.newThread(runnable)).thenReturn(thread);

		new ThreadRunner(threadFactoryFactory).run(runnable, threadName);

		verify(thread).start();
	}
}
