package fesle.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@UsePlaywright(ChromeOptions.class)
public class PlaywrightWaitTests {

    @BeforeEach
    void openContactPage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForSelector("[data-test=product-name]");
    }

    @Test
    void shouldShowAllProductNames(Page page){
        List<String> productNames = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
    }

    @Test
    void shouldShowAllProductImages(Page page) {
        List<String> productImagesTitles = page.locator(".card-img-top").all()
                        .stream()
                        .map(img -> img.getAttribute("alt"))
                                .toList();
        Assertions.assertThat(productImagesTitles).contains("Pliers", "Bolt Cutters", "Hammer");
    }
}
