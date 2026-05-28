package models.comparison;

import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.within;

public class ModelAssertions {

    private ModelAssertions() {
    }

    public static void assertBalanceEquals(SoftAssertions softly,
                                           double actualBalance,
                                           double expectedBalance) {
        softly.assertThat(actualBalance)
                .isCloseTo(expectedBalance, within(0.001));
    }

    public static void assertFieldEquals(SoftAssertions softly,
                                         Object actual,
                                         Object expected) {
        softly.assertThat(actual).isEqualTo(expected);
    }
}