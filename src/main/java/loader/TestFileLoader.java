package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestFileLoader {

	private YamlWrapper yaml;
	private FileReaderFactory fileReaderFactory;

	public TestFileLoader() {
		this(new YamlFactory().create(), new FileReaderFactory());
	}

	protected TestFileLoader(YamlWrapper yaml, FileReaderFactory fileReaderFactory) {
		this.yaml = yaml;
		this.fileReaderFactory = fileReaderFactory;
	}

	public TestFile loadTestfile(File file) throws FileNotFoundException {
		FileReader fileReader = fileReaderFactory.create(file);
		return (TestFile) yaml.load(fileReader);
	}
}
