package fesle.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;


import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AddingItemsToTheCartTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;



    @BeforeEach
    void setUpBrowser() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setSlowMo(2000)
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
        page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com/");
        playwright.selectors().setTestIdAttribute("data-test");
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Searching for pliars")
    @Test
    void searchForPliers() {
        //page.locator("[placeholder='Search']").fill("Pliers");
        //page.locator("[data-test='search-submit']").click();
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        assertThat(page.locator(".card")).hasCount(4);
        List<String> productnames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productnames).allMatch(name -> name.contains("Pliers"));
        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");
        assertThat(outOfStockItem).hasCount(1);
        assertThat(outOfStockItem).hasText("Long Nose Pliers");
    }

}
