package com.poliakova.jdbc.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Oksana Poliakova on 02.07.2023
 * @projectName JDBC_practice
 */
public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() { }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        // // Get the input stream of the "application.properties" file from the class loader
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            // Load the properties from the input stream into the PROPERTIES object
            PROPERTIES.load(inputStream);
        } catch (IOException error) {
            // If an IOException occurs, wrap it in a RuntimeException and throw it
            throw new RuntimeException(error);
        }
    }
}
