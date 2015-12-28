package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class ParsedTestModel {

	private Map<ParsedTestUnit, TestItem> map = new LinkedHashMap<>();

	public void addFile(ParsedTestFile parsedTestFile) {
		for (ParsedTestUnit parsedTestUnit : parsedTestFile.getTests()) {
			String name = parsedTestUnit.getName();
			map.put(parsedTestUnit, new TestItem(name, TestResult.LOADED));
		}
	}

	public List<ParsedTestUnit> getTests() {
		return new ArrayList<>(map.keySet());
	}

	public List<TestItem> getTestResults() {
		return new ArrayList<>(map.values());
	}

	public void setUnitStatus(ParsedTestUnit parsedTestUnit, TestResult testResult) {
		map.get(parsedTestUnit).setStatus(testResult.getStatus());
	}

}
