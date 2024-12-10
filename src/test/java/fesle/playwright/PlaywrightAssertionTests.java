package fesle.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class PlaywrightAssertionTests {


    @DisplayName("Making assertions about the contents of the fields")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage(Page page) {page.navigate("https://practicesoftwaretesting.com/contact");}

        @DisplayName("Checking a value of a field")
        @Test
        void fieldValues(Page page){
            var firstNameField = page.getByLabel("First name");

            firstNameField.fill("Sara");

            assertThat(firstNameField).hasValue("Sara");
            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }


    }

    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForCondition(() -> page.getByTestId("product-name").count()>0);
        }

        @DisplayName("All product prices should have correct value")
        @Test
        void allProductPricesShouldHaveCorrectValue(Page page){
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$","")) ).toList();
            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 0)
                    .doesNotContain(0.0)
                    .allSatisfy(price -> Assertions.assertThat(price)
                            .isGreaterThan(0.0)
                            .isLessThan(1000.0));
        }


    }

}
