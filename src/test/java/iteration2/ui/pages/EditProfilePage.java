package iteration2.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import iteration2.ui.utils.UiSelectors;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class EditProfilePage {

    public void open() {
        executeJavaScript("window.location.href = '/edit-profile';");
    }

    public void shouldBeOpened() {
        $(Selectors.withText("Edit Profile"))
                .shouldBe(Condition.visible);

        profileNameInput()
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled);
    }

    public void setProfileName(String name) {
        profileNameInput()
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .click();

        profileNameInput()
                .sendKeys(Keys.chord(Keys.CONTROL, "a"));

        profileNameInput()
                .sendKeys(Keys.BACK_SPACE);

        profileNameInput()
                .sendKeys(name);

        profileNameInput()
                .shouldHave(Condition.value(name));
    }

    public void submitProfileName() {
        $(Selectors.withText("Save Changes"))
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .click();
    }

    private SelenideElement profileNameInput() {
        return $(UiSelectors.PROFILE_NAME_INPUT);
    }
}