package loader;

import java.io.FileReader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlWrapper {

	private Yaml yaml;

	public YamlWrapper(Constructor construtor) {
		this(new Yaml(construtor));
	}

	protected YamlWrapper(Yaml yaml) {
		this.yaml = yaml;
	}

	public Object load(FileReader fileReader) {
		return yaml.load(fileReader);
	}

}
