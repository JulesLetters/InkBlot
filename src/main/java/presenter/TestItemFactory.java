package presenter;

import java.util.HashMap;
import java.util.Map;

import parser.ParsedTestUnit;
import runner.TestResult;
import view.TestItem;

public class TestItemFactory {

	private Map<ParsedTestUnit, TestItem> testItems = new HashMap<>();

	public TestItem create(ParsedTestUnit unit) {
		TestItem testItem = new TestItem(unit.getName(), TestResult.LOADED);
		testItems.put(unit, testItem);
		return testItem;
	}

	public void setUnitStatus(ParsedTestUnit unit, String status) {
		testItems.get(unit).setStatus(status);
	}

}
