package fesle.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
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

        @DisplayName("Should Filter Products By Category")
        @Test
        void shouldFilterProductsByCategory(Page page) {
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();
            page.waitForSelector(".card",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000)
            );
            var filteredProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filteredProducts).contains("Sheet Sander", "Belt Sander", "Circular Saw");
        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName("It should display a toaster message when an item is add to the cart")
        @Test
        void shouldDisplayToasterMessage(Page page) {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            //wait to the toaster message to appear
            PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        @DisplayName("Should update the cart item count")
        void shouldUpdateTheCartItemCount(Page page) {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
            //PlaywrightAssertions.assertThat(page.getByTestId("cart-quantity")).hasText("1");
        }

    }

    @Nested
    class WaitingForAPICalls {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        @DisplayName("Sort by descending price")
        void sortByDescendingPrice(Page page){

            //sort the prices
            //https://api.practicesoftwaretesting.com/products?sort=price,desc&between=price,1,100&page=0
            page.waitForResponse("**/products?sort**",
                    () ->
                        page.getByTestId("sort").selectOption("Price (High - Low)")
                    );


            //find all the prices on the page
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            System.out.println("Product prices: "+productPrices);

            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());

        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$",""));
        }

    }

}
