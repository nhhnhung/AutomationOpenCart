package com.opencart.tests;

import Data.DataProviderFactory;
import com.opencart.base.BaseTest;
import com.opencart.pages.SearchPage;
import io.qameta.allure.*;
import listeners.ReportListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ReportListener.class)
@Epic("Search Function")
@Feature("Search Product")
public class SearchTest extends BaseTest {
    SearchPage searchPage;
    WebDriver driver;

    @BeforeClass
    public void initPages() {
        driver = getDriver();
        searchPage = new SearchPage(driver);
    }

    @Test(priority = 1, dataProvider = "searchData", dataProviderClass = DataProviderFactory.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Search với keyword hợp lệ và hiển thị danh sách sản phẩm")
    public void searchValidKeyword(
            String typeTest, String keyword
    ) {
        searchPage.searchProduct(keyword);
        searchPage.assertTrue(searchPage.isProductNameMatch(keyword),
                "Lỗi: Tên sản phẩm tìm thấy không đúng với từ khóa");
    }

    @Test(priority = 2, dataProvider = "searchData", dataProviderClass = DataProviderFactory.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Search với keyword không hợp lệ")
    public void searchInvalidKeyword(
            String typeTest, String keyword
    ){
        searchPage.searchProduct(keyword);
        searchPage.assertTrue(searchPage.isDisplayMsg(),
                "Lỗi: Từ khóa không hợp lệ vẫn tìm được sản phẩm");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Lọc sản phẩm")
    public void filterProduct(){
        searchPage.navFilter();
        searchPage.assertTrue(searchPage.filter("Name (A - Z)"), "Lỗi: Lọc theo Name (A - Z)");
        searchPage.assertTrue(searchPage.filter("Name (Z - A)"), "Lỗi: Lọc theo Name (Z - A)");
        searchPage.assertTrue(searchPage.filter("Price (Low > High)"), "Lỗi: Lọc theo Price (Low > High)");
        searchPage.assertTrue(searchPage.filter("Price (High > Low)"), "Lỗi: Lọc theo Price (High > Low)");

    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Lọc sản phẩm theo danh mục")
    public void filterFromCategory(){
        searchPage.navCategoryFilter();
        searchPage.assertTrue(searchPage.filterCategory(), "Lỗi: Lọc theo danh mục không hiện sản phẩm");
    }
}
