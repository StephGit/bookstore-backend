package ch.bfh.eadj.application.service;

import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.util.HashMap;
import java.util.Map;

public class MyCustomConfigSource implements ConfigSource {

    public static Map<String, String> myProperties = new HashMap<>();

    static {
        myProperties.put("my.config.key.test", "myConfigValue");
    }

    @Override
    public int getOrdinal() {
        return 0;
    }

    @Override
    public Map<String, String> getProperties() {
        return myProperties;
    }

    @Override
    public String getPropertyValue(String s) {
        return myProperties.get(s);
    }

    @Override
    public String getConfigName() {
        return null;
    }

    @Override
    public boolean isScannable() {
        return false;
    }
}
