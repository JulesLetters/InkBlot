package model;

import java.io.FileNotFoundException;
import java.io.FileReader;

import model.fileStructure.TestCommandText;
import model.fileStructure.TestFile;
import model.fileStructure.TestUnit;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

public class TestListModel {

	private FileReaderFactory fileReaderFactory;
	private String filename;

	public TestListModel() {
		this("Tests.txt");
	}

	protected TestListModel(String filename) {
		this(new FileReaderFactory(), filename);
	}

	protected TestListModel(FileReaderFactory fileReaderFactory, String filename) {
		this.fileReaderFactory = fileReaderFactory;
		this.filename = filename;
	}

	public TestFile getTests() {
		FileReader fileReader;
		TestFile list = null;

		try {
			fileReader = fileReaderFactory.create(filename);

			TypeDescription testFileDescription = new TypeDescription(TestFile.class);
			testFileDescription.putListPropertyType("tests", TestUnit.class);
			testFileDescription.putListPropertyType("commands", TestCommandText.class);
			Constructor constructor = new Constructor(TestFile.class);
			constructor.addTypeDescription(testFileDescription);
			list = (TestFile) new YamlWrapper(constructor).load(fileReader);

		} catch (FileNotFoundException e) {
		}

		return list;
	}
}
