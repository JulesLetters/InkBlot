package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

	public List<String> getTests() {
		FileReader fileReader;
		List<String> list = null;

		try {
			fileReader = fileReaderFactory.create(filename);
			list = (List<String>) yaml.load(fileReader);
		} catch (FileNotFoundException e) {
		}

		return list;
	}
}
