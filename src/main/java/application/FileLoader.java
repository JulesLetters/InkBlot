package application;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class FileLoader {

	private TestListModel testListModel;

	public FileLoader(TestListModel testListModel) {
		this.testListModel = testListModel;
	}

	public void loadFiles(Path root) {
		try {
			//@formatter:off
			Files.walk(root, FileVisitOption.FOLLOW_LINKS)
					.filter(FileLoader::isRegularFileFollowLinks)
					.filter(FileLoader::isYamlPath)
					.forEach(path -> testListModel.loadFile(path.toFile()));
			//@formatter:on
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isRegularFileFollowLinks(Path path) {
		return Files.isRegularFile(path, new LinkOption[0]);
	}

	private static boolean isYamlPath(Path path) {
		return path.toFile().getName().endsWith(".yaml");
	}
}
