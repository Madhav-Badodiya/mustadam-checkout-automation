package com.mustadam.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts()
              .implicitlyWait(Duration.ofSeconds(10));

        driver.get(ConfigReader.get("url"));

        WebDriverWait wait = new WebDriverWait(driver,
            Duration.ofSeconds(20));

        // Wait for page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("body")));

        // Click language dropdown
        wait.until(ExpectedConditions.elementToBeClickable(
            By.id("dropdownMenuButton1"))).click();

        // Click English
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[value='English']"))).click();

        System.out.println("Language switched to English");
    }
// TearDown logic to quit the driver after the execution
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
