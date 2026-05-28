package iteration2.ui.tests;

import iteration2.ui.extensions.UiSession;
import iteration2.ui.steps.AdminUiSteps;
import iteration2.ui.steps.CustomerUiSteps;
import iteration2.ui.utils.UiApiBridge;
import iteration2.ui.utils.UiConfig;
import org.junit.jupiter.api.BeforeAll;

public class BaseUiTest {

    protected final UiApiBridge uiApiBridge = new UiApiBridge();
    protected final CustomerUiSteps customerUiSteps = new CustomerUiSteps();
    protected final AdminUiSteps adminUiSteps = new AdminUiSteps();

    @BeforeAll
    public static void setupUi() {
        UiConfig.setupBrowser();
    }

    protected void loginAsUser(UiSession session) {
        customerUiSteps.loginAsUserByToken(session.getAuthToken());
    }

    protected void loginAsAdmin(UiSession session) {
        customerUiSteps.loginAsUserByToken(session.getAuthToken());
    }
}