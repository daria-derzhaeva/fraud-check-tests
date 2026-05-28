package iteration2.ui.extensions;

import configs.Config;
import models.CreateUserRequest;

import java.lang.annotation.Annotation;

public class AdminSessionExtension extends SessionResolver {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return AdminSession.class;
    }

    @Override
    protected UiSession createSession() {
        CreateUserRequest admin = CreateUserRequest.builder()
                .username(Config.getAdminUsername())
                .password(Config.getAdminPassword())
                .role("ADMIN")
                .build();

        return new UiSession(admin, Config.getAdminAuth());
    }
}