package model;

import java.io.FileReader;

import org.yaml.snakeyaml.Yaml;

public class YamlWrapper {

	private Yaml yaml;

	public YamlWrapper() {
		this(new Yaml());
	}

	protected YamlWrapper(Yaml yaml) {
		this.yaml = yaml;
	}

	public Object load(FileReader fileReader) {
		return yaml.load(fileReader);
	}

}
