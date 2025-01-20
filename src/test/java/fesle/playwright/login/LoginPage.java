package fesle.playwright.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import fesle.playwright.domain.User;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(User user){
        page.getByPlaceholder("Your email").fill(user.email());
        page.getByPlaceholder("Your password").fill(user.password());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

    }

    public String title() {
        return page.getByTestId("page-title").textContent();
    }

    public String loginPageError() {
        return page.getByTestId("login-error").textContent();
    }
}
