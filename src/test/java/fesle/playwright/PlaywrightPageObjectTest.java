package fesle.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightPageObjectTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Nested
    class WhenSearchingProductsByKeyword {

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {
            page.waitForResponse("**/products/search?q=tape", () -> {
                page.getByPlaceholder("Search").fill("tape");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
            List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(matchingProducts)
                    .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");

        }


        @DisplayName("With Page Objects")
        @Test
        void withPageObjects() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);

            searchComponent.searchBy("tape");

            var matchingProducts = productList.getProductNames();

           Assertions.assertThat(matchingProducts)
                    .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
        }


    }

    @Nested
    class WhenAddingItemsToTheCart {
        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {
            //Search for pliers
            page.waitForResponse("**/products/search?q=pliers", () -> {
                page.getByPlaceholder("Search").fill("pliers");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
            //Show the details page
            page.locator(".card").getByText("Combination Pliers").click();

            //Increase cart quantity
            page.getByTestId("increase-quantity").click();
            page.getByTestId("increase-quantity").click();

            //Add to cart
            page.getByTestId("add-to-cart").click();

            page.waitForCondition(()-> page.getByTestId("cart-quantity").textContent().equals("3"));

            //Open the cart
            page.getByTestId("nav-cart").click();

            //check cart context
            assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
            assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();

        }

        @DisplayName("With Page Objects")
        @Test
        void withPageObjects() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            ProductDetails productDetails = new ProductDetails(page);
            NavBar navBar = new NavBar(page);

            searchComponent.searchBy("pliers");

            productList.viewProductDetails("Combination Pliers");

            productDetails.increaseQuantityBy(2);
            productDetails.addToCart();

            navBar.openCart();

            assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
            assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
        }
    }

    class SearchComponent {
        private final Page page;

        SearchComponent(Page page) {
            this.page = page;
        }

        public void searchBy(String tape) {
            page.waitForResponse("**/products/search?q=" +tape, () -> {
                page.getByPlaceholder("Search").fill(tape);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
        }
    }

    class ProductList {
        private final Page page;

        ProductList(Page page) {
            this.page = page;
        }

        public List<String>  getProductNames() {
            return page.getByTestId("product-name").allInnerTexts();
        }

        public void viewProductDetails(String productName) {
            page.locator(".card").getByText(productName).click();
        }
    }

    class ProductDetails {
        private final Page page;

        ProductDetails(Page page) {
            this.page = page;
        }

        public void increaseQuantityBy(int quantity) {
            while (quantity>0) {
                page.getByTestId("increase-quantity").click();
                quantity--;
            }
        }

        public void addToCart() {

            page.waitForResponse(
                    response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                            ()-> page.getByTestId("add-to-cart").click()
            );


        }
    }

    class NavBar {
        private final Page page;

        NavBar(Page page) {
            this.page = page;
        }

        public void openCart() {
            page.getByTestId("nav-cart").click();
        }
    }
}
