package com.mustadam.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderSummaryPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Locators
    private By orderSummaryHeading = By.cssSelector(
        "div.total_bill_content");
    private By productName = By.cssSelector(
        "div.product_title_block h5");
    private By itemTotal = By.cssSelector(
        "td.text-end[align='end']");
    private By shippingFee = By.cssSelector(
        "td.shipping-fee-div");
    private By vatAmount = By.cssSelector(
        "td.dark_border.text-end");
    private By totalAmount = By.cssSelector(
        "th.text-end");
    private By proceedToPayBtn = By.cssSelector(
        "a.payment_btn");

    public OrderSummaryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    // Verify order summary page is loaded
    public boolean isOrderSummaryPageLoaded() {
        try {
            wait.until(ExpectedConditions
                .visibilityOfElementLocated(
                    By.cssSelector("div.total_bill_content")));
            return true;
        } catch (Exception e) {
            System.out.println("Order summary not loaded, current URL: " 
                + driver.getCurrentUrl());
            return false;
        }
    }

    // Get product name on order summary
    public String getProductName() {
        // Re-find element fresh to avoid stale reference
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(productName));
        return driver.findElement(productName)
            .getAttribute("title");
    }

    // Get item total
    public String getItemTotal() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(itemTotal));
        return driver.findElement(itemTotal)
            .getText().trim();
    }

    // Get shipping fee
    public String getShippingFee() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(shippingFee));
        return driver.findElement(shippingFee)
            .getText().trim();
    }

    // Get VAT amount
    public String getVatAmount() {
        wait.until(ExpectedConditions
            .visibilityOfElementLocated(vatAmount));
        return driver.findElement(vatAmount)
            .getText().trim();
    }

    // Get total amount
    public String getTotalAmount() {
        // Scroll to total amount
        WebElement total = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                totalAmount));
        js.executeScript(
            "arguments[0].scrollIntoView(true);", total);
        return total.getText().trim();
    }

//    // Click proceed to pay
//    public void clickProceedToPay() {
//        WebElement payBtn = wait.until(
//            ExpectedConditions.visibilityOfElementLocated(
//                proceedToPayBtn));
//        js.executeScript(
//            "arguments[0].scrollIntoView(true);", payBtn);
//        wait.until(ExpectedConditions
//            .elementToBeClickable(proceedToPayBtn));
//        payBtn.click();
//    }
}
