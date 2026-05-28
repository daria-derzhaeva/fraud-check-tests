package iteration2.ui.extensions;

import models.CreateUserRequest;

public class UiSession {

    private final CreateUserRequest user;
    private final String authToken;

    public UiSession(CreateUserRequest user, String authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public CreateUserRequest getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }
}