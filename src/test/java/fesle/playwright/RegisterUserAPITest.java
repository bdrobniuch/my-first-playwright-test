package fesle.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;
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

        String responseBody = response.text();
        Gson gson = new Gson();
        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should create a 201 status code")
                    .isEqualTo(201);
            softly.assertThat(createdUser)
                    .as("created user should match the specified user without a passwoerd")
                    .isEqualTo(validUser.withPassword(null));
            softly.assertThat(responseObject.has("password"))
                            .as("no password should be returned")
                                    .isFalse();
            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();
            softly.assertThat(
                    response.headers().get("content-type")
            ).contains("application/json");
        });
    }

    @Test
    void first_name_is_mandatory() {
        User userWithNoName = new User(
                null,
                "Smith",

        )
    }
}
