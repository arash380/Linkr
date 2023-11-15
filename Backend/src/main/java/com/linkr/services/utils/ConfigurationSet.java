package com.linkr.services.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Used to get environment variable data.
 *
 * @author Team Linkr
 * @version 1.0
 */
public class ConfigurationSet {

    /**
     * Gets property.
     *
     * @param name the name
     * @return the property
     */
    public static String getProperty(String name) {

        String propertyValue = "";

        try (InputStream in = ConfigurationSet.class.getClassLoader().
                getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            propertyValue = properties.getProperty(name, null);

            if (propertyValue == null) {
                throw new IOException(
                    "Object with name: " + name + " was not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return propertyValue;
    }

}
