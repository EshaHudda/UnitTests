package com.example.usermanagement;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("User Management End-to-End Tests")
class UserManagementE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String htmlFilePath;

    @BeforeAll
    static void setupWebDriverManager() {
        WebDriverManager.chromedriver().setup();
        // You can choose other browsers as well, e.g., Firefox
        // WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Run in headless mode (no browser UI)
        options.addArguments("--disable-gpu"); // Recommended for headless
        options.addArguments("--window-size=1920,1080"); // Set window size

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Get the absolute path to your HTML file in src/test/resources
        File htmlFile = new File("src/test/resources/index.html");
        htmlFilePath = "file://" + htmlFile.getAbsolutePath();

        driver.get(htmlFilePath);
    }

    @Test
    @DisplayName("Should successfully add a user through the UI and display it")
    void shouldAddUserThroughUI() {
        // Find elements
        WebElement usernameInput = driver.findElement(By.id("usernameInput"));
        WebElement emailInput = driver.findElement(By.id("emailInput"));
        WebElement addUserButton = driver.findElement(By.id("addUserButton"));
        WebElement messageElement = driver.findElement(By.id("message"));
        WebElement userListElement = driver.findElement(By.id("userList"));

        // Input values
        usernameInput.sendKeys("e2eUser");
        emailInput.sendKeys("e2e@example.com");

        // Click button
        addUserButton.click();

        // Assert message
        wait.until(ExpectedConditions.textToBePresentInElement(messageElement, "User 'e2eUser' added successfully!"));
        assertEquals("User 'e2eUser' added successfully!", messageElement.getText());
        assertTrue(messageElement.getCssValue("color").contains("green")); // Check for green color

        // Assert user appears in the list
        wait.until(ExpectedConditions.textToBePresentInElement(userListElement, "Username: e2eUser, Email: e2e@example.com"));
        assertTrue(userListElement.getText().contains("Username: e2eUser, Email: e2e@example.com"));

        // Verify inputs are cleared
        assertEquals("", usernameInput.getAttribute("value"));
        assertEquals("", emailInput.getAttribute("value"));
    }

    @Test
    @DisplayName("Should display error message when trying to add user with empty fields")
    void shouldDisplayErrorForEmptyFields() {
        WebElement addUserButton = driver.findElement(By.id("addUserButton"));
        WebElement messageElement = driver.findElement(By.id("message"));
        WebElement userListElement = driver.findElement(By.id("userList"));

        // Click button without entering anything
        addUserButton.click();

        // Assert error message
        wait.until(ExpectedConditions.textToBePresentInElement(messageElement, "Please enter both username and email."));
        assertEquals("Please enter both username and email.", messageElement.getText());
        assertTrue(messageElement.getCssValue("color").contains("red")); // Check for red color

        // Assert user list remains empty
        assertEquals("No users added yet.", userListElement.getText().trim());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}