package iteration2.ui.steps;

import configs.Config;
import iteration2.ui.pages.LoginPage;

public class AdminUiSteps {

    private final LoginPage loginPage = new LoginPage();

    public void loginAsAdmin() {
        loginPage.open();
        loginPage.shouldBeOpened();
        loginPage.setUsername(Config.getAdminUsername());
        loginPage.setPassword(Config.getAdminPassword());
        loginPage.submitLogin();
    }
}