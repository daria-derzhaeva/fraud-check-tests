package iteration2.ui.tests;

import iteration2.ui.extensions.UiSession;
import iteration2.ui.extensions.UserSession;
import iteration2.ui.extensions.UserSessionExtension;
import iteration2.ui.utils.UiTestData;
import models.CreateAccountResponse;
import models.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import specs.ResponseSpecs;

import static iteration2.ui.utils.UiAssertions.assertAlertContainsAndAccept;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(UserSessionExtension.class)
public class TransferMoneyTest extends BaseUiTest {

    @Test
    public void userCanTransferMoneyWithValidAmountTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double transferAmount = UiTestData.randomTransferAmount();
        String transferAmountText = UiTestData.amountAsText(transferAmount);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                transferAmount + UiTestData.TRANSFER_BALANCE_RESERVE
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoney(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                transferAmountText
        );

        assertAlertContainsAndAccept(ResponseSpecs.TRANSFER_SUCCESSFULLY);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer - transferAmount);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer + transferAmount);

        assertThat(senderAccountAfterTransfer.getTransactions())
                .isNotEmpty();

        assertThat(receiverAccountAfterTransfer.getTransactions())
                .isNotEmpty();

        assertThat(senderAccountAfterTransfer.getTransactions().toString())
                .contains("TRANSFER_OUT");

        assertThat(receiverAccountAfterTransfer.getTransactions().toString())
                .contains("TRANSFER_IN");
    }

    @Test
    public void userCanTransferMaximumAllowedAmountTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double maxTransferAmount = UiTestData.amountAsDouble(UiTestData.MAX_TRANSFER_AMOUNT);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                maxTransferAmount
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoney(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                UiTestData.MAX_TRANSFER_AMOUNT
        );

        assertAlertContainsAndAccept(ResponseSpecs.TRANSFER_SUCCESSFULLY);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer - maxTransferAmount);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer + maxTransferAmount);

        assertThat(senderAccountAfterTransfer.getTransactions().toString())
                .contains("TRANSFER_OUT");

        assertThat(receiverAccountAfterTransfer.getTransactions().toString())
                .contains("TRANSFER_IN");
    }

    @Test
    public void userCanNotTransferAmountGreaterThanMaximumAllowedAmountTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double maxTransferAmount = UiTestData.amountAsDouble(UiTestData.MAX_TRANSFER_AMOUNT);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                maxTransferAmount
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoney(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                UiTestData.GREATER_THAN_MAX_TRANSFER_AMOUNT
        );

        assertAlertContainsAndAccept(ResponseSpecs.INVALID_TRANSFER_MESSAGE);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer);
    }

    @Test
    public void userCanNotTransferAmountGreaterThanSenderAccountBalanceTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderInitialBalance = UiTestData.amountAsDouble(UiTestData.INSUFFICIENT_FUNDS_BALANCE);
        double transferAmount = UiTestData.amountAsDouble(UiTestData.GREATER_THAN_BALANCE_TRANSFER_AMOUNT);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                senderInitialBalance
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoney(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                UiTestData.GREATER_THAN_BALANCE_TRANSFER_AMOUNT
        );

        assertAlertContainsAndAccept(ResponseSpecs.INVALID_TRANSFER_MESSAGE);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer);
    }

    @Test
    public void userCanNotSubmitTransferWithoutSelectedSenderAccountTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double transferAmount = UiTestData.randomTransferAmount();
        String transferAmountText = UiTestData.amountAsText(transferAmount);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                transferAmount + UiTestData.TRANSFER_BALANCE_RESERVE
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoneyWithoutSelectedSenderAccount(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                transferAmountText
        );

        assertAlertContainsAndAccept(ResponseSpecs.TRANSFER_REQUIRED_FIELDS_MESSAGE);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer);
    }

    @Test
    public void userCanNotSubmitTransferWithoutRecipientAccountNumberTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double transferAmount = UiTestData.randomTransferAmount();
        String transferAmountText = UiTestData.amountAsText(transferAmount);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                transferAmount + UiTestData.TRANSFER_BALANCE_RESERVE
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoneyWithoutRecipientAccountNumber(
                receiver.getUsername(),
                transferAmountText
        );

        assertAlertContainsAndAccept(ResponseSpecs.TRANSFER_REQUIRED_FIELDS_MESSAGE);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer);
    }

    @Test
    public void userCanNotSubmitTransferWithoutConfirmationCheckboxTest(@UserSession UiSession senderSession) {
        CreateUserRequest receiver = uiApiBridge.createUser();

        uiApiBridge.createAccount(senderSession.getUser());
        uiApiBridge.createAccount(receiver);

        CreateAccountResponse senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double transferAmount = UiTestData.randomTransferAmount();
        String transferAmountText = UiTestData.amountAsText(transferAmount);

        uiApiBridge.deposit(
                senderSession.getUser(),
                senderAccount.getId(),
                transferAmount + UiTestData.TRANSFER_BALANCE_RESERVE
        );

        senderAccount = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        receiverAccount = uiApiBridge.getUserAccounts(receiver)[0];

        double senderBalanceBeforeTransfer = senderAccount.getBalance();
        double receiverBalanceBeforeTransfer = receiverAccount.getBalance();

        loginAsUser(senderSession);

        customerUiSteps.openTransferPage();

        customerUiSteps.transferMoneyWithoutConfirmation(
                receiver.getUsername(),
                receiverAccount.getAccountNumber(),
                transferAmountText
        );

        assertAlertContainsAndAccept(ResponseSpecs.TRANSFER_REQUIRED_FIELDS_MESSAGE);

        CreateAccountResponse senderAccountAfterTransfer = uiApiBridge.getUserAccounts(senderSession.getUser())[0];
        CreateAccountResponse receiverAccountAfterTransfer = uiApiBridge.getUserAccounts(receiver)[0];

        assertThat(senderAccountAfterTransfer.getBalance())
                .isEqualTo(senderBalanceBeforeTransfer);

        assertThat(receiverAccountAfterTransfer.getBalance())
                .isEqualTo(receiverBalanceBeforeTransfer);
    }
}