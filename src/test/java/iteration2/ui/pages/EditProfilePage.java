package iteration2.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import iteration2.ui.utils.UiSelectors;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class EditProfilePage {

    public EditProfilePage open() {
        executeJavaScript("window.location.href = '/edit-profile';");
        return this;
    }

    public EditProfilePage shouldBeOpened() {
        $(Selectors.withText("Edit Profile"))
                .shouldBe(Condition.visible);

        $(UiSelectors.PROFILE_NAME_INPUT)
                .shouldBe(Condition.visible);

        return this;
    }

    public EditProfilePage setProfileName(String name) {
        $(UiSelectors.PROFILE_NAME_INPUT)
                .shouldBe(Condition.visible)
                .click();

        $(UiSelectors.PROFILE_NAME_INPUT)
                .sendKeys(Keys.chord(Keys.COMMAND, "a"));

        $(UiSelectors.PROFILE_NAME_INPUT)
                .sendKeys(Keys.BACK_SPACE);

        $(UiSelectors.PROFILE_NAME_INPUT)
                .sendKeys(name);

        $(UiSelectors.PROFILE_NAME_INPUT)
                .shouldHave(Condition.value(name));

        return this;
    }

    public EditProfilePage submitProfileName() {
        $(Selectors.withText("Save Changes"))
                .shouldBe(Condition.visible)
                .click();

        return this;
    }
}