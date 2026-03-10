package com.opencart.tests;

import com.opencart.base.BaseTest;
import com.opencart.pages.CartPage;
import com.opencart.pages.CheckoutPage;
import com.opencart.pages.LoginPage;
import com.opencart.pages.SearchPage;
import io.qameta.allure.*;
import listeners.ReportListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ReportListener.class)
@Epic("Checkout Function")
@Feature("Order Placement")
public class CheckoutTest extends BaseTest {
    SearchPage searchPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    LoginPage loginPage;

    @BeforeClass
    public void init() {
        searchPage = new SearchPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        loginPage = new LoginPage(driver);
    }

    private void addProductToCart(){
        cartPage.addIMac("Silver");
        cartPage.openCart();
    }

    //Khách vảng lãi thanh toán thành công
    @Test(priority = 1)
    @Description("Kiểm tra khách vãng lai thanh toán thành công")
    @Severity(SeverityLevel.CRITICAL)
    public void guestCheckoutSuccess() {
        addProductToCart();
        checkoutPage.goToCheckout();
        checkoutPage.checkoutAsGuest(
                "Ham",
                "Le",
                "leham@gmail.com",
                "0785626535",
                "Hậu Giang",
                "Hậu Giang",
                "90000",
                "Viet Nam",
                "Hau Giang"
        );

        checkoutPage.assertTrue(checkoutPage.isTotalCorrect(),"Lỗi: Tính tổng thanh toán sai!");
        checkoutPage.confirmOrder();
        checkoutPage.waitForPageLoaded();
        checkoutPage.assertTrue(checkoutPage.isSuccess(),"Lỗi: Thanh toán thất bại!");
    }

    // Khách hàng có đăng ký tài khoản đăng ký thành công
    @Test(priority = 4)
    @Description("Kiểm tra thanh toán thành công với khách có tài khoản")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithAccountSuccess() {
        cartPage.openCart();
        checkoutPage.goToCheckout();
        loginPage.login("john068@mail.com", "123456");
        checkoutPage.continuePaymentAddrMethod();
        checkoutPage.continueShipAddrMethod();
        checkoutPage.continueShippingMethod();
        checkoutPage.agreeTerms();
        checkoutPage.continuePaymentMethod();
        checkoutPage.assertTrue(checkoutPage.isTotalCorrect(),"Lỗi: Tính tổng thanh toán sai!");
        checkoutPage.confirmOrder();
        checkoutPage.waitForPageLoaded();
        checkoutPage.assertTrue(checkoutPage.isSuccess(),"Lỗi: Thanh toán thất bại!");
    }

    // Negative

    @Test(priority = 2)
    @Description("Kiểm tra thất bại khi không đồng ý điều khoản")
    @Severity(SeverityLevel.NORMAL)
    public void checkoutFailWithoutAgree() {
        addProductToCart();
        checkoutPage.goToCheckout();
        // không tick agree
        checkoutPage.chooseGuestCheckout();
        checkoutPage.enterBillingDetails("Nguyen",
                "Van B",
                "fail" + System.currentTimeMillis() + "@mail.com",
                "0123456789",
                "123 Street",
                "HCM",
                "700000",
                "Viet Nam",
                "Ho Chi Minh City");
        checkoutPage.continueGuest();
        checkoutPage.waitForPageLoaded();
        checkoutPage.continueShippingMethod();
        checkoutPage.continuePaymentMethod();
        checkoutPage.assertTrue(checkoutPage.getErrorMessage().length() > 0,"Lỗi: Không có thông báo lỗi!");
    }

    // kiểm tra thất bại khi nhập email sai và số điện thoại là chữ cái
    @Test(priority = 3)
    @Description("Kiểm tra thanh toán thất bại khi email không hợp lệ và số điện thoại là chữ cái")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutFailInvailEmailAndPhone() {
        cartPage.openCart();
        checkoutPage.goToCheckout();
        checkoutPage.chooseGuestCheckout();
        checkoutPage.enterBillingDetails(
                "Ham",
                "Le",
                "ham@vdsjvdjd.com",
                "@$#dfyh",
                "Hậu Giang",
                "Hậu Giang",
                "90000",
                "Viet Nam",
                "Hau Giang"
        );

        checkoutPage.continueGuest();

        checkoutPage.assertTrue(checkoutPage.getErrorMessage().length() > 0,"Lỗi: Không hiện thông báo lỗi!");
    }
}
