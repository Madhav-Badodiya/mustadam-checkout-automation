package com.mustadam.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By searchBox = By.id("header-search");
    private By productItems = By.cssSelector("div.product_item");
    private By addToCartBtn = By.cssSelector("button.add_cart_btn");
    private By successToast = By.cssSelector("div.jq-toast-single");
    private By cartIcon = By.cssSelector("a[href*='cart']");
    private JavascriptExecutor js;
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void searchProduct(String productName) {
        WebElement search = wait.until(
            ExpectedConditions.elementToBeClickable(searchBox));
        search.clear();
        search.sendKeys(productName);
        search.sendKeys(Keys.ENTER);
    }

    public String getFirstProductName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productItems));
        WebElement firstProduct = driver.findElements(productItems).get(0);
        return firstProduct.findElement(
            By.cssSelector("h5")).getAttribute("title");
    }

    public void addFirstProductToCart() {
        WebElement firstProduct = driver.findElements(productItems).get(0);

        // Hover to reveal Add to Cart button
        Actions actions = new Actions(driver);
        actions.moveToElement(firstProduct).perform();

        // Use JS click to bypass interception
        WebElement addBtn = wait.until(
            ExpectedConditions.presenceOfElementLocated(addToCartBtn));
        js.executeScript("arguments[0].click();", addBtn);
    }

    public boolean isSuccessToastDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                successToast));
            String toastText = driver.findElement(successToast).getText();
            System.out.println("Toast message: " + toastText);
            return toastText.contains("Product has been added to cart");
        } catch (Exception e) {
            System.out.println("Toast not found: " + e.getMessage());
            return false;
        }
    }

    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }
}
