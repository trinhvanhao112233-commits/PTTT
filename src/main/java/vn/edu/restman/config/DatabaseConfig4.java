package vn.edu.restman.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseConfig4 {
    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConfig4() {
    }

    public static String getUrl() { return setting("RESTMAN_DB_URL", "restman.db.url", "db.url"); }
    public static String getUser() { return setting("RESTMAN_DB_USER", "restman.db.user", "db.user"); }
    public static String getPassword() { return setting("RESTMAN_DB_PASSWORD", "restman.db.password", "db.password"); }
    public static String getDriver() { return setting("RESTMAN_DB_DRIVER", "restman.db.driver", "db.driver"); }

    private static String setting(String environmentName, String systemPropertyName, String fileName) {
        String systemValue = System.getProperty(systemPropertyName);
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) return environmentValue;
        return PROPERTIES.getProperty(fileName, "");
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig4.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) throw new IllegalStateException("Khong tim thay db.properties");
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Khong doc duoc cau hinh co so du lieu", exception);
        }
    }
}
