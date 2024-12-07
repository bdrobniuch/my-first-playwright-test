package fesle.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ASimplePlaywrightTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup ()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @AfterEach
    void teardown(){
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitle(){

        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @Test
    void shouldSearchByKeyword() {

        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("dddd");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResults = page.locator(".card").count();

        Assertions.assertTrue(matchingSearchResults > 0);


    }
}
