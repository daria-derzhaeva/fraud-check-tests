package iteration2.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import iteration2.ui.utils.UiSelectors;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public void open() {
        Selenide.open("/login");
    }

    public void shouldBeOpened() {
        $(UiSelectors.USERNAME_INPUT)
                .shouldBe(Condition.visible);
    }

    public void setUsername(String username) {
        $(UiSelectors.USERNAME_INPUT)
                .shouldBe(Condition.visible)
                .setValue(username);
    }

    public void setPassword(String password) {
        $(UiSelectors.PASSWORD_INPUT)
                .shouldBe(Condition.visible)
                .setValue(password);
    }

    public void submitLogin() {
        $("button")
                .shouldBe(Condition.visible)
                .click();
    }
}