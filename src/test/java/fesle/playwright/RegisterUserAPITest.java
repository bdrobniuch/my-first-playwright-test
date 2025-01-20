package fesle.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import fesle.playwright.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private  Gson gson = new Gson();

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

        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        assertSoftly(softly -> {
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
                "address",
                "city",
                "state",
                "country",
                "123",
                "123123",
                "1990-02-02",
                "password",
                "email@wp.pl"
        );

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);

        assertSoftly(softly -> {
                    softly.assertThat(response.status()).isEqualTo(422);
                    softly.assertThat(responseObject.has("first_name")).isTrue();
                    softly.assertThat(responseObject.get("first_name").getAsString()).isEqualTo(    "The first name field is required.");
                }

        );
    }
}
