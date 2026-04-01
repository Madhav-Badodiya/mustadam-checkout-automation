# Mustadam E-Commerce Checkout Automation Framework

## Overview
End-to-end automation framework built using **Selenium WebDriver (Java)** with **TestNG** and **Page Object Model (POM)** design pattern to automate the complete checkout flow of [Mustadam](https://dev.mustadam.shop/) — a furniture e-commerce web application.

---

## Tech Stack
| Tool | Purpose |
|------|---------|
| Selenium WebDriver 4.18.1 | UI Automation |
| Java 17 | Programming Language |
| TestNG 7.9.0 | Test Framework |
| WebDriverManager 5.7.0 | Driver Management |
| ExtentReports 5.1.1 | Reporting |
| Maven | Build & Dependency Management |

---

## Framework Structure
```
src/test/java
├── com.mustadam.base
│   ├── BaseTest.java         # Driver setup, teardown, language switching
│   ├── ConfigReader.java     # Reads config.properties
│   └── TestListener.java     # TestNG listener for pass/fail logging
├── com.mustadam.pages
│   ├── LoginPage.java        # Login, logout methods
│   ├── HomePage.java         # Search, hover, add to cart
│   ├── CartPage.java         # Cart validations, checkout
│   └── OrderSummaryPage.java # Order summary validations
└── com.mustadam.tests
    └── CheckoutFlowTest.java # All test cases

src/test/resources
├── config.properties         # URL, credentials, test data
└── testng.xml                # Test suite configuration
```

---

## Test Cases
| # | Test | Type | Status |
|---|------|------|--------|
| 1 | Login with valid credentials | Positive | ✅ Pass |
| 2 | Search product and add to cart | Positive | ✅ Pass |
| 3 | Validate product details in cart | Positive | ✅ Pass |
| 4 | Validate order summary and total amount | Positive | ✅ Pass |
| 5 | Login with invalid credentials | Negative | ✅ Pass |
| 6 | Login with empty credentials | Negative | ✅ Pass |
| 7 | Search with invalid product keyword | Negative | ✅ Pass |

**Total: 7/7 Passing ✅**

---

## How to Run

### Prerequisites
- Java 17+
- Maven
- Chrome Browser

### Steps
```bash
git clone https://github.com/yourusername/mustadam-checkout-automation.git
cd mustadam-checkout-automation
mvn clean test
```

Or run via TestNG XML directly in Eclipse:
- Right click `testng.xml` → Run As → TestNG Suite

---

## Key Design Decisions

### Page Object Model (POM)
Each page of the application has a dedicated Page class containing all locators and interaction methods. Test classes only call high-level methods — keeping tests clean and maintainable.

### Explicit Waits
All element interactions use `WebDriverWait` with `ExpectedConditions` — no hardcoded `Thread.sleep()` anywhere in the framework. Timeouts set to 20 seconds to handle the application's slow loading.

### JavascriptExecutor for Intercepted Clicks
The application has overlapping elements and slow rendering. Where `ElementClickInterceptedException` occurred, `JavascriptExecutor` was used as a deliberate fallback after standard click failed.

### Language Switching
The application defaults to Arabic. The framework automatically switches to English on startup by clicking the website's own language dropdown before any test execution begins.

### Config-Driven
All environment-specific data (URL, credentials, search keyword) is stored in `config.properties` — making it easy to switch environments without touching test code.

---

## Challenges Faced & Resolutions

### 1. Application Defaulting to Arabic
**Issue:** Website always opened in Arabic regardless of `?lang=en` URL parameter.
**Resolution:** Identified the website's own language dropdown (`#dropdownMenuButton1`) and automated clicking the English option in `BaseTest.setUp()` before any test runs.

### 2. ElementClickInterceptedException on Add to Cart
**Issue:** The Add to Cart button was covered by an overlay after hover, causing standard Selenium click to fail.
**Resolution:** Used `Actions` class to hover over the product first, then used `JavascriptExecutor` to fire the click directly on the DOM element, bypassing the overlay.

### 3. Flaky Toast Message Assertion
**Issue:** The success toast after adding to cart was not being detected because the locator `div.toast-body` was incorrect.
**Resolution:** Inspected the actual toast HTML and found it used `jq-toast` library — updated locator to `div.jq-toast-single` and validated text contains "Product has been added to cart."

### 4. StaleElementReferenceException on Order Summary
**Issue:** After clicking Checkout, the page reloaded and previously found elements became stale.
**Resolution:** Re-fetched elements fresh after page navigation using `wait.until(visibilityOfElementLocated())` before each interaction on the Order Summary page.

### 5. Negative Login Tests Failing After Valid Login
**Issue:** After the valid login test, the user remained logged in — so the Login button was not visible for negative test scenarios.
**Resolution:** Implemented a full `logout()` method in `LoginPage` that navigates to My Profile, scrolls to the Logout button, clicks it, confirms via modal popup, then navigates to the signin page directly.

### 6. ElementClickInterceptedException on Checkout Button
**Issue:** The Checkout button in cart was intercepted by a sticky footer overlay.
**Resolution:** Used `scrollIntoView` via `JavascriptExecutor` followed by JS click to ensure the button was properly in view and clickable.

---

## Test Execution Results
```
===============================================
Mustadam Checkout Suite
Total tests run: 7, Passes: 7, Failures: 0, Skips: 0
===============================================
```

---

## Author
**Madhav Badodiya**
QA Automation Engineer | TCS Indore
[LinkedIn](https://linkedin.com/in/madhav-badodiya)
