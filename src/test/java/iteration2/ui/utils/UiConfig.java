package iteration2.ui.utils;

import com.codeborne.selenide.Configuration;
import configs.Config;

import java.util.Map;

public class UiConfig {

    private UiConfig() {
    }

    public static void setupBrowser() {
        Configuration.baseUrl = getRequiredProperty(Config.Property.UI_URL);
        Configuration.browser = getPropertyOrDefault(Config.Property.BROWSER, "chrome");
        Configuration.browserVersion = getPropertyOrDefault(Config.Property.BROWSER_VERSION, "128.0");
        Configuration.browserSize = getPropertyOrDefault(Config.Property.BROWSER_SIZE, "1920x1080");
        Configuration.timeout = Long.parseLong(getPropertyOrDefault(Config.Property.BROWSER_TIMEOUT, "10000"));
        Configuration.pageLoadTimeout = Long.parseLong(getPropertyOrDefault(Config.Property.PAGE_LOAD_TIMEOUT, "60000"));

        Configuration.reportsFolder = "build/reports/tests";
        Configuration.screenshots = true;
        Configuration.savePageSource = true;

        if (Boolean.parseBoolean(getPropertyOrDefault(Config.Property.SELENOID_ENABLED, "false"))) {
            Configuration.remote = getRequiredProperty(Config.Property.SELENOID_URL);

            Configuration.browserCapabilities.setCapability("selenoid:options",
                    Map.of(
                            "enableVNC", true,
                            "enableLog", true
                    )
            );
        }
    }

    private static String getRequiredProperty(Config.Property property) {
        String value = Config.getProperty(property);

        if (value == null) {
            throw new RuntimeException(property.getKey() + " is not found in config.properties");
        }

        return value;
    }

    private static String getPropertyOrDefault(Config.Property property, String defaultValue) {
        String value = Config.getProperty(property);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }
}