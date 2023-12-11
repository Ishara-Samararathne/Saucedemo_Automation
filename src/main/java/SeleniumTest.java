import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {

        System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");

    }

    @Test(priority = 0)
    public void loginpage() {
                /*
        Login verification
         */

        //entering username and password.
        WebElement usernametxt = driver.findElement(By.xpath("//*[@id=\"user-name\"]"));
        usernametxt.sendKeys("standard_user");
        WebElement txtpassword = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        txtpassword.sendKeys("secret_sauce");

        //Login button click.
        WebElement btnLogin = driver.findElement(By.cssSelector("#login-button"));
        btnLogin.click();
    }

    @Test(priority = 1)
    public void checkingShoping() {


        // URL being verified.
        String currentURL = driver.getCurrentUrl();
        String givenURL = "https://www.saucedemo.com/inventory.html";
        System.out.println("Verifing URL");
        Assert.assertEquals(currentURL, givenURL);

        /*
        Adding a product to a cart and verifying its being added to the cart correctly
         */
        //Reaching the product page
        driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).click();
        WebElement lblname = driver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div/div[1]"));

        String givename = "Sauce Labs Backpack";
        String currentprice = driver.findElement(By.cssSelector("#inventory_item_container > div > div > div > div.inventory_details_price")).getText();
        String givenprice = "$29.99";

        //verifying price
        System.out.println("cheching prices");
        Assert.assertEquals(currentprice, givenprice);

        //verifying name
        System.out.println("Checking names");
        Assert.assertEquals(lblname.getText(), givename);

        //adding the item to cart from the product page by clicking add to cart button
        driver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div/button")).click();

        //accessing the cart by clicking on the top right cart icon
        driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]")).click();

        //verifying the cart URL
        String givenURL1 = "https://www.saucedemo.com/cart.html";
        System.out.println("Verifing URL cart");
        Assert.assertEquals(driver.getCurrentUrl(), givenURL1);

        /*Verifying in cart name and price
         */
        WebElement lblcartPrice = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div"));
        String inCartname = lblcartPrice.getText();

        //Verifying in cart name
        System.out.println("checking cart item names");
        Assert.assertEquals(givename, inCartname);

        //Verifying in cart price
        double inCartPrice = Double.parseDouble(driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText());
        double exeptedPrice = 29.99;
        System.out.println("Checking cart item prices");
        Assert.assertEquals(inCartPrice, exeptedPrice);

        //removing the item from the cart
        WebElement btnRemove = driver.findElement(By.cssSelector("#cart_contents_container > div > div.cart_list > div.cart_item > div.cart_item_label > div.item_pricebar > button"));
        btnRemove.click();

        /*
        verifying it is removed
        */
        boolean isDisplayed = true;
        try {
            lblcartPrice.getText();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            isDisplayed = false;
        }

        boolean exceptedDisplayed = false;

        System.out.println("Verifying");
        Assert.assertEquals(isDisplayed, exceptedDisplayed); //boolean value being compared.

        //coming back to inventory page by clicking on continue shopping button
        driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[2]/a[1]")).click();

        // adding two items to cart(top 2 items were added)
        driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[3]/button")).click();
        driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[2]/div[3]/button")).click();


        //reaching cart page
        driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();


        //verifying names and prices from cart, of each of 2 items

//        given names
        String givenItem1name = "Sauce Labs Backpack";
        double giventItem1price = 29.99;
        String givenItem2name = "Sauce Labs Bike Light";
        double givenItem2price = 9.99;

//        currentnames
        String item1currentname = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        double item1currentprice = Double.parseDouble(driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText());
        String item2currentname = driver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div")).getText();
        double item2currentprice = Double.parseDouble(driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText());

//        verifying
        System.out.println("verifing names");
        Assert.assertEquals(item1currentname, givenItem1name);
        Assert.assertEquals(item2currentname, givenItem2name);
        System.out.println("verifing prices");
        Assert.assertEquals(item1currentprice, giventItem1price);
        Assert.assertEquals(item2currentprice, givenItem2price);

        // clicking on continue to checkout
        driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[2]/a[2]")).click();

        //parsing credentials
        driver.findElement(By.cssSelector("#first-name")).sendKeys("firstname");
        driver.findElement(By.cssSelector("#last-name")).sendKeys("secondname");
        driver.findElement(By.cssSelector("#postal-code")).sendKeys("12345");

        //clicking on continue
        driver.findElement(By.xpath("//*[@id=\"checkout_info_container\"]/div/form/div[2]/input")).click();

        //verifying total price
        double exeptedTotalPrice = giventItem1price + givenItem2price;
        String exeptedTotalPriceNeew = "Item total: $" + exeptedTotalPrice;
        String curretItemtotalpice = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[5]")).getText();
        System.out.println("Verifying item total price");
        Assert.assertEquals(exeptedTotalPriceNeew, curretItemtotalpice);

        //clicking on finish
        driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]/a[2]")).click();

        //Verifying the Thank You for your order text is visible.
        driver.findElement(By.cssSelector("#checkout_complete_container > h2")).isDisplayed();

        //verifying the final URL
        String finalExceptedURL = "https://www.saucedemo.com/checkout-complete.html";
        String finalCurrentURL = driver.getCurrentUrl();
        System.out.println("Verifying final urls");
        Assert.assertEquals(finalCurrentURL, finalExceptedURL);

    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }
}
