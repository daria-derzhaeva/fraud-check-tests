package iteration2.ui.extensions;

import models.CreateUserRequest;
import models.LoginUserRequest;
import requests.steps.AdminSteps;

import java.lang.annotation.Annotation;

public class UserSessionExtension extends SessionResolver {

    private final AdminSteps adminSteps = new AdminSteps();

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return UserSession.class;
    }

    @Override
    protected UiSession createSession() {
        CreateUserRequest user = adminSteps.createUser();

        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String authToken = adminSteps.loginUser(loginUserRequest);

        return new UiSession(user, authToken);
    }
}