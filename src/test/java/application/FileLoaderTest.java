package application;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FileLoaderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Mock
	private TestListModel model;

	private FileLoader fileLoader;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		fileLoader = new FileLoader(model);
	}

	@Test
	public void testLoadYamlFile() throws Exception {
		File expectedFile = folder.newFile("MyFile.yaml");

		fileLoader.loadFiles(folder.getRoot().toPath());

		verify(model).loadFile(expectedFile);
	}

	@Test
	public void testLoadOnlyYamlFiles() throws Exception {
		File expectedFile1 = folder.newFile("Test.yaml");
		File expectedFile2 = folder.newFile("Ambiguous.yaml");
		File notExpectedFile = folder.newFile("Ambiguous.txt");

		fileLoader.loadFiles(folder.getRoot().toPath());

		verify(model).loadFile(expectedFile1);
		verify(model).loadFile(expectedFile2);
		verify(model, never()).loadFile(notExpectedFile);
	}

	@Test
	public void testLoadFilesInSubdirectories() throws Exception {
		File subfolder1 = folder.newFolder("Subfolder1");
		File subfolder2 = folder.newFolder("Subfolder2");
		File expectedFile1 = new File(subfolder1, "Test.yaml");
		File expectedFile2 = new File(subfolder1, "Test2.yaml");
		File expectedFile3 = new File(subfolder2, "Test3.yaml");
		File expectedFile4 = folder.newFile("Test4.yaml");
		expectedFile1.createNewFile();
		expectedFile2.createNewFile();
		expectedFile3.createNewFile();

		fileLoader.loadFiles(folder.getRoot().toPath());

		verify(model).loadFile(expectedFile1);
		verify(model).loadFile(expectedFile2);
		verify(model).loadFile(expectedFile3);
		verify(model).loadFile(expectedFile4);
		verify(model, never()).loadFile(subfolder1);
		verify(model, never()).loadFile(subfolder2);
	}

}
