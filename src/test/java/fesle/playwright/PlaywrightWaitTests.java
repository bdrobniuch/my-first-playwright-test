package fesle.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@UsePlaywright(ChromeOptions.class)
public class PlaywrightWaitTests {

    @Nested
    class WaitingForState {

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

    @Nested
    class AutomaticWaiting {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName( "Shoudl wait for the filter checkbox option to appear before clicking")
        @Test
        void shouldVaidForTheFilterCheckboxes (Page page) {
            var screwDriverFilter = page.getByLabel("Screwdriver");
            screwDriverFilter.click();
            PlaywrightAssertions.assertThat(screwDriverFilter).isChecked();
        }
    }

}
