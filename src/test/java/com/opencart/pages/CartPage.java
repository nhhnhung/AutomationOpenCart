package com.opencart.pages;

import com.opencart.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CartPage extends BasePage {

    SearchPage searchPage;

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
        searchPage = new SearchPage(driver);
    }

    // Thông báo

    @FindBy(css = ".alert-success")
    private WebElement successMessage;

    @FindBy(css = ".alert-success .close")
    private WebElement closeMsgBtn;

    // Giỏ hàng

    @FindBy(xpath = "//nav[@id='top']//li[4]")
    private WebElement cartBtn;

    @FindBy(xpath = "//table//tbody//tr")
    private List<WebElement> cartRows;

    // Sản phẩm

    @FindBy(css = "#product h3")
    private WebElement optionTitle;

    @FindBy(id = "input-option229")   // color
    private WebElement colorDropdown;

    @FindBy(id = "input-option230")   // RAM
    private WebElement ramDropdown;

    @FindBy(id = "input-option231")   // Storage
    private WebElement storageDropdown;

    @FindBy(id = "button-cart")
    private WebElement addToCartBtn;

    @FindBy(xpath = "//h2[normalize-space()='Products meeting the search criteria']")
    WebElement titleProd;

    @FindBy(xpath = "//div[@class='alert alert-danger alert-dismissible']")
    WebElement msgDanger;


    private WebElement getRowByProduct(String name) {
        return driver.findElement(
                By.xpath("//div[@class='table-responsive']//tbody/tr[.//a[normalize-space()='" + name + "']]")
        );
    }

    private Select getSelect(WebElement element) {
        waitForElementVisible(element);
        return new Select(element);
    }

    private double parsePrice(String text) {

        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        String clean = text.replaceAll("[^0-9.]", "");

        if (clean.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(clean);
    }

    @Step("Lấy thông báo nguy hiểm")
    public String getDangerMessage() {
        return msgDanger.getText();
    }


    @Step("Mở giỏ hàng")
    public void openCart() {
        moveToElement(cartBtn);
        clickElement(cartBtn);
    }

    @Step("Kiểm tra thông báo thành công")
    public boolean isSuccessMessageDisplayed() {
        try {
            if (successMessage.isDisplayed()) {
                clickElement(closeMsgBtn);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    @Step("Reload trang")
    public void reloadPage() {
        driver.navigate().refresh();
        waitForPageLoaded();
    }

    //Thêm sản phẩm

    @Step("Thêm imac với màu {0}")
    public void addIMac(String color) {
        searchPage.searchProduct("imac");
        driver.findElement(By.xpath("//a[normalize-space()='iMac']")).click();

        waitForElementVisible(optionTitle);

        getSelect(colorDropdown).selectByVisibleText(color);
        clickElement(addToCartBtn);
    }

    @Step("Thêm MacBook với RAM {0} và Storage {1}")
    public void addMacbook(String ramSize, String storageSize) {
        searchPage.searchProduct("macbook");
        WebElement macBook = driver.findElement(By.xpath("(//a[normalize-space()='MacBook'])[1]"));
        scrollToElement(macBook);
        clickElement(macBook);
        waitForElementVisible(optionTitle);

        Select ram = getSelect(ramDropdown);
        for (WebElement option : ram.getOptions()){
            if(option.getText().contains(ramSize)){
                clickElement(option);
                break;
            }
        }

        Select storage = getSelect(storageDropdown);
        for (WebElement op : storage.getOptions()){
            if(op.getText().contains(storageSize)){
                clickElement(op);
                break;
            }
        }

        clickElement(addToCartBtn);
    }

    @Step("Thêm sản phẩm nhanh")
    public void addProductsForTest() {
        addIMac("Silver");
        isSuccessMessageDisplayed();

        addMacbook("8GB", "256GB");
        isSuccessMessageDisplayed();
    }

    //Cập nhật, xóa

    @Step("Cập nhật số lượng {1} cho sản phẩm {0}")
    public void updateQuantity(String product, String qty) {
        WebElement row = getRowByProduct(product);
        WebElement qtyInput = row.findElement(By.cssSelector("input[name*='quantity']"));
        enterText(qtyInput, qty);
        WebElement updateBtn = row.findElement(By.cssSelector("button[data-original-title='Update']"));
        clickElement(updateBtn);
    }

    @Step("Xóa sản phẩm {0}")
    public void removeProduct(String product) {
        WebElement row = getRowByProduct(product);
        WebElement removeBtn = row.findElement(By.cssSelector("button[data-original-title='Remove']"));
        clickElement(removeBtn);
        wait.until(ExpectedConditions.stalenessOf(row));
    }

  //Phần giỏ hàng

    @Step("Kiểm tra sản phẩm {0} có trong giỏ")
    public boolean isProductInCart(String product) {
        try {
            return driver.findElements(
                    By.xpath("//tbody/tr[.//a[normalize-space()='" + product + "']]")
            ).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Lấy số lượng của sản phẩm {0}")
    public int getProductQuantity(String product) {
        WebElement row = getRowByProduct(product);
        String value = row.findElement(By.cssSelector("input[name*='quantity']")).getAttribute("value");
        return Integer.parseInt(value);
    }

    @Step("Lấy giá từng sản phẩm {0}")
    public double getUnitPrice(String product) {
        WebElement row = getRowByProduct(product);
        return parsePrice(row.findElement(By.xpath("./td[5]")).getText());
    }

    @Step("Lấy tổng tiền của sản phẩm {0}")
    public double getTotalPrice(String product) {
        WebElement row = getRowByProduct(product);
        return parsePrice(row.findElement(By.xpath("./td[6]")).getText());
    }

    @Step("Lấy tổng tiền cuối cùng")
    public double getCartTotal() {
        WebElement el = driver.findElement(By.xpath(
                "//strong[text()='Total:']/parent::td/following-sibling::td"
        ));
        return parsePrice(el.getText());
    }



    public double getSubTotal() {
        WebElement el = driver.findElement(By.xpath(
                "//strong[text()='Sub-Total:']/parent::td/following-sibling::td"));
        scrollToElement(el);
        return parsePrice(el.getText());
    }


    public double getVAT() {
        WebElement el = driver.findElement(By.xpath(
                "//strong[normalize-space()='VAT (20%):']/parent::td/following-sibling::td"));
        return parsePrice(el.getText());
    }

    public double getEcoTax() {
        WebElement el = driver.findElement(By.xpath(
                "//strong[normalize-space()='Eco Tax (-2.00):']/parent::td/following-sibling::td"
        ));
        return parsePrice(el.getText());
    }


    @Step("Kiểm tra tổng tiền chính xác")
    public boolean isTotalCorrect() {
        waitForPageLoaded();
        double sub = getSubTotal();
        double vat = getVAT();
        double eco = getEcoTax();
        double total = getCartTotal();

        System.out.println("Sub: " + sub);
        System.out.println("VAT: " + vat);
        System.out.println("Eco: " + eco);
        System.out.println("Total: " + total);
        System.out.println("Calc: " + (sub + vat + eco));

        return Math.abs((sub + vat + eco) - total) < 0.01;
    }



}
