package application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;

public class ThreadFactoryFactoryTest {

	@Test
	public void createdThreadFactoryUsesGivenProperties() throws Exception {
		String threadName = "Thread Name";
		ThreadFactory factory = new ThreadFactoryFactory().create(threadName);

		Thread newThread1 = factory.newThread(null);
		assertTrue(newThread1.isDaemon());
		assertEquals(threadName, newThread1.getName());

		Thread newThread2 = factory.newThread(null);
		assertTrue(newThread2.isDaemon());
		assertEquals(threadName, newThread2.getName());
	}

	private CountDownLatch threadIsRunningLatch = new CountDownLatch(1);
	private CountDownLatch keepThreadAlivelatch = new CountDownLatch(1);

	private Runnable latchWaiter = () -> {
		try {
			threadIsRunningLatch.countDown();
			keepThreadAlivelatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	};

	@Test
	public void newThreadFromFactoryUsesGivenRunnable() throws Exception {
		String threadName = "Thread Name";
		ThreadFactory factory = new ThreadFactoryFactory().create(threadName);
		Thread thread = factory.newThread(latchWaiter);

		thread.start();

		threadIsRunningLatch.await();
		assertTrue(thread.isAlive());
		keepThreadAlivelatch.countDown();
	}

}
