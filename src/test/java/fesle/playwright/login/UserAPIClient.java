package fesle.playwright.login;

import com.microsoft.playwright.Page;
import fesle.playwright.domain.User;

public class UserAPIClient {

    private final Page page;
    private static final String REGISTER_USER = "https://api.practicesoftwaretesting.com/users/register";

    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
        var response = page.request().post(REGISTER_USER)
    }
}
