package fesle.playwright;

import com.microsoft.playwright.junit.UsePlaywright;

import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class PlaywrightFormsTest {

    @BeforeEach
    void openContentPage(Page page) {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Complete form")
    @Test
    void completeForm(Page page) throws IOException, URISyntaxException {
        var firstNameField = page.getByLabel("First name");
        firstNameField.fill("Sarah");
        assertThat(firstNameField).hasValue("Sarah");

        var lastNameField = page.getByLabel("Last name");
        lastNameField.fill("Smith");
        assertThat(lastNameField).hasValue("Smith");

        var emailField = page.getByLabel("Email address");
        emailField.fill("deathstar@gmail.com");
        assertThat(emailField).hasValue("deathstar@gmail.com");

        var messageField = page.getByLabel("Message");
        messageField.fill("Hello World!");
        assertThat(messageField).hasValue("Hello World!");

        var subjectField = page.getByLabel("Subject");
        subjectField.selectOption("warranty");
        assertThat(subjectField).hasValue("warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);
        var uploadField = page.getByLabel("Attachment");
        String uploadedFile = uploadField.inputValue();
        Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");

    }

    @DisplayName("Mandatory fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name","Last name","Email","Message"})
    void mandatoryFields(String fieldName, Page page){
        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var sendButton = page.getByText("Send");

        //Fill field values
        firstNameField.fill("Sarah");
        lastNameField.fill("Smith");
        emailField.fill("deathstar@gmail.com");
        messageField.fill("Hello World!");

        //clear one of the fields
        page.getByLabel(fieldName).clear();
        sendButton.click();

        //check the error message for that field


        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName+" is required");
        assertThat(errorMessage).isVisible();

    }

}
