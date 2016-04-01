package presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class TestItemFactory {

	private Map<ParsedTestUnit, TestItem> testItems = new HashMap<>();

	public TestItem create(ParsedTestFile parsedTestFile) {
		List<TestItem> children = parsedTestFile.getTests().stream().map(this::create).collect(Collectors.toList());
		String fileName = parsedTestFile.getFile().getName();
		TestItem testItem = new TestItem(fileName, "Loaded", children);
		return testItem;
	}

	private TestItem create(ParsedTestUnit parsedTestUnit) {
		TestItem testItem = new TestItem(parsedTestUnit.getName(), TestResult.LOADED);
		testItems.put(parsedTestUnit, testItem);
		return testItem;
	}

	public void setUnitStatus(ParsedTestUnit unit, String status) {
		testItems.get(unit).setStatus(status);
	}

}
