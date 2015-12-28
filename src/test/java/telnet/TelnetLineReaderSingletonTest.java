package telnet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TelnetLineReaderSingletonTest {

	@Test
	public void testGetInstanceDoesNotReturnNull() throws Exception {
		assertNotNull(new TelnetLineReaderSingleton().getInstance());
	}

	@Test
	public void testGetInstanceReturnsSameInstance() throws Exception {
		assertSame(new TelnetLineReaderSingleton().getInstance(), new TelnetLineReaderSingleton().getInstance());
	}
}
