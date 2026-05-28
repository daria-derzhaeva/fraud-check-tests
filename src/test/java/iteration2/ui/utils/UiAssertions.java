package iteration2.ui.utils;

import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

public class UiAssertions {

    private UiAssertions() {
    }

    public static void assertAlertContainsAndAccept(String expectedText) {
        Alert alert = switchTo().alert();

        assertThat(alert.getText())
                .contains(expectedText);

        alert.accept();
    }
}