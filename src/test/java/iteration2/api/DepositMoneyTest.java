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

public class DepositMoneyTest extends BaseTest {

    private final AdminSteps adminSteps = new AdminSteps();

    @Test
    public void userCanDepositMoneyToOwnExistingAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse account = userSteps.createAccount();

        double balanceBeforeDeposit = databaseSteps.getAccountById(account.getId()).getBalance();
        double depositAmount = RandomData.getDepositAmount();

        userSteps.depositMoney(account.getId(), depositAmount);

        double expectedBalance = balanceBeforeDeposit + depositAmount;

        AccountDao accountFromDb = databaseSteps.getAccountById(account.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                accountFromDb.getBalance(),
                expectedBalance
        );
    }

    @Test
    public void userCanDepositMaximumAllowedAmount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse account = userSteps.createAccount();

        double balanceBeforeDeposit = databaseSteps.getAccountById(account.getId()).getBalance();

        userSteps.depositMoney(
                account.getId(),
                TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT
        );

        double expectedBalance = balanceBeforeDeposit + TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT;

        AccountDao accountFromDb = databaseSteps.getAccountById(account.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                accountFromDb.getBalance(),
                expectedBalance
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDepositAmounts")
    public void userCannotDepositInvalidAmount(double amount) {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse account = userSteps.createAccount();

        AccountDao accountBeforeDepositFromDb = databaseSteps.getAccountById(account.getId());

        userSteps.depositMoneyWithBadRequest(account.getId(), amount);

        AccountDao accountAfterDepositFromDb = databaseSteps.getAccountById(account.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                accountAfterDepositFromDb.getBalance(),
                accountBeforeDepositFromDb.getBalance()
        );
    }

    @Test
    public void userCannotDepositMoneyToNonExistingAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();

        userSteps.depositMoneyWithCustomResponse(
                        TestConstants.NON_EXISTING_ACCOUNT_ID,
                        RandomData.getDepositAmount()
                )
                .statusCode(Matchers.anyOf(
                        Matchers.equalTo(HttpStatus.SC_BAD_REQUEST),
                        Matchers.equalTo(HttpStatus.SC_NOT_FOUND),
                        Matchers.equalTo(HttpStatus.SC_FORBIDDEN)
                ));

        assertThat(databaseSteps.accountExistsById(TestConstants.NON_EXISTING_ACCOUNT_ID))
                .isFalse();
    }

    @Test
    public void userCannotDepositMoneyToAnotherUsersAccount() {
        String firstUserToken = adminSteps.createUserAndGetToken();
        String secondUserToken = adminSteps.createUserAndGetToken();

        UserSteps firstUserSteps = new UserSteps(firstUserToken);
        UserSteps secondUserSteps = new UserSteps(secondUserToken);

        AccountResponse secondUserAccount = secondUserSteps.createAccount();

        AccountDao accountBeforeDepositFromDb = databaseSteps.getAccountById(secondUserAccount.getId());

        firstUserSteps.depositMoneyWithCustomResponse(
                        secondUserAccount.getId(),
                        RandomData.getDepositAmount()
                )
                .statusCode(Matchers.anyOf(
                        Matchers.equalTo(HttpStatus.SC_BAD_REQUEST),
                        Matchers.equalTo(HttpStatus.SC_FORBIDDEN)
                ));

        AccountDao accountAfterDepositFromDb = databaseSteps.getAccountById(secondUserAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                accountAfterDepositFromDb.getBalance(),
                accountBeforeDepositFromDb.getBalance()
        );
    }

    private static Stream<Double> invalidDepositAmounts() {
        return Stream.of(
                TestConstants.NEGATIVE_DEPOSIT_AMOUNT,
                TestConstants.ZERO_AMOUNT
        );
    }
}