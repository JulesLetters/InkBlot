package presenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class TestItemFactory {

	private Map<ParsedTestUnit, TestItem> testItems = new LinkedHashMap<>();

	public TestItem create(ParsedTestFile parsedTestFile) {
		List<TestItem> children = parsedTestFile.getTests().stream().map(this::create).collect(Collectors.toList());
		String fileName = parsedTestFile.getFile().getName();
		TestItem testFileItem = new TestItem(fileName, "Loaded", children);
		return testFileItem;
	}

	private TestItem create(ParsedTestUnit parsedTestUnit) {
		TestItem testUnitItem = new TestItem(parsedTestUnit.getName(), TestResult.LOADED);
		testItems.put(parsedTestUnit, testUnitItem);
		return testUnitItem;
	}

	public void setUnitStatus(ParsedTestUnit unit, String status) {
		testItems.get(unit).setStatus(status);
	}

	public List<ParsedTestUnit> getTests() {
		return new ArrayList<>(testItems.keySet());
	}

}
