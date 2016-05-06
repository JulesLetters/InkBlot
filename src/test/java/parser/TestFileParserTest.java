package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import loader.TestFile;
import loader.TestFileLoader;
import loader.TestFileUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yaml.snakeyaml.error.YAMLException;

public class TestFileParserTest {

	@Mock
	private TestFileLoader testFileLoader;
	@Mock
	private File file;
	@Mock
	private TestFile testFile;
	@Mock
	private TestFileUnit testUnit1;
	@Mock
	private TestFileUnit testUnit2;
	@Mock
	private ParsedTestUnit parsedUnit1;
	@Mock
	private ParsedTestUnit parsedUnit2;
	@Mock
	private TestUnitParser testUnitParser;
	@Mock
	private IParserCallback callback;
	@Captor
	private ArgumentCaptor<ParsedTestFile> parsedTestFileCaptor;

	private TestFileParser testFileParser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testFileParser = new TestFileParser(testFileLoader, testUnitParser);
	}

	@Test
	public void callbackIsCalledWithParsedTestFile() throws Exception {
		verify(callback, never()).parseCompleted(any(ParsedTestFile.class));
		when(testFileLoader.loadTestfile(file)).thenReturn(testFile);
		when(testFile.getTests()).thenReturn(Arrays.asList());

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		assertEquals(file, parsedTestFile.getFile());
		assertFalse(parsedTestFile.getError().isPresent());
	}

	@Test
	public void parsedUnitIsSetOnParsedFile() throws Exception {
		when(testFileLoader.loadTestfile(file)).thenReturn(testFile);
		when(testFile.getTests()).thenReturn(Arrays.asList(testUnit1));
		when(testUnitParser.parse(testUnit1)).thenReturn(parsedUnit1);

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		List<ParsedTestUnit> parsedTests = parsedTestFile.getTests();
		assertEquals(1, parsedTests.size());
		assertEquals(parsedUnit1, parsedTests.get(0));
	}

	@Test
	public void multipleParsedUnitsAreSetOnParsedFile() throws Exception {
		when(testFileLoader.loadTestfile(file)).thenReturn(testFile);
		when(testFile.getTests()).thenReturn(Arrays.asList(testUnit1, testUnit2));
		when(testUnitParser.parse(testUnit1)).thenReturn(parsedUnit1);
		when(testUnitParser.parse(testUnit2)).thenReturn(parsedUnit2);

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		List<ParsedTestUnit> parsedTests = parsedTestFile.getTests();
		assertEquals(2, parsedTests.size());
		assertEquals(parsedUnit1, parsedTests.get(0));
		assertEquals(parsedUnit2, parsedTests.get(1));
	}

	@Test
	public void noParsedUnitsAreSetForFileWithNoTests() throws Exception {
		when(testFileLoader.loadTestfile(file)).thenReturn(testFile);
		when(testFile.getTests()).thenReturn(null);

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		List<ParsedTestUnit> parsedTests = parsedTestFile.getTests();
		assertEquals(0, parsedTests.size());
	}

	@Test
	public void parsedTestFileHasErrorWhenFileNotFound() throws Exception {
		String expectedFilePath = "C:/MyFileLocation/MyFile.txt";
		when(file.getAbsolutePath()).thenReturn(expectedFilePath);
		when(testFileLoader.loadTestfile(file)).thenThrow(new FileNotFoundException());

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		assertEquals(file, parsedTestFile.getFile());
		assertEquals(Collections.EMPTY_LIST, parsedTestFile.getTests());
		assertEquals("File Not Found: " + expectedFilePath, parsedTestFile.getError().get());
	}

	@Test
	public void parsedTestFileHasErrorWhenYamlLoaderFails() throws Exception {
		String expectedErrorText = "Yaml's error messages are generally helpful.";
		when(testFileLoader.loadTestfile(file)).thenThrow(new YAMLException(expectedErrorText));

		testFileParser.parse(file, callback);

		verify(callback).parseCompleted(parsedTestFileCaptor.capture());
		ParsedTestFile parsedTestFile = parsedTestFileCaptor.getValue();
		assertEquals(file, parsedTestFile.getFile());
		assertEquals(Collections.EMPTY_LIST, parsedTestFile.getTests());
		assertEquals("Yaml Loading Failed:\r\n" + expectedErrorText, parsedTestFile.getError().get());
	}
}
