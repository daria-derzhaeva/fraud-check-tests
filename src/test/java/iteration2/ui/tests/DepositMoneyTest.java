package iteration2.ui.tests;

import iteration2.ui.extensions.UiSession;
import iteration2.ui.extensions.UserSession;
import iteration2.ui.extensions.UserSessionExtension;
import iteration2.ui.utils.UiTestData;
import models.CreateAccountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import specs.ResponseSpecs;

import static iteration2.ui.utils.UiAssertions.assertAlertContainsAndAccept;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(UserSessionExtension.class)
public class DepositMoneyTest extends BaseUiTest {

    @Test
    public void userCanDepositMoneyToOwnAccountTest(@UserSession UiSession session) {
        uiApiBridge.createAccount(session.getUser());

        CreateAccountResponse[] accountsBeforeDeposit = uiApiBridge.getUserAccounts(session.getUser());
        assertThat(accountsBeforeDeposit).hasSize(1);

        double depositAmount = UiTestData.randomDepositAmount();
        String depositAmountText = UiTestData.amountAsText(depositAmount);

        double balanceBeforeDeposit = accountsBeforeDeposit[0].getBalance();

        loginAsUser(session);

        customerUiSteps.openDepositPage();
        customerUiSteps.depositMoney(depositAmountText);

        assertAlertContainsAndAccept(ResponseSpecs.DEPOSIT_SUCCESSFULLY);

        CreateAccountResponse[] accountsAfterDeposit = uiApiBridge.getUserAccounts(session.getUser());

        assertThat(accountsAfterDeposit[0].getBalance())
                .isEqualTo(balanceBeforeDeposit + depositAmount);

        assertThat(accountsAfterDeposit[0].getTransactions())
                .isNotEmpty();

        assertThat(accountsAfterDeposit[0].getTransactions().toString())
                .contains("DEPOSIT");
    }

    @Test
    public void userCanDepositMaximumAllowedAmountTest(@UserSession UiSession session) {
        uiApiBridge.createAccount(session.getUser());

        CreateAccountResponse[] accountsBeforeDeposit = uiApiBridge.getUserAccounts(session.getUser());
        assertThat(accountsBeforeDeposit).hasSize(1);

        double balanceBeforeDeposit = accountsBeforeDeposit[0].getBalance();
        double maxDepositAmount = UiTestData.amountAsDouble(UiTestData.MAX_DEPOSIT_AMOUNT);

        loginAsUser(session);

        customerUiSteps.openDepositPage();
        customerUiSteps.depositMoney(UiTestData.MAX_DEPOSIT_AMOUNT);

        assertAlertContainsAndAccept(ResponseSpecs.DEPOSIT_SUCCESSFULLY);

        CreateAccountResponse[] accountsAfterDeposit = uiApiBridge.getUserAccounts(session.getUser());

        assertThat(accountsAfterDeposit[0].getBalance())
                .isEqualTo(balanceBeforeDeposit + maxDepositAmount);

        assertThat(accountsAfterDeposit[0].getTransactions())
                .isNotEmpty();

        assertThat(accountsAfterDeposit[0].getTransactions().toString())
                .contains("DEPOSIT");
    }

    @Test
    public void userCanNotDepositAmountGreaterThanMaximumAllowedAmountTest(@UserSession UiSession session) {
        uiApiBridge.createAccount(session.getUser());

        CreateAccountResponse[] accountsBeforeDeposit = uiApiBridge.getUserAccounts(session.getUser());
        assertThat(accountsBeforeDeposit).hasSize(1);

        double balanceBeforeDeposit = accountsBeforeDeposit[0].getBalance();

        loginAsUser(session);

        customerUiSteps.openDepositPage();
        customerUiSteps.depositMoney(UiTestData.GREATER_THAN_MAX_DEPOSIT_AMOUNT);

        assertAlertContainsAndAccept(ResponseSpecs.INVALID_DEPOSIT_AMOUNT_MESSAGE);

        CreateAccountResponse[] accountsAfterDeposit = uiApiBridge.getUserAccounts(session.getUser());

        assertThat(accountsAfterDeposit[0].getBalance())
                .isEqualTo(balanceBeforeDeposit);
    }

    @Test
    public void userCanNotSubmitDepositWithoutSelectedAccountTest(@UserSession UiSession session) {
        uiApiBridge.createAccount(session.getUser());

        CreateAccountResponse[] accountsBeforeDeposit = uiApiBridge.getUserAccounts(session.getUser());
        assertThat(accountsBeforeDeposit).hasSize(1);

        double depositAmount = UiTestData.randomDepositAmount();
        String depositAmountText = UiTestData.amountAsText(depositAmount);

        double balanceBeforeDeposit = accountsBeforeDeposit[0].getBalance();

        loginAsUser(session);

        customerUiSteps.openDepositPage();
        customerUiSteps.depositMoneyWithoutSelectedAccount(depositAmountText);

        assertAlertContainsAndAccept(ResponseSpecs.ACCOUNT_NOT_SELECTED_MESSAGE);

        CreateAccountResponse[] accountsAfterDeposit = uiApiBridge.getUserAccounts(session.getUser());

        assertThat(accountsAfterDeposit[0].getBalance())
                .isEqualTo(balanceBeforeDeposit);
    }

    @Test
    public void userCanNotSubmitDepositWithBlankAmountTest(@UserSession UiSession session) {
        uiApiBridge.createAccount(session.getUser());

        CreateAccountResponse[] accountsBeforeDeposit = uiApiBridge.getUserAccounts(session.getUser());
        assertThat(accountsBeforeDeposit).hasSize(1);

        double balanceBeforeDeposit = accountsBeforeDeposit[0].getBalance();

        loginAsUser(session);

        customerUiSteps.openDepositPage();
        customerUiSteps.submitDepositWithoutAmount();

        assertAlertContainsAndAccept(ResponseSpecs.INVALID_AMOUNT_MESSAGE);

        CreateAccountResponse[] accountsAfterDeposit = uiApiBridge.getUserAccounts(session.getUser());

        assertThat(accountsAfterDeposit[0].getBalance())
                .isEqualTo(balanceBeforeDeposit);
    }
}