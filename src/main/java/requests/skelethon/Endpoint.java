package requests.skelethon;

public enum Endpoint {

    CREATE_USER("/admin/users"),
    LOGIN("/auth/login"),

    CREATE_ACCOUNT("/accounts"),
    ACCOUNTS("/accounts"),

    DEPOSIT("/accounts/deposit"),
    ACCOUNTS_DEPOSIT("/accounts/deposit"),

    TRANSFER("/accounts/transfer"),
    TRANSFER_WITH_FRAUD_CHECK("/accounts/transfer-with-fraud-check"),

    CUSTOMER_ACCOUNTS("/customer/accounts"),
    CUSTOMER_PROFILE("/customer/profile");

    private final String url;

    Endpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return url;
    }
}