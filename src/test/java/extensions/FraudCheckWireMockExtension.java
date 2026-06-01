package extensions;

import annotations.FraudCheckMock;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FraudCheckWireMockExtension implements BeforeEachCallback, AfterEachCallback {

    private WireMockServer wireMockServer;
    private String responseBody;

    @Override
    public void beforeEach(ExtensionContext context) {
        FraudCheckMock mockConfig = context.getTestMethod()
                .map(method -> method.getAnnotation(FraudCheckMock.class))
                .orElseGet(() -> context.getTestClass()
                        .map(clazz -> clazz.getAnnotation(FraudCheckMock.class))
                        .orElse(null));

        if (mockConfig != null) {
            setupWireMock(mockConfig);
        }
    }

    private void setupWireMock(FraudCheckMock config) {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().port(config.port())
        );

        wireMockServer.start();

        WireMock.configureFor("localhost", config.port());

        responseBody = String.format(Locale.US, """
                        {
                          "status": "%s",
                          "decision": "%s",
                          "riskScore": %.2f,
                          "reason": "%s",
                          "requiresManualReview": %s,
                          "additionalVerificationRequired": %s
                        }
                        """,
                config.status(),
                config.decision(),
                config.riskScore(),
                config.reason(),
                config.requiresManualReview(),
                config.additionalVerificationRequired()
        );

        stubFor(post(urlPathEqualTo("/fraud-check"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (wireMockServer != null) {
            System.out.println("WireMock requests count: " + wireMockServer.getAllServeEvents().size());
            System.out.println("WireMock response body:");
            System.out.println(responseBody);

            wireMockServer.getAllServeEvents()
                    .forEach(event -> {
                        System.out.println("WireMock request:");
                        System.out.println(event.getRequest().getMethod() + " " + event.getRequest().getUrl());
                        System.out.println(event.getRequest().getBodyAsString());
                    });

            wireMockServer.stop();
        }
    }
}