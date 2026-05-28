package iteration2.ui.steps;

import com.codeborne.selenide.Selenide;
import iteration2.ui.pages.DashboardPage;
import iteration2.ui.pages.DepositPage;
import iteration2.ui.pages.EditProfilePage;
import iteration2.ui.pages.LoginPage;
import iteration2.ui.pages.TransferPage;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class CustomerUiSteps {

    private final LoginPage loginPage = new LoginPage();
    private final DashboardPage dashboardPage = new DashboardPage();
    private final DepositPage depositPage = new DepositPage();
    private final TransferPage transferPage = new TransferPage();
    private final EditProfilePage editProfilePage = new EditProfilePage();

    public void loginAsUser(String username, String password) {
        loginPage.open();
        loginPage.shouldBeOpened();
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.submitLogin();
        dashboardPage.shouldBeOpened();
    }

    public void loginAsUserByToken(String authToken) {
        Selenide.open("/login");

        executeJavaScript(
                "window.localStorage.setItem('authToken', arguments[0]);",
                authToken
        );

        Selenide.refresh();

        Selenide.open("/dashboard");

        System.out.println("CURRENT URL = " + Selenide.webdriver().driver().url());
        System.out.println("AUTH TOKEN = " + executeJavaScript(
                "return window.localStorage.getItem('authToken');"
        ));

        dashboardPage.shouldBeOpened();
    }

    public void openDepositPage() {
        dashboardPage.openDepositPage();
        depositPage.shouldBeOpened();
    }

    public void depositMoney(String amount) {
        depositPage.selectFirstAccount();
        depositPage.setAmount(amount);
        depositPage.submitDeposit();
    }

    public void depositMoneyWithoutSelectedAccount(String amount) {
        depositPage.setAmount(amount);
        depositPage.submitDeposit();
    }

    public void submitDepositWithoutAmount() {
        depositPage.selectFirstAccount();
        depositPage.submitDeposit();
    }

    public void openTransferPage() {
        dashboardPage.openTransferPage();
        transferPage.shouldBeOpened();
    }

    public void transferMoney(String recipientName, String recipientAccountNumber, String amount) {
        transferPage.selectFirstSenderAccount();
        transferPage.setRecipientName(recipientName);
        transferPage.setRecipientAccountNumber(recipientAccountNumber);
        transferPage.setAmount(amount);
        transferPage.confirmTransfer();
        transferPage.submitTransfer();
    }

    public void transferMoneyWithoutSelectedSenderAccount(
            String recipientName,
            String recipientAccountNumber,
            String amount
    ) {
        transferPage.setRecipientName(recipientName);
        transferPage.setRecipientAccountNumber(recipientAccountNumber);
        transferPage.setAmount(amount);
        transferPage.confirmTransfer();
        transferPage.submitTransfer();
    }

    public void transferMoneyWithoutRecipientAccountNumber(String recipientName, String amount) {
        transferPage.selectFirstSenderAccount();
        transferPage.setRecipientName(recipientName);
        transferPage.setAmount(amount);
        transferPage.confirmTransfer();
        transferPage.submitTransfer();
    }

    public void transferMoneyWithoutConfirmation(
            String recipientName,
            String recipientAccountNumber,
            String amount
    ) {
        transferPage.selectFirstSenderAccount();
        transferPage.setRecipientName(recipientName);
        transferPage.setRecipientAccountNumber(recipientAccountNumber);
        transferPage.setAmount(amount);
        transferPage.submitTransfer();
    }

    public void openEditProfilePage() {
        editProfilePage.open();
        editProfilePage.shouldBeOpened();
    }

    public void changeProfileName(String name) {
        editProfilePage.setProfileName(name);
        editProfilePage.submitProfileName();
    }
}