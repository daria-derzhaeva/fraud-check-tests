package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

public class ResponseSpecs {

    public static final String PROFILE_UPDATED_API_MESSAGE = "Profile updated successfully";
    public static final String PROFILE_UPDATED_UI_MESSAGE = "Name updated successfully";
    public static final String INVALID_PROFILE_NAME_API_MESSAGE = "Name must contain two words with letters only";
    public static final String INVALID_PROFILE_NAME_UI_MESSAGE = "Please enter a valid name";    public static final String DEPOSIT_SUCCESSFULLY = "Successfully deposited";
    public static final String INVALID_DEPOSIT_AMOUNT_MESSAGE = "Please deposit less or equal to 5000";
    public static final String ACCOUNT_NOT_SELECTED_MESSAGE = "Please select an account";
    public static final String INVALID_AMOUNT_MESSAGE = "Please enter a valid amount";

    public static final String TRANSFER_SUCCESSFULLY = "Successfully transferred";
    public static final String INVALID_TRANSFER_MESSAGE = "Invalid transfer";
    public static final String TRANSFER_REQUIRED_FIELDS_MESSAGE = "Please fill all fields and confirm";

    private ResponseSpecs() {
    }

    private static ResponseSpecBuilder defaultResponseBuilder() {
        return new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(5000L));
    }

    public static ResponseSpecification entityWasCreated() {
        return defaultResponseBuilder()
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static ResponseSpecification requestReturnsOK() {
        return defaultResponseBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static ResponseSpecification requestReturnsBadRequest() {
        return defaultResponseBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }

    public static ResponseSpecification withoutStatusCode() {
        return defaultResponseBuilder()
                .build();
    }
}