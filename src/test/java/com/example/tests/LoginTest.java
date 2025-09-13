package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.logging.Logger;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(LoginTest.class.getName());

    @BeforeClass
    public void setUp() {
        logger.info("Setting up WebDriver and Chrome options...");

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Good for CI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("Opening the login page...");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @Test
    public void verifyLoginPageTitle() {
        logger.info("Verifying login page title...");

        // Synchronization: wait for login box to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

        String expectedTitle = "OrangeHRM";
        String actualTitle = driver.getTitle();
        logger.info("Page Title: " + actualTitle);

        Assert.assertTrue(actualTitle.contains(expectedTitle), "Title did not match!");
        logger.info("Login page title verification PASSED.");
    }

    @Test
    public void performLoginTest() {
        logger.info("Performing login test...");

        // Wait for username field to be visible
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement password = driver.findElement(By.name("password"));
        WebElement loginBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        username.sendKeys("Admin");
        password.sendKeys("admin123");
        loginBtn.click();

        // Wait for dashboard or some post-login element
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.oxd-userdropdown-tab")));

        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL after login: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("/dashboard"), "Login might have failed!");
        logger.info("Login test PASSED.");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down the driver...");
        if (driver != null) {
            driver.quit();
        }
        logger.info("Driver closed.");
    }
}
