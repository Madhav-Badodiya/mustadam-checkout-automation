package com.mustadam.tests;

import com.mustadam.base.BaseTest;
import com.mustadam.base.ConfigReader;
import com.mustadam.pages.CartPage;
import com.mustadam.pages.HomePage;
import com.mustadam.pages.LoginPage;
import com.mustadam.pages.OrderSummaryPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import java.time.Duration;

public class CheckoutFlowTest extends BaseTest {

    LoginPage loginPage;
    HomePage homePage;
    CartPage cartPage;
    OrderSummaryPage orderSummaryPage;

    // ✅ TEST 1 - Login with valid credentials
    @Test(priority = 1)
    public void testValidLogin() {
        loginPage = new LoginPage(driver);
        loginPage.login(
            ConfigReader.get("email"),
            ConfigReader.get("password"));
        Assert.assertTrue(loginPage.isLoginSuccessful(),
            "Login failed — My Profile not visible!");
    }

    // ✅ TEST 2 - Search product and add to cart
    @Test(priority = 2, dependsOnMethods = "testValidLogin")
    public void testAddProductToCart() {
        homePage = new HomePage(driver);
        homePage.searchProduct(ConfigReader.get("searchProduct"));
        homePage.addFirstProductToCart();
        Assert.assertTrue(homePage.isSuccessToastDisplayed(),
            "Product was not added to cart!");
    }

    // ✅ TEST 3 - Validate product details in cart
    @Test(priority = 3, dependsOnMethods = "testAddProductToCart")
    public void testValidateCartDetails() {
        homePage = new HomePage(driver);
        homePage.goToCart();

        cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isCartPageLoaded(),
            "Cart page did not load!");

        String productName = cartPage.getCartProductName();
        Assert.assertNotNull(productName,
            "Product name is null in cart!");
        Assert.assertFalse(productName.isEmpty(),
            "Product name is empty in cart!");

        String price = cartPage.getCartProductPrice();
        Assert.assertNotNull(price,
            "Product price is null in cart!");

        String quantity = cartPage.getCartProductQuantity();
        Assert.assertEquals(quantity, "1",
            "Product quantity should be 1!");

        System.out.println("Cart Product: " + productName);
        System.out.println("Cart Price: " + price);
        System.out.println("Cart Quantity: " + quantity);
    }

    // ✅ TEST 4 - Proceed to checkout and validate order summary
    @Test(priority = 4, dependsOnMethods = "testValidateCartDetails")
    public void testValidateOrderSummary() {
        cartPage = new CartPage(driver);
        cartPage.clickCheckout();

        orderSummaryPage = new OrderSummaryPage(driver);
        Assert.assertTrue(orderSummaryPage.isOrderSummaryPageLoaded(),
            "Order Summary page did not load!");

        String productName = orderSummaryPage.getProductName();
        Assert.assertNotNull(productName,
            "Product name is null on order summary!");

        String itemTotal = orderSummaryPage.getItemTotal();
        Assert.assertNotNull(itemTotal,
            "Item total is null!");

        String shippingFee = orderSummaryPage.getShippingFee();
        Assert.assertNotNull(shippingFee,
            "Shipping fee is null!");

        String vat = orderSummaryPage.getVatAmount();
        Assert.assertNotNull(vat,
            "VAT amount is null!");

        String totalAmount = orderSummaryPage.getTotalAmount();
        Assert.assertNotNull(totalAmount,
            "Total amount is null!");

        System.out.println("Product: " + productName);
        System.out.println("Item Total: " + itemTotal);
        System.out.println("Shipping Fee: " + shippingFee);
        System.out.println("VAT: " + vat);
        System.out.println("Total Amount: " + totalAmount);
    }

    // ❌ NEGATIVE TEST 1 - Login with invalid credentials
    @Test(priority = 5)
    public void testInvalidLogin() {
        loginPage = new LoginPage(driver);
        loginPage.logout();
        loginPage.login("invalid@gmail.com", "wrongpassword");
        Assert.assertFalse(loginPage.isLoginSuccessful(),
            "Login should have failed with invalid credentials!");
    }

    @Test(priority = 6)
    public void testEmptyCredentialsLogin() {
        // Navigate directly to signin since already logged out
        driver.get("https://dev.mustadam.shop/signin");
        loginPage = new LoginPage(driver);
        loginPage.login("", "");
        Assert.assertFalse(loginPage.isLoginSuccessful(),
            "Login should have failed with empty credentials!");
    }


    // ❌ NEGATIVE TEST 3 - Search with invalid product
    @Test(priority = 7)
    public void testInvalidProductSearch() {
        driver.get("https://dev.mustadam.shop/?lang=en");
        homePage = new HomePage(driver);
        homePage.searchProduct("xyzabc123invalidproduct");

        // Wait for page to settle
        WebDriverWait wait = new WebDriverWait(driver, 
            Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.product_item")),
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.no_product, div.empty"))
            ));
        } catch (Exception e) {
            // No products found is expected
        }

        boolean noProducts = driver.findElements(
            By.cssSelector("div.product_item")).isEmpty();
        Assert.assertTrue(noProducts,
            "Expected no products for invalid search!");
    }
}

