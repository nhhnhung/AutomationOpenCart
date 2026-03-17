package com.opencart.base;


import com.opencart.helpers.CaptureHelper;
import com.opencart.helpers.DatabaseHelper;
import com.opencart.helpers.PropertiesHelper;
import com.opencart.utils.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;


import java.time.Duration;


public class BaseTest {
    public WebDriver driver;
    public String URL;

    @BeforeClass
    public void createDriver() {
        DatabaseHelper.resetLoginAttempt();
        PropertiesHelper.loadAllFiles();
        String browser = PropertiesHelper.getValue("browser");
        URL = PropertiesHelper.getValue("url");
        Log.info("Khởi tạo trình duyệt: " + browser);
        //Khởi tạo driver
        switch (browser.toLowerCase()){
            case "chrome":
                //Tạo ChromeOption => tắt thông báo
                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--incognito");
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-infobars");
                options.addArguments("--password-store=basic");
                options.addArguments("--disable-features=PasswordLeakDetection");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Không hổ trợ browser: " + browser);
        }

        driver.get(URL);
        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
    }

    public WebDriver getDriver() {
        return driver;
    }

    // Đóng trình duyệt
    @AfterClass
    public void closeDriver() {
        driver.quit();
    }

}
