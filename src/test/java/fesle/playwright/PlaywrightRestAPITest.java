package fesle.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;

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

    }

}
