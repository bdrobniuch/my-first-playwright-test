package fesle.playwright;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;

    @BeforeEach
    void setup(Playwright playwright){
        request= playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://api.practicesoftwaretesting.com/")
        );

    }

    @AfterEach
    void tearDown(){
        if (request != null)
        {
            request.dispose();
        }
    }

    @Test
    void should_register_user(){
        User validUser= User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        Assertions.assertThat(response.status()).isEqualTo(201);
        String responseBody = response.text();
        Gson gson = new Gson();
        User createdUser = gson.fromJson(responseBody, User.class);

        Assertions.assertThat(createdUser).isEqualTo(validUser.withPassword(null));
    }
}
