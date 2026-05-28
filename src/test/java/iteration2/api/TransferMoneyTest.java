package iteration2.api;

import database.dao.AccountDao;
import generators.RandomData;
import generators.TestConstants;
import models.AccountResponse;
import models.comparison.ModelAssertions;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferMoneyTest extends BaseTest {

    private final AdminSteps adminSteps = new AdminSteps();

    @Test
    public void userCanTransferMoneyToAnotherAccount() {
        String senderToken = adminSteps.createUserAndGetToken();
        String receiverToken = adminSteps.createUserAndGetToken();

        UserSteps senderSteps = new UserSteps(senderToken);
        UserSteps receiverSteps = new UserSteps(receiverToken);

        AccountResponse senderAccount = senderSteps.createAccount();
        AccountResponse receiverAccount = receiverSteps.createAccount();

        double transferAmount = RandomData.getTransferAmount();
        double depositAmount = transferAmount + RandomData.getDepositAmount();

        senderSteps.depositMoney(senderAccount.getId(), depositAmount);

        AccountDao senderAccountBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        senderSteps.transferMoney(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        AccountDao senderAccountAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAccountAfterTransfer.getBalance(),
                senderAccountBeforeTransfer.getBalance() - transferAmount
        );
        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAccountAfterTransfer.getBalance(),
                receiverAccountBeforeTransfer.getBalance() + transferAmount
        );
    }

    @Test
    public void userCanTransferMaximumAllowedAmount() {
        String senderToken = adminSteps.createUserAndGetToken();
        String receiverToken = adminSteps.createUserAndGetToken();

        UserSteps senderSteps = new UserSteps(senderToken);
        UserSteps receiverSteps = new UserSteps(receiverToken);

        AccountResponse senderAccount = senderSteps.createAccount();
        AccountResponse receiverAccount = receiverSteps.createAccount();

        senderSteps.depositMoney(senderAccount.getId(), TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT);
        senderSteps.depositMoney(senderAccount.getId(), TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT);

        AccountDao senderAccountBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        senderSteps.transferMoney(
                senderAccount.getId(),
                receiverAccount.getId(),
                TestConstants.MAX_TRANSFER_ALLOWED_AMOUNT
        );

        AccountDao senderAccountAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAccountAfterTransfer.getBalance(),
                senderAccountBeforeTransfer.getBalance() - TestConstants.MAX_TRANSFER_ALLOWED_AMOUNT
        );
        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAccountAfterTransfer.getBalance(),
                receiverAccountBeforeTransfer.getBalance() + TestConstants.MAX_TRANSFER_ALLOWED_AMOUNT
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTransferAmounts")
    public void userCannotTransferInvalidAmount(double amount) {
        String senderToken = adminSteps.createUserAndGetToken();
        String receiverToken = adminSteps.createUserAndGetToken();

        UserSteps senderSteps = new UserSteps(senderToken);
        UserSteps receiverSteps = new UserSteps(receiverToken);

        AccountResponse senderAccount = senderSteps.createAccount();
        AccountResponse receiverAccount = receiverSteps.createAccount();

        senderSteps.depositMoney(senderAccount.getId(), TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT);

        AccountDao senderAccountBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        senderSteps.transferMoneyWithBadRequest(senderAccount.getId(), receiverAccount.getId(), amount);

        AccountDao senderAccountAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAccountAfterTransfer.getBalance(),
                senderAccountBeforeTransfer.getBalance()
        );
        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAccountAfterTransfer.getBalance(),
                receiverAccountBeforeTransfer.getBalance()
        );
    }

    @Test
    public void userCannotTransferAmountGreaterThanSenderAccountBalance() {
        String senderToken = adminSteps.createUserAndGetToken();
        String receiverToken = adminSteps.createUserAndGetToken();

        UserSteps senderSteps = new UserSteps(senderToken);
        UserSteps receiverSteps = new UserSteps(receiverToken);

        AccountResponse senderAccount = senderSteps.createAccount();
        AccountResponse receiverAccount = receiverSteps.createAccount();

        senderSteps.depositMoney(senderAccount.getId(), TestConstants.SMALL_DEPOSIT_AMOUNT);

        AccountDao senderAccountBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        senderSteps.transferMoneyWithBadRequest(
                senderAccount.getId(),
                receiverAccount.getId(),
                TestConstants.MORE_THAN_BALANCE_TRANSFER_AMOUNT
        );

        AccountDao senderAccountAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverAccountAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAccountAfterTransfer.getBalance(),
                senderAccountBeforeTransfer.getBalance()
        );
        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAccountAfterTransfer.getBalance(),
                receiverAccountBeforeTransfer.getBalance()
        );
    }

    @Test
    public void userCannotTransferFromNonExistingSenderAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse receiverAccount = userSteps.createAccount();

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();

        AccountDao receiverAccountBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        userSteps.transferMoneyWithCustomResponse(
                        TestConstants.NON_EXISTING_ACCOUNT_ID,
                        receiverAccount.getId(),
                        RandomData.getTransferAmount()
                )
                .statusCode(Matchers.anyOf(
                        Matchers.equalTo(HttpStatus.SC_BAD_REQUEST),
                        Matchers.equalTo(HttpStatus.SC_NOT_FOUND),
                        Matchers.equalTo(HttpStatus.SC_FORBIDDEN)
                ));

        AccountDao receiverAccountAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();

        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAccountAfterTransfer.getBalance(),
                receiverAccountBeforeTransfer.getBalance()
        );
    }

    @Test
    public void userCannotTransferToNonExistingReceiverAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse senderAccount = userSteps.createAccount();

        double transferAmount = RandomData.getTransferAmount();
        double depositAmount = transferAmount + RandomData.getDepositAmount();

        userSteps.depositMoney(senderAccount.getId(), depositAmount);

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();

        AccountDao senderAccountBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());

        userSteps.transferMoneyWithCustomResponse(
                        senderAccount.getId(),
                        TestConstants.NON_EXISTING_ACCOUNT_ID,
                        transferAmount
                )
                .statusCode(Matchers.anyOf(
                        Matchers.equalTo(HttpStatus.SC_BAD_REQUEST),
                        Matchers.equalTo(HttpStatus.SC_NOT_FOUND)
                ));

        AccountDao senderAccountAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAccountAfterTransfer.getBalance(),
                senderAccountBeforeTransfer.getBalance()
        );
    }

    private static Stream<Double> invalidTransferAmounts() {
        return Stream.of(
                TestConstants.MORE_THAN_MAX_TRANSFER_AMOUNT,
                TestConstants.NEGATIVE_TRANSFER_AMOUNT,
                TestConstants.ZERO_AMOUNT
        );
    }
}