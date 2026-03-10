
import com.opencart.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import listeners.ReportListener;
import org.testng.*;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners(ReportListener.class)
@Epic("Test thử nghiệm")
@Feature("Test Screeshot")
public class TestScreenshot extends BaseTest {


    @Test
    @Step("Mở trang so sánh tên web đúng")
    public void testHomePage1(Method method) {
        driver.get("https://anhtester.com");
        Assert.assertEquals(driver.getTitle(), "Anh Tester Automation Testing");
    }

    @Test
    @Step("Mở trang so sánh tên web cố tình sai")
    public void testHomePage2(Method method) {
        driver.get("https://anhtester.com");
        Assert.assertEquals(driver.getTitle(), "Anh Tester Automation Test");
    }

}
