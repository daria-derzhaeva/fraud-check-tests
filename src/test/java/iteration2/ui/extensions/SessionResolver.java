package iteration2.ui.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Annotation;

public abstract class SessionResolver implements ParameterResolver {

    protected abstract Class<? extends Annotation> getAnnotation();

    protected abstract UiSession createSession();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        boolean hasAnnotation = parameterContext.isAnnotated(getAnnotation());

        Class<?> parameterType = parameterContext.getParameter().getType();

        return hasAnnotation && (
                parameterType.equals(UiSession.class) ||
                        parameterType.equals(String.class)
        );
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        UiSession session = extensionContext
                .getStore(ExtensionContext.Namespace.create(getClass(), extensionContext.getUniqueId()))
                .getOrComputeIfAbsent(
                        getAnnotation().getSimpleName(),
                        key -> createSession(),
                        UiSession.class
                );

        if (parameterContext.getParameter().getType().equals(String.class)) {
            return session.getAuthToken();
        }

        return session;
    }
}