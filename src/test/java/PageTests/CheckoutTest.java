package PageTests;

import PageTests.Extensions.ExtensionBaseTest;
import PageTests.TestBase.BaseTest;
import org.PageObjects.Pages.CheckoutPage.CheckoutFinishPage;
import org.PageObjects.Pages.CheckoutPage.CheckoutInformationPage;
import org.PageObjects.Pages.CheckoutPage.CheckoutOverviewPage;
import org.PageObjects.Pages.HomePage.HomePage;
import org.PageObjects.Pages.LoginPage.LoginPage;
import org.PageObjects.Pages.ShoppingCartPage.ShoppingCartPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ExtensionBaseTest.class)
public class CheckoutTest extends BaseTest {
    String[] productsToSelect = {
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie"
    };

    String username = "standard_user";
    String password = "secret_sauce";

    double expectedSubTotal = 113.95;

    String firstName = "Cire";
    String lastName = "May";
    String postalCode = "underwater";

    @Test
    public void Test_1() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(this.username, this.password);
        log.info("Logged in with valid username, password: [" + this.username + ", " + this.password + "]");

        HomePage homePage = new HomePage(driver);
        homePage.waitForHomePage();
        assertTrue(homePage.topNavigationBarDisplayed());
        log.info("Successfully logged in and landed on HomePage");

        ArrayList<String> productsAddedLog = homePage.addProductProductPage(driver, Arrays.asList(this.productsToSelect));
        log.info("Product: " + productsAddedLog + " added to shopping cart");
//        this.addProductsToCart(driver, this.productsToSelect);
        homePage.clickShoppingCart();

        ShoppingCartPage scp = new ShoppingCartPage(driver);
        scp.waitForShoppingCartPage();
        log.info("Landed on Shopping Cart Page");

        assertEquals(scp.getNumberOfProductsInCart(), this.productsToSelect.length);
        log.info("Successfully validated actual cart size: [" + scp.getNumberOfProductsInCart() +
                "] matches expected cart size: [" + this.productsToSelect.length + "]");

        assertEquals(scp.calculateSubTotal(), expectedSubTotal);
        log.info("Successfully validated actual subtotal: [" + scp.calculateSubTotal() +
                "] matches expected subtotal: [" + expectedSubTotal + "]");
        scp.clickCheckoutButton();

        CheckoutInformationPage cip = new CheckoutInformationPage(driver);
        cip.waitForCheckoutInformationPage();
        log.info("Landed on Checkout Information Page");
        cip.clickContinueButton();

        assertTrue(cip.isErrorMsgDisplayed());
        log.info("Successfully validated error message displayed on Checkout Information Page");

        cip.clickCancelButton();
        scp.clickCheckoutButton();

        assertFalse(cip.isErrorMsgDisplayed());
        log.info("Successfully validated error message not displayed on Checkout Information Page");

        cip.setInputFirstName(this.firstName);
        cip.setInputLastName(this.lastName);
        cip.setInputPostalCode(this.postalCode);
        log.info("Entered Checkout Information: [" + this.firstName + ", " +
                this.lastName + ", " + this.postalCode + "] ");

        cip.clickContinueButton();

        CheckoutOverviewPage cop = new CheckoutOverviewPage(driver);
        cop.waitForCheckoutOverviewPage();
        log.info("Landed on Checkout Overview Page");

        assertEquals(cop.getNumberOfProductsInCart(), this.productsToSelect.length);
        log.info("Successfully validated actual cart size: [" + scp.getNumberOfProductsInCart() +
                "] matches expected cart size: [" + this.productsToSelect.length + "]");

        assertEquals(cop.getSubTotalLabel(), cop.calculateSubTotal());
        log.info("Successfully validated actual subtotal: [" + cop.calculateSubTotal() +
                "] matches expected subtotal: [" + cop.getSubTotalLabel() + "]");

        assertEquals(cop.getSummaryTaxTotalLabel(), cop.calculateTaxAndSubTotal());
        log.info("Successfully actual tax total: [" + cop.calculateTaxAndSubTotal() +
                "] matches expected subtotal: [" + cop.getSummaryTaxTotalLabel() + "]");

        assertEquals(cop.getSummaryTotalLabel(), cop.calculateTotal());
        log.info("Successfully actual total summary: [" + cop.calculateTotal() +
                "] matches expected total summary: [" + cop.getSummaryTotalLabel() + "]");

        cop.clickFinishButton();

        CheckoutFinishPage cfp = new CheckoutFinishPage(driver);
        cfp.waitCheckoutFinishPage();
        log.info("Landed on Checkout Finish Page");

        assertTrue(cfp.verifyFinishPageDisplayed());
        log.info("Successfully validated Checkout Finish Page is displayed");
    }
}
