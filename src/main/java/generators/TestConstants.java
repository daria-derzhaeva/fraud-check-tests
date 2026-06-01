package generators;

public class TestConstants {

    public static final int NON_EXISTING_ACCOUNT_ID = 999999;

    public static final double ZERO_AMOUNT = 0.0;
    public static final double NEGATIVE_DEPOSIT_AMOUNT = -200.0;
    public static final double NEGATIVE_TRANSFER_AMOUNT = -100.0;

    public static final double MAX_DEPOSIT_ALLOWED_AMOUNT = 5000.00;
    public static final double MAX_TRANSFER_ALLOWED_AMOUNT = 10000.00;
    public static final double MORE_THAN_MAX_TRANSFER_AMOUNT = 10000.01;

    public static final double SMALL_DEPOSIT_AMOUNT = 100.00;
    public static final double MORE_THAN_BALANCE_TRANSFER_AMOUNT = 1000.00;

    public static final double LOW_FRAUD_RISK_SCORE = 0.2;
    public static final double MEDIUM_FRAUD_RISK_SCORE = 0.4;
    public static final double HIGH_FRAUD_RISK_SCORE = 0.95;

    public static final String LOW_RISK_TRANSACTION_REASON = "Low risk transaction";
    public static final String HIGH_RISK_TRANSACTION_REASON = "High risk transaction";
    public static final String ADDITIONAL_VERIFICATION_REQUIRED_REASON = "Additional verification required";

    public static final String TRANSFER_APPROVED_MESSAGE = "Transfer approved and processed immediately";
    public static final String TRANSFER_REQUIRES_MANUAL_REVIEW_MESSAGE = "Transfer requires manual review";

    private TestConstants() {
    }
}