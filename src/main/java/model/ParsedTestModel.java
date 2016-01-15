package model;

import java.util.ArrayList;
import java.util.List;

import parser.ParsedTestFile;
import parser.ParsedTestUnit;
import runner.TestResult;

public class ParsedTestModel {

	private List<ParsedTestFile> files = new ArrayList<>();
	private List<ParsedTestUnit> units = new ArrayList<>();

	public List<ParsedTestFile> getParsedTestFiles() {
		return files;
	}

	public List<ParsedTestUnit> getTests() {
		return units;
	}

	public void setUnitStatus(ParsedTestUnit parsedTestUnit, TestResult result) {
		// TODO Auto-generated method stub

	}

	public void addFile(ParsedTestFile parsedTestFile) {
		files.add(parsedTestFile);

		for (ParsedTestUnit unit : parsedTestFile.getTests()) {
			units.add(unit);
		}
	}

}
