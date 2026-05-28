package iteration2.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    public void shouldBeOpened() {
        System.out.println("CURRENT PAGE TEXT:");
        System.out.println($("body").getText());

        $(Selectors.withText("User Dashboard"))
                .shouldBe(Condition.visible);
    }

    public void openDepositPage() {
        $(Selectors.withText("Deposit Money"))
                .shouldBe(Condition.visible)
                .click();
    }

    public void openTransferPage() {
        $(Selectors.withText("Make a Transfer"))
                .shouldBe(Condition.visible)
                .click();
    }
}