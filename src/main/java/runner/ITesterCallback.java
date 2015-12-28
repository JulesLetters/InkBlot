package runner;

import parser.ParsedTestUnit;

public interface ITesterCallback {

	void testCompleted(ParsedTestUnit parsedTestUnit, TestResult result);

}
