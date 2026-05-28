package requests.skelethon;

public enum Endpoint {

    CREATE_USER("/admin/users"),
    LOGIN("/auth/login"),

    CREATE_ACCOUNT("/accounts"),
    ACCOUNTS("/accounts"),

    DEPOSIT("/accounts/deposit"),
    ACCOUNTS_DEPOSIT("/accounts/deposit"),

    TRANSFER("/accounts/transfer"),

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