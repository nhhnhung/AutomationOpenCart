package listeners;

import com.opencart.base.BaseTest;
import com.opencart.helpers.CaptureHelper;
import com.opencart.helpers.PropertiesHelper;
import com.opencart.utils.Log;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import report.AllureManager;

import java.io.File;

import static com.opencart.helpers.LibraryHelper.sleep;


public class ReportListener implements ITestListener {

    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
    }

    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
    }

    @Override
    public void onStart(ITestContext result) {
        PropertiesHelper.loadAllFiles();
    }

    @Override
    public void onFinish(ITestContext result) {
        Log.info("End testing " + result.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        Log.info("Running test case " + result.getName());
        //Bắt đầu quay video
        CaptureHelper.startRecord(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Log.info("Test case " + result.getName() + " is passed.");
        //Dừng quay video
        sleep(1);
        File mp4File = CaptureHelper.stopRecord();
        AllureManager.attachVideo(mp4File);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();
        Log.error("Test case " + result.getName() + " is failed.");


        //Allure report
        AllureManager.saveTextLog(result.getName() + " is failed.");
        AllureManager.attachScreenshot(driver, result.getName());

        //Screenshot khi fail
        CaptureHelper.takeScreenshotWhenFail(driver,result.getName());
        Log.error(result.getThrowable().toString());
        //Dừng quay video
        sleep(1);
        File mp4File = CaptureHelper.stopRecord();
        AllureManager.attachVideo(mp4File);

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.error("Test case " + result.getName() + " is skipped.");
        Log.error(result.getThrowable().toString());
        //Dừng quay video
        sleep(1);
        File mp4File = CaptureHelper.stopRecord();
        AllureManager.attachVideo(mp4File);
    }
}
