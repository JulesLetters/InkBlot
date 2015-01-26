package loader;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlFactory {

	public YamlWrapper create() {
		TypeDescription testFileDescription = new TypeDescription(TestFile.class);
		testFileDescription.putListPropertyType("tests", TestFileUnit.class);
		testFileDescription.putListPropertyType("commands", TestUnitCommand.class);

		Constructor constructor = new Constructor(TestFile.class);
		constructor.addTypeDescription(testFileDescription);

		return new YamlWrapper(constructor);
	}

}
