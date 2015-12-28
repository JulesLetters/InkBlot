package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileReaderFactory {

	public FileReader create(File file) throws FileNotFoundException {
		return new FileReader(file);
	}

}
