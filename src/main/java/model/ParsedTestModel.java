package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;

public class ParsedTestModel {

	private Map<ParsedTestUnit, String> unitStatuses = new LinkedHashMap<>();

	public void addFile(ParsedTestFile parsedTestFile) {
		parsedTestFile.getTests().forEach(unit -> unitStatuses.put(unit, TestResult.LOADED));
	}

	public List<ParsedTestUnit> getTests() {
		return new ArrayList<>(unitStatuses.keySet());
	}

	public void setUnitStatus(ParsedTestUnit parsedTestUnit, TestResult testResult) {
		unitStatuses.put(parsedTestUnit, testResult.getStatus());
	}

	public String getUnitStatus(ParsedTestUnit parsedTestUnit) {
		return unitStatuses.get(parsedTestUnit);
	}

}
