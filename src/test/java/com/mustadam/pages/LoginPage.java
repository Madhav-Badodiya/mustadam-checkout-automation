package com.mustadam.pages;

import com.mustadam.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By loginBtn = By.linkText("Login");
    private By emailTab = By.xpath("//button[@data-bs-target='#email_login']");
    private By emailField = By.id("email_email");
    private By passwordField = By.id("password");
    private By signInBtn = By.id("sign_in_btn");
    private By myProfile = By.linkText("My Profile");
    private By myProfileLink = By.cssSelector("a[href='https://dev.mustadam.shop/my-profile']");
    private By logoutBtn = By.cssSelector("a.logout_btn");
    private By confirmLogoutBtn = By.id("confirmLogout");
    private JavascriptExecutor js;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void login(String email, String password) {
        // Only click Login button if not already on signin page
        if (!driver.getCurrentUrl().contains("signin")) {
            wait.until(ExpectedConditions
                .elementToBeClickable(loginBtn)).click();
        }

        // Switch to Email tab
        wait.until(ExpectedConditions
            .elementToBeClickable(emailTab)).click();

        // Enter email
        WebElement emailInput = wait.until(ExpectedConditions
            .visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);

        // Enter password
        WebElement passwordInput = wait.until(ExpectedConditions
            .visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        // Click Sign In
        wait.until(ExpectedConditions
            .elementToBeClickable(signInBtn)).click();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(myProfile));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void logout() {
        wait.until(ExpectedConditions
            .elementToBeClickable(myProfileLink)).click();

        wait.until(ExpectedConditions
            .visibilityOfElementLocated(logoutBtn));
        
        WebElement logoutElement = driver.findElement(logoutBtn);
        js.executeScript(
            "arguments[0].scrollIntoView(true);", logoutElement);
        js.executeScript(
            "arguments[0].click();", logoutElement);

        wait.until(ExpectedConditions
            .elementToBeClickable(confirmLogoutBtn)).click();

        wait.until(ExpectedConditions
            .urlContains("mustadam.shop"));
        driver.get("https://dev.mustadam.shop/signin");
        System.out.println("Logged out successfully");
    }
}
