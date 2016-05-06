package presenter;

import java.util.ArrayList;
import java.util.Collections;
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
		List<ParsedTestUnit> tests = parsedTestFile.getTests();
		String fileName = parsedTestFile.getFile().getName();
		TestItem testFileItem;
		if (parsedTestFile.getError().isPresent()) {
			testFileItem = new TestItem(fileName, "FileException", Collections.emptyList());
		} else if (tests.isEmpty()) {
			testFileItem = new TestItem(fileName, "FileInvalid", Collections.emptyList());
		} else {
			List<TestItem> children = tests.stream().map(this::create).collect(Collectors.toList());
			testFileItem = new TestItem(fileName, "FileLoaded", children);
		}
		return testFileItem;
	}

	private TestItem create(ParsedTestUnit parsedTestUnit) {
		String status = parsedTestUnit.getError().isPresent() ? TestResult.INVALID : TestResult.LOADED;
		TestItem testUnitItem = new TestItem(parsedTestUnit.getName(), status);
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
