package annotations;

import extensions.FraudCheckWireMockExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
@ExtendWith(FraudCheckWireMockExtension.class)
public @interface FraudCheckMock {

    int port() default 8080;

    String status() default "SUCCESS";

    String decision() default "APPROVED";

    double riskScore() default 0.2;

    String reason() default "Low risk transaction";

    boolean requiresManualReview() default false;

    boolean additionalVerificationRequired() default false;
}