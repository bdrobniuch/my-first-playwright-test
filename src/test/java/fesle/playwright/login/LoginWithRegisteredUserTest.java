package fesle.playwright.login;

import fesle.playwright.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {
    @Test
    @DisplayName("Should be able to login with a registered user")
    void should_login_registered_user() {
        //register user via API

        //login using login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        //Check that we are on the right account page
    }
}
