package fesle.playwright.login;

import fesle.playwright.PlaywrightTestCase;
import fesle.playwright.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {
    @Test
    @DisplayName("Should be able to login with a registered user")
    void should_login_registered_user() {
        //register user via API
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        //login using login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);
        //Check that we are on the right account page
        Assertions.assertThat(loginPage.title()).isEqualTo("My account");
    }

    @Test
    @DisplayName("Should reject user with invalid passoword")
    void should_reject_user_with_invalid_password() {
        //register user via API
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        //login using login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("wrong password"));
        //Check that we are on the right account page
        Assertions.assertThat(loginPage.loginPageError()).isEqualTo("Invalid email or password");
    }
}
