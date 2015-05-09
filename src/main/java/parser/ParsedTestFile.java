package parser;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ParsedTestFile {

	private File file;
	private List<ParsedTestUnit> tests;
	private Optional<String> error;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<ParsedTestUnit> getTests() {
		return tests;
	}

	public void setTests(List<ParsedTestUnit> tests) {
		this.tests = tests;
	}

	public Optional<String> getError() {
		return error;
	}

	public void setError(Optional<String> error) {
		this.error = error;
	}

}
