package com.opencart.pages;

import com.opencart.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {
    public CheckoutPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 5), this);
    }

    @FindBy(xpath = "//a[@class='btn btn-primary']")
    WebElement btnNavCheckout;

    //Trang checkout

    @FindBy(xpath = "//input[@value='guest']")
    WebElement radioGuest;

    @FindBy(xpath = "//input[@value='register']")
    WebElement radioRegister;

    @FindBy(id = "button-account")
    WebElement btnCtnAC;

    //Phần điền thông tin giao hàng
    //TT cá nhân
    @FindBy(id = "input-payment-firstname")
    WebElement firstname;

    @FindBy(id = "input-payment-lastname")
    WebElement lastname;

    @FindBy(id = "input-payment-email")
    WebElement email;

    @FindBy(id = "input-payment-telephone")
    WebElement phone;


    //TT địa chỉ
    @FindBy(id = "input-payment-address-1")
    WebElement address;

    @FindBy(id = "input-payment-city")
    WebElement city;

    @FindBy(id = "input-payment-postcode")
    WebElement postcode;

    @FindBy(id = "input-payment-country")
    WebElement selectCountry;

    @FindBy(id = "input-payment-zone")
    WebElement selectZone;

    @FindBy(id = "button-guest")
    WebElement btnCtnGuest;

    @FindBy(id = "button-shipping-method")
    WebElement btnShip;

    //Phần payment
    @FindBy(xpath = "//input[@name='agree']")
    WebElement checkAgree;

    @FindBy(xpath = "//div[@class='alert alert-danger alert-dismissible']")
    WebElement msgErorr;


    @FindBy(id = "button-payment-method")
    WebElement btnPayment;

    //Phần của khách có tài khoản
    @FindBy(id = "button-payment-address")
    WebElement btnPaymentAddr;

    @FindBy(id = "button-shipping-address")
    WebElement btnShipAddr;

    //Phần bảng tính tổng tiền
    //Các hàng của bảng
    @FindBy(xpath = "//table//tbody/tr")
    private WebElement productRow;

    @FindBy(xpath = "//table//tbody/tr/td[1]")
    private WebElement productName;

    @FindBy(xpath = "//table//tbody/tr/td[2]")
    private WebElement model;

    @FindBy(xpath = "//table//tbody/tr/td[3]")
    private WebElement quantity;

    @FindBy(xpath = "//table//tbody/tr/td[4]")
    private WebElement unitPrice;

    @FindBy(xpath = "//table//tbody/tr/td[5]")
    private WebElement productTotal;


    @FindBy(xpath = "//strong[text()='Sub-Total:']/parent::td/following-sibling::td")
    private WebElement subTotal;

    @FindBy(xpath = "//strong[contains(text(),'Shipping')]/parent::td/following-sibling::td")
    private WebElement shipping;

    @FindBy(xpath = "//strong[text()='Total:']/parent::td/following-sibling::td")
    private WebElement total;


    @FindBy(id = "button-confirm")
    private WebElement btnConfirmOrder;

    @FindBy(xpath = "//h1[contains(text(),'Your order has been placed')]")
    WebElement successTitle;

    public boolean isSuccess(){
        waitForPageLoaded();
        System.out.println("TITLE = [" + successTitle.getText() + "]");
        return successTitle.getText().contains("Your order has been placed");
    }

    public String getProductName() { return productName.getText(); }

    public int getQuantity() { return Integer.parseInt(quantity.getText()); }

    public double getUnitPrice() { return parsePrice(unitPrice.getText()); }

    public double getSubTotal() {
        waitForElementVisible(subTotal);
        return parsePrice(subTotal.getText());
    }

    public double getShipping() {
        waitForElementVisible(shipping);
        return parsePrice(shipping.getText());
    }

    public double getTotal() {
        waitForElementVisible(total);
        return parsePrice(total.getText());
    }

    @Step("Xác nhận đặt hàng")
    public void confirmOrder() { btnConfirmOrder.click(); }

    private double parsePrice(String price) {
        if(price == null || price.trim().isEmpty()) return 0;
        return Double.parseDouble(price.replace("$","").replace(",", "").trim());
    }
    @Step("Kiểm tra tổng tiền có chính xác")
    public boolean isTotalCorrect() {
        System.out.println(getSubTotal()+" ,"+getShipping()+", "+getTotal());
        return Math.abs((getSubTotal() + getShipping()) - getTotal()) < 0.01;
    }
    @Step("Kiểm tra tên sản phẩm có đúng: {0}")
    public boolean isCorrectProduct(String expectedName) {
        return getProductName().equalsIgnoreCase(expectedName);
    }

    @Step("Đi tới trang Checkout")
    public void goToCheckout() {
        clickElement(btnNavCheckout);
    }




    @Step("Chọn thanh toán với tư cách khách (Guest)")
    public void chooseGuestCheckout() {
        clickElement(radioGuest);
        clickElement(btnCtnAC);
    }

    @Step("Chọn đăng ký tài khoản mới")
    public void chooseRegisterCheckout() {
        clickElement(radioRegister);
        clickElement(btnCtnAC);
    }

    public void selectOption(WebElement select, String option){
        waitForElementVisible(select);
        new Select(select).selectByVisibleText(option);
    }

    @Step("Nhập thông tin khách hàng: {0} {1}, địa chỉ {4}, {5}")
    public void enterBillingDetails(String first, String last, String mail, String tel,
                                    String addr, String cityName, String post,
                                    String country, String zone) {
        scrollToElement(firstname);

        enterText(firstname, first);
        enterText(lastname, last);
        enterText(email, mail);
        enterText(phone, tel);
        enterText(address, addr);
        enterText(city, cityName);
        enterText(postcode, post);

        selectOption(selectCountry, country);
        selectOption(selectZone, zone);

    }

    @Step("Tiếp tục sau khi nhập thông tin")
    public void continueGuest() {
        clickElement(btnCtnGuest);
    }

    @Step("Tiếp tục phương thức giao hàng")
    public void continueShippingMethod() {
        clickElement(btnShip);
    }

    @Step("Đồng ý điều khoản thanh toán")
    public void agreeTerms() {
        if (!checkAgree.isSelected()) {
            clickElement(checkAgree);
        }
    }

    @Step("Tiếp tục phương thức thanh toán")
    public void continuePaymentMethod() {
        clickElement(btnPayment);
    }
    @Step("Tiếp tục phương thức thanh toán")
    public void continuePaymentAddrMethod() {
        clickElement(btnPaymentAddr);
    }

    @Step("Tiếp tục phương thức giao hàng")
    public void continueShipAddrMethod() {
        clickElement(btnShipAddr);
    }

    @Step("Lấy thông báo lỗi")
    public String getErrorMessage() {
        return msgErorr.getText();
    }

    @Step("Hoàn tất quy trình checkout với Guest")
    public void checkoutAsGuest(String first, String last, String mail, String tel,
                                String addr, String cityName, String post,
                                String country, String zone) {

        chooseGuestCheckout();
        enterBillingDetails(first, last, mail, tel, addr, cityName, post, country, zone);
        continueGuest();
        waitForPageLoaded();
        continueShippingMethod();
        agreeTerms();
        continuePaymentMethod();
    }

}
