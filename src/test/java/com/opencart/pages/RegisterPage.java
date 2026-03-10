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


public class RegisterPage extends BasePage {

    @FindBy(xpath = "//a[@title='My Account']")
    WebElement btnAcc;
    @FindBy(xpath = "//a[normalize-space()='Register']")
    WebElement btnNavRes;

    @FindBy(xpath = "//a[@class='list-group-item'][normalize-space()='Logout']")
    WebElement logout;

    @FindBy(xpath = "//legend[normalize-space()='Your Personal Details']")
    WebElement fieldsetAcc;
    @FindBy(xpath = "//legend[normalize-space()='Your Password']")
    WebElement fieldsetPass;

    //Phần Thông tin tài khoản
    @FindBy(id = "input-firstname")
    WebElement firstName;
    @FindBy(id = "input-lastname")
    WebElement lastName;
    @FindBy(id = "input-email")
    WebElement email;
    @FindBy(id = "input-telephone")
    WebElement telephone;

    //Phần mật khẩu
    @FindBy(id = "input-password")
    WebElement pass;
    @FindBy(id = "input-confirm")
    WebElement comfirmPass;

    //Phần đồng ý chính sách
    @FindBy(xpath = "//input[@name='agree']")
    WebElement agreeCheck;

    @FindBy(xpath = "//input[@value='Continue']")
    WebElement btnRes;

    //Phần cảnh báo
    @FindBy(xpath = "//div[@class='alert alert-danger alert-dismissible']")
    WebElement alertDanger;
    @FindBy(className = "text-danger")
    List<WebElement> fieldErrors;

    @FindBy(xpath = "//h1[normalize-space()='Your Account Has Been Created!']")
    WebElement successTitle;

    public boolean isSuccess(){
        waitForPageLoaded();
        System.out.println("TITLE = [" + successTitle.getText() + "]");
        return successTitle.getText().contains("Your Account Has Been Created!");
    }

    public RegisterPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 5), this); // Khởi tạo cho @FindBy
    }

    public void clickLogout() {
        scrollToElement(this.logout);
        clickElement(this.logout);
    }

    @Step("Di chuyển đến khung nhập thông tin người dùng")
    public void ScrollToInfoAcc(){
        scrollToElement(fieldsetAcc);
    }
    @Step("Di chuyển đến khung nhập password")
    public void ScrollToPass(){
        scrollToElement(fieldsetPass);
    }


    @Step("Nhập first name: {0}")
    public void inputFirstName(String firstName){
        enterText(this.firstName, firstName);
    }
    @Step("Nhập last name: {0}")
    public void inputLastName(String lastName){
        enterText(this.lastName, lastName);
    }
    @Step("Nhập email: {0}")
    public void inputEmail(String email){
        enterText(this.email, email);
    }
    @Step("Nhập số điện thoai: {0}")
    public void inputTelephone(String tel){
        enterText(this.telephone, tel);
    }
    @Step("Nhập password: {0}")
    public void inputPass(String pass){
        enterText(this.pass, pass);
    }
    @Step("Nhập comfirm password: {0}")
    public void inputComfirm(String comfirm){
        enterText(this.comfirmPass, comfirm);
    }

    @Step("check vào đồng ý chính sách")
    public void checkAgreePolycy(){
        if(!this.agreeCheck.isSelected())
            clickElement(this.agreeCheck);
    }

    @Step("Click nút đăng ký tài khoản")
    public void clickBTNRegis(){
        clickElement(btnRes);
    }

    @Step("Mở menu My Account")
    public void openMyAccountMenu() {
        clickElement(this.btnAcc);
    }

    @Step("Click Register")
    public void clickRegister() {
        clickElement(this.btnNavRes);
    }

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

        // error theo field
        for (WebElement error : fieldErrors) {
            try {
                if (error.isDisplayed()) {
                    actualMessages.add(error.getText().trim());
                }
            } catch (Exception ignored) {
            }
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
}
