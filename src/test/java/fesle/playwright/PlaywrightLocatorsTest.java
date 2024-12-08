package fesle.playwright;


import com.microsoft.playwright.*;

import com.microsoft.playwright.assertions.PlaywrightAssertions;

import org.junit.jupiter.api.*;

import java.util.Arrays;

public class PlaywrightLocatorsTest {
    @DisplayName("Locating elements by text")
    @Nested
    class LocatingElementsByText {

        private Playwright playwright;
        private Browser browser;
        private Page page;

        @BeforeEach
        void openCatalogPage(){
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
            );
            page = browser.newPage();
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @AfterEach
         void tearDown(){
            browser.close();
            playwright.close();
        }

        @DisplayName("Locating an element by text")
        @Test
        void byText() {
            page.getByText("Bolt Cutters");
            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }

        @DisplayName("Locating an element by alternative text")
        @Test
        void byAltTexg()
        {
            page.getByAltText("Combination Pliers");
            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
        }
    }

    @DisplayName("Locating elements by label")
    @Nested
    class LocateElementsByLabel {
        private Playwright playwright;
        private Browser browser;
        private Page page;

        @BeforeEach
        void openCatalogPage(){
             playwright = Playwright.create();
             browser = playwright.chromium().launch(
                     new BrowserType.LaunchOptions()
                             .setHeadless(false)
                             .setSlowMo(2000)
                             .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
             );

             page =browser.newPage();
             page.navigate("https://practicesoftwaretesting.com/");

        }

        @AfterEach
        void tearDown ()
        {
            browser.close();
            playwright.close();
        }

        @DisplayName("Fill out form by label")
        @Test
        void fillOutForm() {
            page.locator("a:text('Contact')").click();
            page.getByLabel("First Name").fill("Obi");
            page.getByLabel("Last Name").fill("Kenobi");
            page.getByLabel("Email address").fill("deathstar@gmail.com");
        }

    }
    @DisplayName("Locating elements by testID")
    @Nested
    class LocateElementsByTestID {
        private Playwright playwright;
        private Browser browser;
        private Page page;



        @BeforeEach
        void setUp(){
            playwright = Playwright.create();
            playwright.selectors().setTestIdAttribute("data-test");
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setSlowMo(2000)
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
            );
            page = browser.newPage();
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @AfterEach
        void tearDown() {
            browser.close();
            playwright.close();
        }
        @DisplayName("find Search field by test ID")
        @Test
        void findSearchByID() {
            page.getByTestId("search-query").fill("test");
            page.getByTestId("search-submit").click();
        }

    }

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocateElementByCSS {
        private Playwright playwright;
        private Browser browser;
        private Page page;

        @BeforeEach
        void setUp() {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(2000)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))

            );
            page = browser.newPage();
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @AfterEach
        void tearDown () {
            browser.close();
            playwright.close();
        }

        @DisplayName("find fist name by #ID")
        @Test
        void findByCssId()
        {
            page.locator("#first_name").fill("Jane");
            PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Jane");
        }

    }

}