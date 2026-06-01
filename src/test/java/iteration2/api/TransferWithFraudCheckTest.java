package iteration2.api;

import annotations.FraudCheckMock;
import database.dao.AccountDao;
import generators.TestConstants;
import iteration2.api.fixtures.FraudTransferFixture;
import models.AccountResponse;
import models.FraudTransferStatus;
import models.TransferWithFraudCheckResponse;
import models.comparison.ModelAssertions;
import org.junit.jupiter.api.Test;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;

public class TransferWithFraudCheckTest extends BaseTest {

    private final AdminSteps adminSteps = new AdminSteps();

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCanTransferMoneyWhenFraudCheckApproved() {
        FraudTransferFixture fixture = prepareFraudTransferFixture(
                TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT
        );

        double transferAmount = TestConstants.SMALL_DEPOSIT_AMOUNT;

        TransferWithFraudCheckResponse response = fixture.getSenderSteps().transferMoneyWithFraudCheck(
                fixture.getSenderAccount().getId(),
                fixture.getReceiverAccount().getId(),
                transferAmount
        );

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(fixture.getSenderAccount().getId());
        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(fixture.getReceiverAccount().getId());

        assertApprovedResponse(
                response,
                fixture,
                transferAmount,
                TestConstants.LOW_RISK_TRANSACTION_REASON,
                TestConstants.LOW_FRAUD_RISK_SCORE,
                false
        );

        assertBalancesChanged(senderAfterTransfer, receiverAfterTransfer, fixture, transferAmount);
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "REJECTED",
            riskScore = 0.95,
            reason = "High risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCannotTransferMoneyWhenFraudCheckRejected() {
        FraudTransferFixture fixture = prepareFraudTransferFixture(
                TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT
        );

        double transferAmount = TestConstants.SMALL_DEPOSIT_AMOUNT;

        TransferWithFraudCheckResponse response = fixture.getSenderSteps().transferMoneyWithFraudCheck(
                fixture.getSenderAccount().getId(),
                fixture.getReceiverAccount().getId(),
                transferAmount
        );

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(fixture.getSenderAccount().getId());
        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(fixture.getReceiverAccount().getId());

        assertManualReviewResponse(
                response,
                fixture,
                transferAmount,
                TestConstants.HIGH_RISK_TRANSACTION_REASON,
                TestConstants.HIGH_FRAUD_RISK_SCORE,
                false,
                false
        );

        assertBalancesNotChanged(senderAfterTransfer, receiverAfterTransfer, fixture);
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.4,
            reason = "Additional verification required",
            requiresManualReview = false,
            additionalVerificationRequired = true
    )
    public void userCanTransferMoneyWhenAdditionalVerificationRequired() {
        FraudTransferFixture fixture = prepareFraudTransferFixture(
                TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT
        );

        double transferAmount = TestConstants.SMALL_DEPOSIT_AMOUNT;

        TransferWithFraudCheckResponse response = fixture.getSenderSteps().transferMoneyWithFraudCheck(
                fixture.getSenderAccount().getId(),
                fixture.getReceiverAccount().getId(),
                transferAmount
        );

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(fixture.getSenderAccount().getId());
        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(fixture.getReceiverAccount().getId());

        assertApprovedResponse(
                response,
                fixture,
                transferAmount,
                TestConstants.ADDITIONAL_VERIFICATION_REQUIRED_REASON,
                TestConstants.MEDIUM_FRAUD_RISK_SCORE,
                true
        );

        assertBalancesChanged(senderAfterTransfer, receiverAfterTransfer, fixture, transferAmount);
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCannotTransferMoneyWhenSenderBalanceIsNotEnough() {
        FraudTransferFixture fixture = prepareFraudTransferFixture(
                TestConstants.SMALL_DEPOSIT_AMOUNT
        );

        fixture.getSenderSteps().transferMoneyWithFraudCheckCustomResponse(
                        fixture.getSenderAccount().getId(),
                        fixture.getReceiverAccount().getId(),
                        TestConstants.MORE_THAN_BALANCE_TRANSFER_AMOUNT
                )
                .statusCode(400);

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(fixture.getSenderAccount().getId());
        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(fixture.getReceiverAccount().getId());

        assertBalancesNotChanged(senderAfterTransfer, receiverAfterTransfer, fixture);
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCannotTransferMoneyFromNonExistingSenderAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse receiverAccount = userSteps.createAccount();

        AccountDao receiverBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        userSteps.transferMoneyWithFraudCheckCustomResponse(
                        TestConstants.NON_EXISTING_ACCOUNT_ID,
                        receiverAccount.getId(),
                        TestConstants.SMALL_DEPOSIT_AMOUNT
                )
                .statusCode(403);

        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAfterTransfer.getBalance(),
                receiverBeforeTransfer.getBalance()
        );
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCannotTransferMoneyToNonExistingReceiverAccount() {
        String userToken = adminSteps.createUserAndGetToken();
        UserSteps userSteps = new UserSteps(userToken);

        AccountResponse senderAccount = userSteps.createAccount();

        userSteps.depositMoney(senderAccount.getId(), TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT);

        AccountDao senderBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());

        userSteps.transferMoneyWithFraudCheckCustomResponse(
                        senderAccount.getId(),
                        TestConstants.NON_EXISTING_ACCOUNT_ID,
                        TestConstants.SMALL_DEPOSIT_AMOUNT
                )
                .statusCode(400);

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(senderAccount.getId());

        ModelAssertions.assertBalanceEquals(
                softly,
                senderAfterTransfer.getBalance(),
                senderBeforeTransfer.getBalance()
        );
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void userCannotTransferInvalidAmountWithFraudCheck() {
        FraudTransferFixture fixture = prepareFraudTransferFixture(
                TestConstants.MAX_DEPOSIT_ALLOWED_AMOUNT
        );

        fixture.getSenderSteps().transferMoneyWithFraudCheckCustomResponse(
                        fixture.getSenderAccount().getId(),
                        fixture.getReceiverAccount().getId(),
                        TestConstants.ZERO_AMOUNT
                )
                .statusCode(400);

        AccountDao senderAfterTransfer = databaseSteps.getAccountById(fixture.getSenderAccount().getId());
        AccountDao receiverAfterTransfer = databaseSteps.getAccountById(fixture.getReceiverAccount().getId());

        assertBalancesNotChanged(senderAfterTransfer, receiverAfterTransfer, fixture);
    }

    private FraudTransferFixture prepareFraudTransferFixture(double depositAmount) {
        String senderToken = adminSteps.createUserAndGetToken();
        String receiverToken = adminSteps.createUserAndGetToken();

        UserSteps senderSteps = new UserSteps(senderToken);
        UserSteps receiverSteps = new UserSteps(receiverToken);

        AccountResponse senderAccount = senderSteps.createAccount();
        AccountResponse receiverAccount = receiverSteps.createAccount();

        senderSteps.depositMoney(senderAccount.getId(), depositAmount);

        AccountDao senderBeforeTransfer = databaseSteps.getAccountById(senderAccount.getId());
        AccountDao receiverBeforeTransfer = databaseSteps.getAccountById(receiverAccount.getId());

        return new FraudTransferFixture(
                senderSteps,
                receiverSteps,
                senderAccount,
                receiverAccount,
                senderBeforeTransfer,
                receiverBeforeTransfer
        );
    }

    private void assertApprovedResponse(TransferWithFraudCheckResponse response,
                                        FraudTransferFixture fixture,
                                        double transferAmount,
                                        String fraudReason,
                                        double fraudRiskScore,
                                        boolean requiresVerification) {
        ModelAssertions.assertFieldEquals(
                softly,
                response.getStatus(),
                FraudTransferStatus.APPROVED.name()
        );
        ModelAssertions.assertFieldEquals(
                softly,
                response.getMessage(),
                TestConstants.TRANSFER_APPROVED_MESSAGE
        );
        ModelAssertions.assertFieldEquals(softly, response.getFraudReason(), fraudReason);
        ModelAssertions.assertFieldEquals(softly, response.isRequiresManualReview(), false);
        ModelAssertions.assertFieldEquals(softly, response.isRequiresVerification(), requiresVerification);
        ModelAssertions.assertFieldEquals(softly, response.getSenderAccountId(), fixture.getSenderAccount().getId());
        ModelAssertions.assertFieldEquals(softly, response.getReceiverAccountId(), fixture.getReceiverAccount().getId());
        ModelAssertions.assertBalanceEquals(softly, response.getAmount(), transferAmount);
        ModelAssertions.assertBalanceEquals(softly, response.getFraudRiskScore(), fraudRiskScore);
    }

    private void assertManualReviewResponse(TransferWithFraudCheckResponse response,
                                            FraudTransferFixture fixture,
                                            double transferAmount,
                                            String fraudReason,
                                            double fraudRiskScore,
                                            boolean requiresManualReview,
                                            boolean requiresVerification) {
        ModelAssertions.assertFieldEquals(
                softly,
                response.getStatus(),
                FraudTransferStatus.MANUAL_REVIEW_REQUIRED.name()
        );
        ModelAssertions.assertFieldEquals(
                softly,
                response.getMessage(),
                TestConstants.TRANSFER_REQUIRES_MANUAL_REVIEW_MESSAGE
        );
        ModelAssertions.assertFieldEquals(softly, response.getFraudReason(), fraudReason);
        ModelAssertions.assertFieldEquals(softly, response.isRequiresManualReview(), requiresManualReview);
        ModelAssertions.assertFieldEquals(softly, response.isRequiresVerification(), requiresVerification);
        ModelAssertions.assertFieldEquals(softly, response.getSenderAccountId(), fixture.getSenderAccount().getId());
        ModelAssertions.assertFieldEquals(softly, response.getReceiverAccountId(), fixture.getReceiverAccount().getId());
        ModelAssertions.assertBalanceEquals(softly, response.getAmount(), transferAmount);
        ModelAssertions.assertBalanceEquals(softly, response.getFraudRiskScore(), fraudRiskScore);
    }

    private void assertBalancesChanged(AccountDao senderAfterTransfer,
                                       AccountDao receiverAfterTransfer,
                                       FraudTransferFixture fixture,
                                       double transferAmount) {
        ModelAssertions.assertBalanceEquals(
                softly,
                senderAfterTransfer.getBalance(),
                fixture.getSenderBeforeTransfer().getBalance() - transferAmount
        );

        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAfterTransfer.getBalance(),
                fixture.getReceiverBeforeTransfer().getBalance() + transferAmount
        );
    }

    private void assertBalancesNotChanged(AccountDao senderAfterTransfer,
                                          AccountDao receiverAfterTransfer,
                                          FraudTransferFixture fixture) {
        ModelAssertions.assertBalanceEquals(
                softly,
                senderAfterTransfer.getBalance(),
                fixture.getSenderBeforeTransfer().getBalance()
        );

        ModelAssertions.assertBalanceEquals(
                softly,
                receiverAfterTransfer.getBalance(),
                fixture.getReceiverBeforeTransfer().getBalance()
        );
    }
}