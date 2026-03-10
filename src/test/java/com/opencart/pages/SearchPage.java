package com.opencart.pages;

import com.opencart.base.BasePage;
import io.qameta.allure.Step;
import org.apache.poi.ss.formula.atp.Switch;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPage extends BasePage {
    public SearchPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,5), this);
    }

    @FindBy(xpath = "//input[@placeholder='Search']")
    WebElement search;

    @FindBy(xpath = "//button[@class='btn btn-default btn-lg']")
    WebElement btnSearch;

    @FindBy(xpath = "//p[contains(text(),'There is no product that matches the search criter')]")
    WebElement msgNoProduct;

    @FindBy(css = ".product-thumb h4 a")
    List<WebElement> productNames;

    @FindBy(css = ".product-thumb .price:not(.price-tax)")
    List<WebElement> productPrices;

    //Phần Lọc sản phẩm
    @FindBy(css = ".dropdown-toggle[href='http://localhost/opencart/index.php?route=product/category&path=20']")
    WebElement navDesktops;

    @FindBy(xpath = "//a[normalize-space()='Show All Desktops']")
    WebElement seeAllDesktop;

    @FindBy(id = "input-sort")
    WebElement selectSearch;

    //Phần lọc theo danh mục
    @FindBy(xpath = "//select[@name='category_id']")
    WebElement selectCategory;

    @FindBy(id = "button-search")
    WebElement btnCategorySearch;

    private Select getSortSelect() {
        waitForElementVisible(selectSearch);
        return new Select(selectSearch);
    }

    private Select getCategorySelect(){
        waitForElementVisible(selectCategory);
        return new Select(selectCategory);
    }


    @Step("Nhập từ khóa: {0}, và tìm kiếm")
    public void searchProduct(String keyword){
        enterText(this.search, keyword);
        clickElement(this.btnSearch);
    }

    @Step("Kiểm tra có hiện thông báo không có sản phẩm không")
    public boolean isDisplayMsg(){
        if(this.msgNoProduct.isDisplayed()){
            return true;
        }
        return false;
    }

    @Step("Kiểm tra tên các sản phẩm có hiện đúng theo từ khóa: {0}")
    public boolean isProductNameMatch(String keyword){
        for(WebElement name : this.productNames){
            if(!name.getText().toLowerCase().contains(keyword.toLowerCase()))
                return false;
        }
        return true;
    }

    public List<String> getTextProductNames() {
        return this.productNames.stream()
                .map(e -> e.getText().trim())
                .toList();
    }

    public List<Double> getPriceProductPrices(){
        return this.productPrices.stream()
                .map(WebElement::getText)
                .map(text -> {
                    String price = text.replaceAll("[^0-9.,]", " ").trim().split("\\s+")[0];
                    return price.replace(",", "");
                })
                .map(Double::parseDouble)
                .toList();
    }

    @Step("Kiểm tra tên có được sắp xếp theo A - Z không")
    public boolean isSortNameAZ(){
        List<String> names = getTextProductNames();
        List<String> sortList = new ArrayList<>(names);
        Collections.sort(sortList, String.CASE_INSENSITIVE_ORDER);
        return names.equals(sortList);
    }
    @Step("Kiểm tra tên có được sắp xếp theo Z - A không")
    public boolean isSortNamedZA(){
        List<String> names = getTextProductNames();
        List<String> sortList = new ArrayList<>(names);
        Collections.sort(sortList, String.CASE_INSENSITIVE_ORDER.reversed());
        return names.equals(sortList);
    }

    @Step("Kiểm tra giá có được sắp xếp theo cao tới thấp không")
    public boolean isPriceHL(){
        List<Double> prices = getPriceProductPrices();
        List<Double> sortList = new ArrayList<>(prices);
        Collections.sort(sortList, Collections.reverseOrder());
        return prices.equals(sortList);
    }

    @Step("Kiểm tra giá có được sắp xếp theo thấp đến cao không")
    public  boolean isPriceLH(){
        List<Double> prices = getPriceProductPrices();
        List<Double> sortList = new ArrayList<>(prices);
        Collections.sort(sortList);
        return prices.equals(sortList);
    }

    @Step("Lọc sản phẩm theo {0}")
    public boolean filter(String sortBy){
        getSortSelect().selectByVisibleText(sortBy);
        waitForPageLoaded();
        switch (sortBy){
            case "Name (A - Z)":
                return isSortNameAZ();
            case "Name (Z - A)":
                return isSortNamedZA();
            case "Price (Low > High)":
                return isPriceLH();
            case "Price (High > Low)":
                return isPriceHL();
            default:
                throw new RuntimeException("Không có lọc đúng theo tìm kiếm!");
        }
    }

    @Step("Click vào nút search")
    public void clickBtnSearch(){
        clickElement(this.btnCategorySearch);
    }

    @Step("Lọc các sản phẩm theo danh mục: Desktops")
    public boolean filterCategory(){
        getCategorySelect().selectByVisibleText("Desktops");
        clickBtnSearch();
        if(isDisplayMsg()){
            return false;
        }else{
            return true;
        }
    }

    public void navFilter(){
        moveToElement(this.navDesktops);
        clickElement(this.seeAllDesktop);
    }

    @Step("Di chuyển đến trang lọc theo danh mục")
    public void navCategoryFilter(){
        enterText(this.search, "");
        clickElement(this.btnSearch);
    }


}
