package com.opencart.tests;

import com.opencart.base.BaseTest;
import com.opencart.pages.CartPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import listeners.ReportListener;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ReportListener.class)
@Epic("Cart Function")
@Feature("Shopping Cart")
public class CartTest extends BaseTest {
    CartPage cart;
    WebDriver driver;

    @BeforeClass
    public void initPages() {
        driver = getDriver();
        cart = new CartPage(driver);
        cart.addProductsForTest();
        cart.openCart();
    }


    @Test(priority = 1)
    @Description("Kiểm tra sản phẩm đã được thêm vào giỏ hàng thành công")
    public void verifyProductsAddedToCart() {
        cart.assertTrue(cart.isProductInCart("iMac"),"Lỗi: iMac chưa được thêm vào giỏ hàng");
        cart.assertTrue(cart.isProductInCart("MacBook"),"Lỗi: MacBook chưa được thêm vào giỏ hàng");
    }

   //Test kiểm tra cập nhật số lượng giá có đúng không
    @Test(priority = 2)
    @Description("Kiểm tra khi cập nhật số lượng thì tổng tiền sản phẩm thay đổi đúng")
    public void verifyUpdateQuantityUpdatesTotalPrice() {
        cart.updateQuantity("MacBook", "2");
        double unitPrice = cart.getUnitPrice("MacBook");
        double totalPrice = cart.getTotalPrice("MacBook");
        cart.assertEquals(totalPrice, unitPrice * 2, "Lỗi: Tổng tiền không đúng sau khi cập nhật số lượng");
    }

    //Test xóa sản phẩm
    @Test(priority = 3)
    @Description("Kiểm tra có thể xóa sản phẩm khỏi giỏ hàng")
    public void verifyRemoveProduct() {
        cart.removeProduct("iMac");
        Assert.assertFalse(cart.isProductInCart("iMac"),"Lỗi: Sản phẩm iMac chưa được xóa khỏi giỏ hàng");
    }

    //Test reload trang có giữ nguyên hiện trạng không
    @Test(priority = 4)
    @Description("Kiểm tra giỏ hàng vẫn giữ nguyên sau khi reload trang")
    public void verifyReloadKeepsCartState() {
        cart.updateQuantity("MacBook", "3");
        cart.reloadPage();
        cart.assertEquals(cart.getProductQuantity("MacBook"), 3,"Lỗi: Sai số lượng khi reload lại trang");
    }

    //Khi nhập số lượng quá mức vd: 900000000
    @Test(priority = 5)
    @Description("Kiểm tra khi nhập số lượng vượt quá tồn kho")
    public void verifyQuantityOverflow() {
        cart.updateQuantity("MacBook", "999999999999");
        cart.assertTrue(
                cart.getDangerMessage().contains("not available in the desired quantity"),
                "Lỗi: Không hiển thị cảnh báo hết hàng"
        );
    }

    //Khi nhập số lượng = 0
    @Test(priority = 6)
    @Description("Kiểm tra hệ thống xử lý khi nhập số lượng = 0")
    public void verifyQuantityZero() {
        if(!cart.isProductInCart("MacBook")){
            cart.addMacbook("8GB","256GB");
            cart.openCart();
        }
        cart.updateQuantity("MacBook", "0");
        cart.waitForPageLoaded();
        cart.assertTrue(
                !cart.isProductInCart("MacBook"),
                "Lỗi: Hệ thống không xử lý đúng khi nhập số lượng = 0"
        );
    }


    // Khi nhập số lượng = -1
    @Test(priority = 7)
    @Description("Kiểm tra hệ thống xử lý khi nhập số lượng âm")
    public void verifyNegativeQuantity() {
        if(!cart.isProductInCart("MacBook")){
            cart.addMacbook("8GB","256GB");
            cart.waitForPageLoaded();
            cart.openCart();
        }
        cart.updateQuantity("MacBook", "-1");
        cart.waitForPageLoaded();
        cart.assertTrue(
                !cart.isProductInCart("MacBook"),
                "Lỗi: Hệ thống không xử lý đúng khi nhập số lượng = -1"
        );
    }

    //Khi nhập số lượng là các kí tự
    @Test(priority = 8)
    @Description("Kiểm tra hệ thống xử lý khi nhập ký tự không hợp lệ")
    public void verifyInvalidQuantityText() {
        if(!cart.isProductInCart("MacBook")){
            cart.addMacbook("8GB","256GB");
            cart.openCart();
        }
        cart.updateQuantity("MacBook","@#$%gj");
        cart.waitForPageLoaded();
        cart.assertTrue(
                !cart.isProductInCart("MacBook"),
                "Lỗi: Hệ thống chấp nhận ký tự không hợp lệ");
    }



    //Tính tổng tiền
    @Test(priority = 9)
    @Description("Kiểm tra tổng tiền giỏ hàng được tính đúng")
    public void verifyCartTotalPrice() {
        if(!cart.isProductInCart("MacBook")){
            cart.addMacbook("8GB","256GB");
            cart.openCart();
        }
        cart.assertTrue(cart.isTotalCorrect(),"Lỗi: Tính tiền sai!");
    }
}


