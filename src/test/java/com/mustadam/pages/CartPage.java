package com.mustadam.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Locators
    private By productName = By.cssSelector(
        "div.product_title_block h5");
    private By productPrice = By.cssSelector(
        "div.total_product_ptice span.d-block");
    private By productQuantity = By.cssSelector(
        "div.product_qty input[type='number']");
    private By subtotalAmount = By.cssSelector(
        "div.total_bill");
    private By checkoutBtn = By.cssSelector(
        "a.checkout_btn");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // Validate product name in cart
    public String getCartProductName() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(productName));
        return driver.findElement(productName)
            .getAttribute("title");
    }

    // Validate product price in cart
    public String getCartProductPrice() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(productPrice));
        return driver.findElement(productPrice)
            .getText().trim();
    }

    // Validate quantity in cart
    public String getCartProductQuantity() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(productQuantity));
        return driver.findElement(productQuantity)
            .getAttribute("value");
    }

    // Validate subtotal amount
    public String getSubtotalAmount() {
        // Scroll down to price details section
        WebElement subtotal = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                subtotalAmount));
        js.executeScript(
            "arguments[0].scrollIntoView(true);", subtotal);
        return subtotal.getText();
    }

    // Click checkout button
    public void clickCheckout() {
        WebElement checkout = wait.until(
            ExpectedConditions.presenceOfElementLocated(checkoutBtn));
        js.executeScript("arguments[0].scrollIntoView(true);", checkout);
        js.executeScript("arguments[0].click();", checkout);
    }

    // Verify cart page is loaded
    public boolean isCartPageLoaded() {
        try {
            wait.until(ExpectedConditions
                .visibilityOfElementLocated(productName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
  //Check if any error message appears on checkout click
    public String getCheckoutError() {
     try {
         By errorMsg = By.cssSelector("div.general-error");
         wait.until(ExpectedConditions
             .visibilityOfElementLocated(errorMsg));
         return driver.findElement(errorMsg).getText();
     } catch (Exception e) {
         return "";
     }
    }
}

