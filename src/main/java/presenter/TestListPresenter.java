package presenter;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;
import view.TestListView;
import application.IEventBus;
import application.TestListModel;

import com.google.common.eventbus.Subscribe;

import events.RunButtonClicked;
import events.TestListModelUpdatedEvent;

public class TestListPresenter {

	private TestListView testListView;
	private TestListModel testListModel;
	private Map<ParsedTestUnit, TestItem> viewObjects = new LinkedHashMap<>();

	public TestListPresenter(TestListView testListView, TestListModel testListModel, IEventBus eventBus) {
		this.testListView = testListView;
		this.testListModel = testListModel;
		eventBus.register(this);
		File currentDirectory = Paths.get(".").toAbsolutePath().toFile();

		File[] listFiles = currentDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
		for (File file : listFiles) {
			testListModel.loadFile(file);
		}
		// testListModel.loadFile(new File("Tests.txt"));
		// testListModel.loadFile(new File("OneTest.txt"));
	}

	// There are three real events that can happen:
	// 'File Added', 'File Removed', 'File Changed'

	@Subscribe
	public void modelUpdated(TestListModelUpdatedEvent event) {
		// "File Added"

		List<TestItem> list = new ArrayList<>();
		List<ParsedTestFile> parsedTestFiles = testListModel.getParsedTestFiles();
		for (ParsedTestFile parsedTestFile : parsedTestFiles) {
			String fileName = parsedTestFile.getFile().getName();

			List<ParsedTestUnit> tests = parsedTestFile.getTests();
			List<TestItem> children = new ArrayList<>();
			for (ParsedTestUnit parsedTestUnit : tests) {
				children.add(new TestItem(parsedTestUnit.getName(), "Loaded"));
			}

			// Add everything, File, Unit, and Command, to one Map< M, TestItem
			// >

			TestItem newFile = new TestItem(fileName, TestResult.LOADED, children);
			list.add(newFile);
		}

		testListView.setInput(list);
	}

	// ParsedFiles are Id'd by their File
	// Tests are Id'd by their name.
	// Commands don't really matter? Exact string match if they do.
	// (ALL Commands could hold a buffer in 'debug' mode?)

	@Subscribe
	public void runButtonClicked(RunButtonClicked event) {
		testListModel.runAllTests();
	}

	// public void addFile(ParsedTestFile parsedTestFile) {
	// for (ParsedTestUnit parsedTestUnit : parsedTestFile.getTests()) {
	// String name = parsedTestUnit.getName();
	// viewObjects.put(parsedTestUnit, new TestItem(name, ));
	// }
	// }

}
