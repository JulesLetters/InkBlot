package model;

import java.io.FileReader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlWrapper {

	private Yaml yaml;

	public YamlWrapper() {
		this(new Yaml());
	}

	public YamlWrapper(Constructor constructor) {
		this(new Yaml(constructor));
	}

	protected YamlWrapper(Yaml yaml) {
		this.yaml = yaml;
	}

	public Object load(FileReader fileReader) {
		return yaml.load(fileReader);
	}

}
