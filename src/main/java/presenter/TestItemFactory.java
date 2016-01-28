package presenter;

import parser.ParsedTestUnit;
import view.TestItem;

public class TestItemFactory {

	public TestItem create(ParsedTestUnit unit) {
		return new TestItem(unit.getName(), "Loaded");
	}

}
