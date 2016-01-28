package model;

import java.util.ArrayList;
import java.util.List;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;

public class ParsedTestModel {

	private List<ParsedTestUnit> units = new ArrayList<>();

	public void addFile(ParsedTestFile parsedTestFile) {
		parsedTestFile.getTests().forEach(unit -> units.add(unit));
	}

	public List<ParsedTestUnit> getTests() {
		return new ArrayList<>(units);
	}

	public void setUnitStatus(ParsedTestUnit parsedTestUnit, TestResult testResult) {
		// list.get(parsedTestUnit).setStatus(testResult.getStatus());
	}

}
