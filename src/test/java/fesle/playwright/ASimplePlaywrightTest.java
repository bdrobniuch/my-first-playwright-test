package fesle.playwright;


import com.microsoft.playwright.*;

import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.Arrays;

//@UsePlaywright(ASimplePlaywrightTest.CustomOptions.class)
public class ASimplePlaywrightTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
                );
                browserContext = browser.newContext();
    }

    @BeforeEach
    public void setUpPage() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown(){
        browser.close();
        playwright.close();
    }
    /*
    public static class CustomOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()

                    .setHeadless(false)

                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setSlowMo(1000)
                                    .setArgs(Arrays.asList("--no-sandbox",
                                            "--disable-gpu",
                                            "--disable-extensions"))
                    );
        }
    }
    */

    @Test
    void shouldShowThePageTitle(){

        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }


    @Test
    void shouldSearchByKeyword() {

        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResults = page.locator(".card").count();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assertions.assertTrue(matchingSearchResults > 0);


    }
}
