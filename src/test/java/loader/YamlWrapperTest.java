package loader;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileReader;

import loader.YamlWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yaml.snakeyaml.Yaml;

public class YamlWrapperTest {

	@Mock
	private Yaml yaml;
	@Mock
	private FileReader fileReader;

	private YamlWrapper yamlWrapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		yamlWrapper = new YamlWrapper(yaml);
	}

	@Test
	public void testLoadFromFileReader() throws Exception {
		Object expectedObject = mock(Object.class);
		when(yaml.load(fileReader)).thenReturn(expectedObject);

		Object actualObject = yamlWrapper.load(fileReader);

		assertSame(expectedObject, actualObject);
	}

}
