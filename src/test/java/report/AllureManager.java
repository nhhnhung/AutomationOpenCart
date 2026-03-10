package report;

import com.google.common.io.Files;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;


public class AllureManager {
    //Thêm log vào báo cáo
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    // Gắn hình chụp vào báo các
    public static void attachScreenshot(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gắn video vào báo cáo allure
    public static void attachVideo(File videoFile) {
        try {
            if (videoFile == null || !videoFile.exists()) return;

            Allure.addAttachment(
                    "Execution Video",
                    "video/mp4",
                    Files.asByteSource(videoFile).openStream(),
                    "mp4"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
