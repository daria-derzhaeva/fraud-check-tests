package iteration2.ui.utils;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class UiTestData {

    public static final String MAX_DEPOSIT_AMOUNT = "5000.00";
    public static final String GREATER_THAN_MAX_DEPOSIT_AMOUNT = "5000.01";

    public static final String MAX_TRANSFER_AMOUNT = "10000.00";
    public static final String GREATER_THAN_MAX_TRANSFER_AMOUNT = "10000.01";

    public static final String INVALID_PROFILE_NAME = "John Smith1";

    public static final String INSUFFICIENT_FUNDS_BALANCE = "100.00";
    public static final String GREATER_THAN_BALANCE_TRANSFER_AMOUNT = "200.00";

    public static final double TRANSFER_BALANCE_RESERVE = 100.00;

    private UiTestData() {
    }

    public static String validProfileName() {
        return randomCapitalizedWord() + " " + randomCapitalizedWord();
    }

    public static String invalidProfileName() {
        return validProfileName() + randomDigit();
    }

    private static int randomDigit() {
        return ThreadLocalRandom.current().nextInt(10);
    }

    public static double randomDepositAmount() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }

    public static double randomTransferAmount() {
        return ThreadLocalRandom.current().nextInt(1, 500);
    }

    public static String amountAsText(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static double amountAsDouble(String amount) {
        return Double.parseDouble(amount);
    }

    private static String randomCapitalizedWord() {
        int length = ThreadLocalRandom.current().nextInt(4, 10);

        StringBuilder word = new StringBuilder();
        word.append(randomUppercaseLetter());

        for (int i = 1; i < length; i++) {
            word.append(randomLowercaseLetter());
        }

        return word.toString();
    }

    private static char randomUppercaseLetter() {
        return (char) ThreadLocalRandom.current().nextInt('A', 'Z' + 1);
    }

    private static char randomLowercaseLetter() {
        return (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1);
    }
}