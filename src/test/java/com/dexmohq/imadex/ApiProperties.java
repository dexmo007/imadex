package com.dexmohq.imadex;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ApiProperties {

    public static String getKey(String name) throws IOException {
        final Properties properties = new Properties();
        properties.load(ApiProperties.class.getResourceAsStream("/tagging.properties"));
        final String key = properties.getProperty(name);
        if (key == null) {
            throw new NoSuchElementException("Api key '" + name + "' not found");
        }
        return key;
    }


}
