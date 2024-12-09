package StepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import org.PageObjects.Pages.CheckoutPage.CheckoutInformationPage;
import org.PageObjects.Pages.ShoppingCartPage.ShoppingCartPage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartSteps {
    String[] productsToSelect = {
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie"
    };

    String[] productsToRemove = {
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie"
    };

    double expectedSubTotal = 113.95;

    TestContext context;

    public ShoppingCartSteps(TestContext context) {
        this.context = context;
    }

    /*
     * Adding tags to Before and After annotations specifies that these methods will only
     * execute on feature files tagged with the specified name in the annotation
     */
    @Before("@ShoppingCart")
    public void setup() {
        this.context.setup();
    }

    @After("@ShoppingCart")
    public void tearDown() {
        this.context.cleanUp();
    }

    @And("Shopping Cart Page Is Displayed")
    public void shoppingCarPageDisplayed() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
        scp.waitForShoppingCartPage();
    }

    @And("Validate Number of Items In Cart")
    public void validateNumItemsInCart() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
        assertEquals(this.context.numPurchasedItems, scp.getNumberOfProductsInCart());
    }

    @And("Remove Products In Shopping Cart")
    public void removeProducts() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
//        this.removeProductsShoppingCart(this.context.driver, productsToRemove);
        ArrayList<String> removedProductsList = scp.removeProductsShoppingCart(this.context.driver, productsToRemove);
        this.context.numPurchasedItems -= removedProductsList.size();

        assertEquals(0, scp.getNumberOfProductsInCart());
    }

    @And("User Returns To Home Page From Shopping Cart Page")
    public void continueShoppingButton() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
        scp.clickContinueShoppingButton();
    }

    @And("User Begins Checkout")
    public void beginCheckout() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
        scp.clickCheckoutButton();

        CheckoutInformationPage cip = new CheckoutInformationPage(this.context.driver);
        cip.waitForCheckoutInformationPage();
    }

    @And("Validate Total Cost Of Items Purchased")
    public void validateTotalItemsPurchased() {
        ShoppingCartPage scp = new ShoppingCartPage(this.context.driver);
        scp.waitForShoppingCartPage();

        assertEquals(expectedSubTotal, scp.calculateSubTotal());
    }
}
