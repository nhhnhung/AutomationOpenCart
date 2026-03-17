package com.opencart.tests;

import Data.DataProviderFactory;
import com.opencart.base.BaseTest;
import com.opencart.pages.RegisterPage;
import io.qameta.allure.*;
import listeners.ReportListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ReportListener.class)
@Epic("User Authentication") //Thuộc nhóm chức năng gì
@Feature("Register Account")
public class RegisterTest extends BaseTest {
    RegisterPage registerPage;
    WebDriver driver;

    @BeforeClass
    public void initPages() {
        driver = getDriver();
        registerPage = new RegisterPage(driver);

    }

    //Positive testcase
    @Test(priority = 1, dataProvider = "registerData", dataProviderClass = DataProviderFactory.class, description = "Đăng ký với dữ liệu hợp lệ")
    @Description("Đăng ký thành công với dữ liệu hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    public void registerSuccess(
            String testType,
            String firstName,
            String lastName,
            String email,
            String telephone,
            String password,
            String confirm,
            String expected
    ) {
        if(driver.getCurrentUrl().contains("account/success")){
            registerPage.clickLogout();
        }
        registerPage.openMyAccountMenu();
        registerPage.clickRegister();

        registerPage.assertTrue(driver.getCurrentUrl().contains("/register"), "Trang này không phải trang đăng ký!");
        registerPage.ScrollToInfoAcc();
        registerPage.inputFirstName(firstName);
        registerPage.inputLastName(lastName);
        registerPage.inputEmail(email);
        registerPage.inputTelephone(telephone);
        registerPage.ScrollToPass();
        registerPage.inputPass(password);
        registerPage.inputComfirm(confirm);
        registerPage.checkAgreePolycy();
        registerPage.clickBTNRegis();
        registerPage.assertTrue(registerPage.isSuccess(), "Đăng ký tài khoản thất bại!");
        registerPage.clickLogout();
    }

    //Negative + Boundary testcase
    @Test(priority = 2, dataProvider = "registerData", dataProviderClass = DataProviderFactory.class)
    @Description("Đăng ký thất bại với dữ liệu không hợp lệ")
    public void registerNegativeBoundary(
            String testType,
            String firstName,
            String lastName,
            String email,
            String telephone,
            String password,
            String confirm,
            String expectedMessage
    ) {
        if(driver.getCurrentUrl().contains("account/success")){
            registerPage.clickLogout();
        }
        registerPage.openMyAccountMenu();
        registerPage.clickRegister();
        registerPage.ScrollToInfoAcc();
        registerPage.inputFirstName(firstName);
        registerPage.inputLastName(lastName);
        registerPage.inputEmail(email);
        registerPage.inputTelephone(telephone);
        registerPage.ScrollToPass();
        registerPage.inputPass(password);
        registerPage.inputComfirm(confirm);
        registerPage.checkAgreePolycy();
        registerPage.clickBTNRegis();
        if (driver.getCurrentUrl().contains("account/success")) {
            registerPage.waitForPageLoaded();
            registerPage.clickLogout();
        }
        registerPage.verifyErrorMessage(expectedMessage);
    }

}
