package iteration2.ui.pages;

import com.codeborne.selenide.Condition;
import iteration2.ui.utils.UiSelectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DepositPage {

    public void shouldBeOpened() {
        $(UiSelectors.AMOUNT_INPUT)
                .shouldBe(Condition.visible);
    }

    public void selectFirstAccount() {
        $(UiSelectors.ACCOUNT_SELECT)
                .shouldBe(Condition.visible)
                .selectOption(1);
    }

    public void setAmount(String amount) {
        $(UiSelectors.AMOUNT_INPUT)
                .shouldBe(Condition.visible)
                .setValue(amount);
    }

    public void submitDeposit() {
        $$("button")
                .filterBy(Condition.text("Deposit"))
                .last()
                .shouldBe(Condition.visible)
                .click();
    }
}