package configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config.properties file was not found");
            }

            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    private Config() {
    }

    public enum Property {
        SERVER("server"),
        API_VERSION("apiVersion"),
        ADMIN_AUTH("admin.auth"),
        DEFAULT_PASSWORD("default.password"),

        DB_URL("db.url"),
        DB_USERNAME("db.username"),
        DB_PASSWORD("db.password"),

        UI_URL("uiUrl"),
        BROWSER("browser"),
        BROWSER_VERSION("browser.version"),
        BROWSER_SIZE("browser.size"),
        BROWSER_TIMEOUT("browser.timeout"),
        PAGE_LOAD_TIMEOUT("page.load.timeout"),

        SELENOID_ENABLED("selenoid.enabled"),
        SELENOID_URL("selenoid.url"),

        ADMIN_USERNAME("admin.username"),
        ADMIN_PASSWORD("admin.password");

        private final String key;

        Property(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static String getProperty(Property property) {
        return PROPERTIES.getProperty(property.getKey());
    }

    public static String getServer() {
        return getProperty(Property.SERVER);
    }

    public static String getApiVersion() {
        return getProperty(Property.API_VERSION);
    }

    public static String getBaseUrl() {
        return getServer() + getApiVersion();
    }

    public static String getAdminAuth() {
        return getProperty(Property.ADMIN_AUTH);
    }

    public static String getDefaultPassword() {
        return getProperty(Property.DEFAULT_PASSWORD);
    }

    public static String getUiUrl() {
        return getProperty(Property.UI_URL);
    }

    public static String getAdminUsername() {
        return getProperty(Property.ADMIN_USERNAME);
    }

    public static String getAdminPassword() {
        return getProperty(Property.ADMIN_PASSWORD);
    }

    public static String getDbUrl() {
        return getProperty(Property.DB_URL);
    }

    public static String getDbUsername() {
        return getProperty(Property.DB_USERNAME);
    }

    public static String getDbPassword() {
        return getProperty(Property.DB_PASSWORD);
    }
}