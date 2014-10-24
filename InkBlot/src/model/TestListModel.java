package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;

public class TestListModel {

	private YamlWrapper yaml;
	private FileReaderFactory fileReaderFactory;
	private String filename;

	public TestListModel() {
		this(new YamlWrapper(), new FileReaderFactory(), "Tests.txt");
	}

	protected TestListModel(YamlWrapper yaml, FileReaderFactory fileReaderFactory, String filename) {
		this.yaml = yaml;
		this.fileReaderFactory = fileReaderFactory;
		this.filename = filename;
	}

	public List<LinkedHashMap<String, List<String>>> getTests() {
		FileReader fileReader;
		List<LinkedHashMap<String, List<String>>> list = null;

		try {
			fileReader = fileReaderFactory.create(filename);

			// TypeDescription testFileDescription = new
			// TypeDescription(TestFile.class);
			// testFileDescription.putListPropertyType("commands",
			// TestUnit.class);
			// testFileDescription.putListPropertyType("commands",
			// TestUnit.class);
			// Constructor constructor = new Constructor(TestFile.class);
			// constructor.addTypeDescription(testFileDescription);
			//
			// list = (TestFile) new Yaml(constructor).load(fileReader);
			list = (List<LinkedHashMap<String, List<String>>>) yaml.load(fileReader);

		} catch (FileNotFoundException e) {
		}

		return list;
	}
}
