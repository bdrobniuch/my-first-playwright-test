package fesle.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class PlaywrightRestAPITest {

    protected static Playwright playwright;
    protected static  Browser browser;
    protected static  Page page;

    @BeforeAll
    static void setUpBrowser(){
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
        page = browser.newPage();

    }

    @DisplayName("playwright let us to mock out API responses")
    @Nested
    class MockingAPIResponses {

        @Test
        @DisplayName("When a search returns a single product")
        void whenASingleItemIsFound(){
            page.route("**/products/search?q=Pliers", route -> {
                route.fulfill(
                        new Route.FulfillOptions().setBody(
                                MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY
                        ).setStatus(200)
                );
            });
            page.navigate("https://practicesoftwaretesting.com/");
            page.getByPlaceholder("Search").fill("Pliers");
            page.getByPlaceholder("Search").press("Enter");

            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")).hasText("Super Pliers");
        }
    }

    @Nested
    class MakingAPICalls {

        record Product(String name, Double price) {

        }

        private static APIRequestContext requestContext;

        @BeforeAll
        public static void setupRequestContext() {
            requestContext = playwright.request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL("https://api.practicesoftwaretesting.com/")
                            .setExtraHTTPHeaders(new HashMap<>() {{
                                put("Accept","application/json");
                                                 }}

                            )
            );
        }

        @DisplayName("Check presence of known products")
        @ParameterizedTest(name = "Checking product = {0}")
                @MethodSource("products")
                void checkKnownProduct(Product product)
        {
            page.navigate("https://practicesoftwaretesting.com/");
            //page.fill("[placeholder='Saerch']", product.name);
            page.getByPlaceholder("Search").fill(product.name);
            page.click("button:has-text('Search')");

            //Check that the product appears with the correct name and price

            Locator productCart = page.locator(".card")
                    .filter(
                            new Locator.FilterOptions()
                                    .setHasText(product.name)
                                    .setHasText(Double.toString(product.price))
                    );
            assertThat(productCart).isVisible();

        }

        static Stream<Product> products() {
            APIResponse response = requestContext.get("/products?page=2");
            Assertions.assertThat(response.status()).isEqualTo(200);

            JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = jsonObject.getAsJsonArray("data");

            return data.asList().stream()
                    .map(jsonElement -> {
                        JsonObject productJson = jsonElement.getAsJsonObject();
                        return new Product(
                                productJson.get("name").getAsString(),
                                productJson.get("price").getAsDouble()
                        );
                    });
        }

    }

}
