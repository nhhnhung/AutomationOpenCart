package com.opencart.pages;

import com.opencart.base.BasePage;
import com.opencart.utils.Log;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends BasePage {

    RegisterPage registerPage;
    public LoginPage(WebDriver driver) {
        super(driver);
        registerPage = new RegisterPage(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,5), this);
    }

    @FindBy(xpath = "//a[@title='My Account']")
    WebElement btnAcc;

    @FindBy(xpath = "//a[normalize-space()='Login']")
    WebElement btnNavLogin;

    @FindBy(id = "input-email")
    WebElement email;
    @FindBy(id = "input-password")
    WebElement pass;


    @FindBy(xpath = "//input[@value='Login']")
    WebElement btnLogin;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[normalize-space()='Logout']")
    WebElement logout;


    @FindBy(xpath = "//div[@class='alert alert-danger alert-dismissible']")
    WebElement alertDanger;

    @Step("Kiểm tra thông báo lỗi : {0}")
    public void verifyErrorMessage(String expectedMessage) {

        List<String> actualMessages = new ArrayList<>();
        // alert chung
        try {
            if (alertDanger.isDisplayed()) {
                actualMessages.add(alertDanger.getText().trim());
            }
        } catch (Exception ignored) {
        }

        Log.info("Thông báo lỗi: " + actualMessages);

        boolean isMatched = actualMessages.stream()
                .anyMatch(msg -> msg.contains(expectedMessage));

        Assert.assertTrue(
                isMatched,
                "Lỗi mong muốn không tìm thấy: " + expectedMessage
                        + "\nThông báo thực tế: " + actualMessages
        );
    }

    @Step("Mở menu My Account")
    public void openMyAccountMenu() {
        clickElement(this.btnAcc);
    }

    @Step("Click Login")
    public void clickLogin() {
        clickElement(this.btnNavLogin);
    }

    @Step("Nhập email: {0}")
    public void inputEmail(String email){
        enterText(this.email, email);
    }

    @Step("Nhập password: {0}")
    public void inputPass(String pass){
        enterText(this.pass, pass);
    }

    @Step("Click nút đăng nhập tài khoản")
    public void clickBTNLogin(){
        clickElement(this.btnLogin);
    }

    public void login(String email, String pass){
        inputEmail(email);
        inputPass(pass);
        clickBTNLogin();
    }

    public void clickLogout() {
        registerPage.clickLogout();
    }
}
