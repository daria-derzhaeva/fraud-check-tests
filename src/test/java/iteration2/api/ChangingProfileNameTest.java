package iteration2.api;

import database.dao.CustomerDao;
import generators.RandomData;
import models.CreateUserRequest;
import models.UpdateProfileNameResponse;
import models.comparison.ModelAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;
import specs.ResponseSpecs;

public class ChangingProfileNameTest extends BaseTest {

    private final AdminSteps adminSteps = new AdminSteps();

    @Test
    public void userCanChangeProfileNameToValidName() {
        CreateUserRequest user = adminSteps.createUser();
        String userToken = adminSteps.loginCreatedUser(user);
        UserSteps userSteps = new UserSteps(userToken);

        String expectedName = RandomData.getValidName();

        UpdateProfileNameResponse response = userSteps.updateProfileName(expectedName);

        String actualName = userSteps.getProfileName();
        CustomerDao customerFromDb = databaseSteps.getCustomerByUsername(user.getUsername());

        ModelAssertions.assertFieldEquals(
                softly,
                response.getMessage(),
                ResponseSpecs.PROFILE_UPDATED_API_MESSAGE
        );
        ModelAssertions.assertFieldEquals(softly, response.getCustomer().getName(), expectedName);
        ModelAssertions.assertFieldEquals(softly, actualName, expectedName);
        ModelAssertions.assertFieldEquals(softly, customerFromDb.getName(), expectedName);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Darya",
            "Darya Darya Darya",
            "Darya D1",
            "Darya $arya",
            "DaryaDarya"
    })
    public void userCannotChangeProfileNameToInvalidName(String invalidName) {
        CreateUserRequest user = adminSteps.createUser();
        String userToken = adminSteps.loginCreatedUser(user);
        UserSteps userSteps = new UserSteps(userToken);

        String nameBeforeUpdate = userSteps.getProfileName();

        userSteps.updateProfileNameWithBadRequest(invalidName)
                .body(Matchers.equalTo(ResponseSpecs.INVALID_PROFILE_NAME_API_MESSAGE));

        String nameAfterUpdate = userSteps.getProfileName();
        CustomerDao customerFromDb = databaseSteps.getCustomerByUsername(user.getUsername());

        ModelAssertions.assertFieldEquals(softly, nameAfterUpdate, nameBeforeUpdate);
        ModelAssertions.assertFieldEquals(softly, customerFromDb.getName(), nameBeforeUpdate);
    }
}