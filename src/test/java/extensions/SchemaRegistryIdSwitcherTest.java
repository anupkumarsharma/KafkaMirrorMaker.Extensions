package extensions;

import com.extensions.SchemaRegistryIdSwitcher;
import com.helpers.SchemaRegistryHelper;
import org.junit.Test;

import java.io.IOException;

public class SchemaRegistryIdSwitcherTest {



    @Test(expected = IllegalArgumentException.class)
    public void parseArgumentShouldBeAbleToParse() {

        SchemaRegistryIdSwitcher _schemaRegistryIdSwitcher = new SchemaRegistryIdSwitcher("invalid");

    }


    @Test(expected = IllegalArgumentException.class)
    public void parseArgumentShouldBeAbleToParseKeyValueAlthoughRequiredParameterException() {

        SchemaRegistryIdSwitcher _schemaRegistryIdSwitcher = new SchemaRegistryIdSwitcher("key,value");
        _schemaRegistryIdSwitcher.argumentMap.containsKey("key");

    }


    @Test()
    public void parseArgumentShouldBeAbleToParseKeyValue() {
        SchemaRegistryIdSwitcher _schemaRegistryIdSwitcher = new SchemaRegistryIdSwitcher("registry,http://pop");
        _schemaRegistryIdSwitcher.argumentMap.containsKey("registry");
    }

    @Test()
    public void parseArgumentAllValues() {
        SchemaRegistryIdSwitcher _schemaRegistryIdSwitcher = new SchemaRegistryIdSwitcher("registry,http://pop;key,value;key1,value1");
        _schemaRegistryIdSwitcher.argumentMap.containsKey("registry");
        _schemaRegistryIdSwitcher.argumentMap.containsKey("key");
        _schemaRegistryIdSwitcher.argumentMap.containsKey("key1");
    }

}