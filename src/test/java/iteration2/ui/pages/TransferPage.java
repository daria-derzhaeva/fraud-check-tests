package iteration2.ui.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import iteration2.ui.utils.UiSelectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TransferPage {

    public void shouldBeOpened() {
        $(UiSelectors.RECIPIENT_NAME_INPUT)
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled);

        accountOptions()
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    public void selectFirstSenderAccount() {
        accountSelect()
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled);

        accountOptions()
                .shouldHave(CollectionCondition.sizeGreaterThan(1));

        accountSelect()
                .selectOption(1);
    }

    public void setRecipientName(String recipientName) {
        $(UiSelectors.RECIPIENT_NAME_INPUT)
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .setValue(recipientName)
                .shouldHave(Condition.value(recipientName));
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        $(UiSelectors.RECIPIENT_ACCOUNT_INPUT)
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .setValue(recipientAccountNumber)
                .shouldHave(Condition.value(recipientAccountNumber));
    }

    public void setAmount(String amount) {
        $(UiSelectors.AMOUNT_INPUT)
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .setValue(amount)
                .shouldHave(Condition.value(amount));
    }

    public void confirmTransfer() {
        $(UiSelectors.CONFIRM_TRANSFER_CHECKBOX)
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .click();
    }

    public void submitTransfer() {
        $(Selectors.withText("Send Transfer"))
                .shouldBe(Condition.visible)
                .shouldBe(Condition.enabled)
                .click();
    }

    private SelenideElement accountSelect() {
        return $(UiSelectors.ACCOUNT_SELECT);
    }

    private com.codeborne.selenide.ElementsCollection accountOptions() {
        return $$(UiSelectors.ACCOUNT_SELECT + " option");
    }
}