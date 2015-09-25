package telnet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TelnetLineWriterSingletonTest {

	@Test
	public void testGetInstanceDoesNotReturnNull() throws Exception {
		assertNotNull(new TelnetLineWriterSingleton().getInstance());
	}

	@Test
	public void testGetInstanceReturnsSameInstance() throws Exception {
		assertSame(new TelnetLineWriterSingleton().getInstance(), new TelnetLineWriterSingleton().getInstance());
	}

}
