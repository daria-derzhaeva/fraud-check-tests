package iteration2.api;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import database.DatabaseSteps;

import java.util.List;

public class BaseTest {

    protected SoftAssertions softly;
    protected final DatabaseSteps databaseSteps = new DatabaseSteps();

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                )
        );
    }

    @BeforeEach
    public void setupSoftAssertions() {
        softly = new SoftAssertions();
    }

    @AfterEach
    public void assertAll() {
        softly.assertAll();
    }
}