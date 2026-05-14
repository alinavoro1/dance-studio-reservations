package main.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Could not load application.properties");
        }
    }

    private ConfigLoader() {
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}