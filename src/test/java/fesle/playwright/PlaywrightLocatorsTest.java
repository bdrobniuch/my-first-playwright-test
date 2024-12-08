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
        public void tearDown(){
            browser.close();
            playwright.close();
        }

        @DisplayName("Locating an element by text")
        @Test
        void byText() {
            page.getByText("Bolt Cutters");
            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }
    }
}