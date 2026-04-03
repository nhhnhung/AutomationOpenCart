package com.opencart.tests;

import Data.DataProviderFactory;
import com.opencart.base.BaseTest;
import com.opencart.helpers.DatabaseHelper;
import com.opencart.pages.LoginPage;
import io.qameta.allure.*;
import listeners.ReportListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners(ReportListener.class)
@Epic("User Authentication")
@Feature("Login Account")
public class LoginTest extends BaseTest {
    LoginPage loginPage;
    WebDriver driver;

    @BeforeClass
    public void initPages() {
        driver = getDriver();
        loginPage = new LoginPage(driver);
    }

    //Happy test
    @Test(priority = 1, dataProvider = "loginData", dataProviderClass = DataProviderFactory.class)
    @Description("Đăng nhập tài khoản với dữ liệu hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginSuccess(
            String testType,
            String email,
            String password,
            String expectedMessage
    ) {
        loginPage.openMyAccountMenu();
        loginPage.clickLogin();
        loginPage.inputEmail(email);
        loginPage.inputPass(password);
        loginPage.clickBTNLogin();
        loginPage.assertTrue(driver.getTitle().contains("My Account"), "Đăng nhập tài khoản thất bại!");
        loginPage.clickLogout();
    }

    //Negative + Boundary test
    @Test(priority = 2, dataProvider = "loginData", dataProviderClass = DataProviderFactory.class)
    @Description("Đăng nhập với dữ liệu không hợp lệ")
    public void testLoginNegativeBoundary(
            String testType,
            String email,
            String password,
            String expectedMessage
    ) {
        if(driver.getTitle().contains("My Account") == true){
            loginPage.clickLogout();
        }
        loginPage.openMyAccountMenu();
        loginPage.clickLogin();
        loginPage.inputEmail(email);
        loginPage.inputPass(password);
        loginPage.clickBTNLogin();
        loginPage.verifyErrorMessage(expectedMessage);
    }

    //Test block login
    @Test(priority = 3)
    @Description("Nhấn nút login nhiều lần xem có khóa tạm thời không")
    @Severity(SeverityLevel.MINOR)
    public void testBlockLogin() {
        if(driver.getTitle().contains("My Account") == true){
            loginPage.clickLogout();
        }
        loginPage.openMyAccountMenu();
        loginPage.clickLogin();
        loginPage.waitForPageLoaded();
        for (int i = 0; i < 6; i++) {
            loginPage.clickBTNLogin();
        }
        loginPage.verifyErrorMessage("Your account has exceeded");
    }

    @Test(priority = 4)
    @Description("Đăng nhập với dữ liệu đúng khi bị khóa đăng nhập")
    public void testLoginWhenLoginBlock() {
        if(driver.getTitle().contains("My Account") == true){
            loginPage.clickLogout();
        }
        loginPage.openMyAccountMenu();
        loginPage.clickLogin();
        loginPage.inputEmail("john068@mail.com");
        loginPage.inputPass("123456");
        loginPage.clickBTNLogin();
        loginPage.waitForPageLoaded();
        if(driver.getTitle().contains("My Account") == true){
            loginPage.clickLogout();
            loginPage.assertTrue(false,"Lỗi: Login bị khóa vẫn đăng nhập được");
        }
        loginPage.verifyErrorMessage("Warning: Your account has exceeded allowed number of login attempts. Please try again in 1 hour.");
    }

    @Test(priority = 5, dataProvider = "loginData", dataProviderClass = DataProviderFactory.class)
    @Description("Đăng nhập với mật khẩu: {password}")
    public void testLoginWithPassSQLInjection(
            String testType,
            String email,
            String password,
            String expectedMessage
    ) {
        if(driver.getTitle().contains("My Account") == true){
            loginPage.clickLogout();
        }
        loginPage.openMyAccountMenu();
        loginPage.clickLogin();
        loginPage.inputEmail(email);
        loginPage.inputPass(password);
        loginPage.clickBTNLogin();
        loginPage.verifyErrorMessage(expectedMessage);
    }

}
