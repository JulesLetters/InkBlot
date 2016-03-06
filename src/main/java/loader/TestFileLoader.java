package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestFileLoader {

	private YamlWrapper yaml;

	public TestFileLoader() {
		this.yaml = new YamlFactory().create();
	}

	public synchronized TestFile loadTestfile(File file) throws FileNotFoundException {
		return (TestFile) yaml.load(new FileReader(file));
	}

}
