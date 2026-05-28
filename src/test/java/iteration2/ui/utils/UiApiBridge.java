package iteration2.ui.utils;

import models.CreateAccountResponse;
import models.CreateUserRequest;
import models.CustomerProfileResponse;
import models.LoginUserRequest;
import org.apache.http.HttpStatus;
import requests.skelethon.Endpoint;
import requests.steps.AdminSteps;
import specs.RequestSpecs;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UiApiBridge {

    private final AdminSteps adminSteps = new AdminSteps();

    public CreateUserRequest createUser() {
        return adminSteps.createUser();
    }

    public String getUserToken(CreateUserRequest user) {
        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        return adminSteps.loginUser(loginUserRequest);
    }

    public void createAccount(CreateUserRequest user) {
        given()
                .spec(RequestSpecs.authAsUser(user.getUsername(), user.getPassword()))
                .post(Endpoint.ACCOUNTS.getPath())
                .then().assertThat()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public void deposit(CreateUserRequest user, long accountId, double amount) {
        given()
                .spec(RequestSpecs.authAsUser(user.getUsername(), user.getPassword()))
                .body(Map.of(
                        "id", accountId,
                        "balance", amount
                ))
                .post(Endpoint.ACCOUNTS_DEPOSIT.getPath())
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    public CreateAccountResponse[] getUserAccounts(CreateUserRequest user) {
        return given()
                .spec(RequestSpecs.authAsUser(user.getUsername(), user.getPassword()))
                .get(Endpoint.CUSTOMER_ACCOUNTS.getPath())
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(CreateAccountResponse[].class);
    }

    public CustomerProfileResponse getCustomerProfile(CreateUserRequest user) {
        return given()
                .spec(RequestSpecs.authAsUser(user.getUsername(), user.getPassword()))
                .get(Endpoint.CUSTOMER_PROFILE.getPath())
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(CustomerProfileResponse.class);
    }
}