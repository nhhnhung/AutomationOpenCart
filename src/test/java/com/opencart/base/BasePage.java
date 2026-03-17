package com.opencart.base;

import com.opencart.utils.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.opencart.helpers.LibraryHelper.sleep;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }


    //Chờ đợi trang load xong mới thao tác
    public void waitForPageLoaded() {
        //Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return js.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        //Check JS is Ready
        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            //System.out.println("Javascript is NOT Ready.");
            //Wait for Javascript to load
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                error.printStackTrace();
                throw new RuntimeException("FAILED. Timeout waiting for page load.");
            }
        }
    }

    public void waitForElementVisible(WebElement element) {
        try {
            this.wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Throwable error) {
            Log.error("Hết thời gian chờ phần tử hiển thị: " + element.toString());
            throw new RuntimeException("Không thể tìm thấy phần tử hiển thị: " + element.toString());
        }
    }

    public void waitForElementClickable(WebElement element) {
        try {
            this.wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Throwable error) {
            Log.error("Hết thời gian chờ phần tử có thể click: " + (element != null ? element.toString() : "element is null"));
            throw new RuntimeException("Không thể tìm thấy phần tử có thể click: " + (element != null ? element.toString() : "element is null"));
        }
    }

    public void moveToElement(WebElement element){
        actions.moveToElement(element).click().perform();
    }

    public void clickElement(WebElement element) {
        waitForElementClickable(element);
        element.click();
    }

    public void enterText(WebElement element, String text) {
        waitForElementClickable(element);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element) {
        return element.getText();
    }

    //Hàm xử lý select text cho phần tử element
    public void selectOptionByText(WebElement element, String text) {
        Select sl = new Select(element);
        sl.selectByVisibleText(text);
    }

    //Hàm xử lý select value cho phần tử element
    public void selectOptionByValue(WebElement element, String value) {
        Select sl = new Select(element);
        sl.selectByValue(value);
    }

    //Cuộn tới phần tử
    public void scrollToElement(WebElement element) {
        if (element == null) {
            throw new RuntimeException("Scroll failed: element is NULL");
        }
        Log.info("Cuộn tới phần tử: " + element.toString());
        waitForElementVisible(element);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        sleep(0.3);
    }

    public boolean verifyEquals(Object actual, Object expected) {
        waitForPageLoaded();
        System.out.println("So sánh giá trị:\n- Thực tế: " + actual + "\n - Kỳ vọng: " + expected);
        return actual.equals(expected);
    }

    public void assertEquals(Object actual, Object expected, String message) {
        waitForPageLoaded();
        Log.info("Đang xác thực giá trị bằng nhau...\n - Thực tế: " + actual + "\n - Kỳ vọng: " + expected);
        if (!verifyEquals(actual, expected)) {
            throw new RuntimeException("Kiểm tra thất bại: " + message +
                    "\nKỳ vọng: " + expected +
                    "\nThực tế: " + actual);
        }
    }
    public void assertTrue(Boolean obj, String message) {
        waitForPageLoaded();
        Log.info("Đang xác thực giá trị đúng...\n");
        if (obj == false) {
            throw new RuntimeException("Kiểm tra thất bại: " + message);
        }else{
            Log.info("Giá trị đúng");
        }
    }

    public boolean verifyContains(String actual, String expected) {
        waitForPageLoaded();
        Log.info("Kiểm tra chuỗi chứa: [" + actual + "] có chứa chuỗi [" + expected + "]");
        return actual.contains(expected);
    }

    public void assertContains(String actual, String expected, String message) {
        waitForPageLoaded();
        Log.info("Đang xác thực: [" + actual + "] có chứa chuỗi [" + expected + "]");
        if (!verifyContains(actual, expected))
            throw new RuntimeException("Kiểm tra thất bại: " + message);
    }

    public void Navigation(String url){
        driver.get(url);
    }

}
