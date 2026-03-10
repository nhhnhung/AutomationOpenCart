package com.opencart.helpers;

import com.opencart.utils.VideoUtil;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.VideoFormatKeys.*;
import static org.monte.media.VideoFormatKeys.QualityKey;

public class CaptureHelper extends ScreenRecorder{

    //Chụp toàn màn hình
    public static String captureScreenshot(WebDriver driver, String screenshotName){
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            File theDir = new File("./export/screenshots/");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            String path = System.getProperty("user.dir") + "/export"
                    + "/screenshots/" + screenshotName + "_" + getCurrentTime() + ".png";

            FileUtils.copyFile(source, new File(path));
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String captureElement(WebElement element, String name) {
        try {
            File src = element.getScreenshotAs(OutputType.FILE);
            String path = System.getProperty("user.dir")
                    + "/screenshots/" + name + "_" + getCurrentTime() + ".png";

            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    public static void takeScreenshotWhenFail(WebDriver driver, String testName) {
        captureScreenshot(driver, testName + "_FAIL");
        System.out.println("Đã chụp hình: " + testName + "");
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
    }


    //Phần record
    private static ScreenRecorder screenRecorder;
    private static String name;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    private static File videoDir;

    //Hàm xây dựng
    public CaptureHelper(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    //Hàm này bắt buộc để ghi đè custom lại hàm trong thư viên viết sẵn
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {

        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException(movieFolder + " is not a directory.");
        }
        return new File(movieFolder, name + "_" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
    }


    // Hàm Start record video AVI
    public static void startRecord(String methodName) {
        //Tạo thư mục để lưu file video vào
        videoDir = new File("export/" + "videos" + "/" + methodName + "/");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        System.out.println("width" + width);
        System.out.println("height" + height);
        System.out.println("Đã ghi hình: " + videoDir.getName());

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                        .getDefaultScreenDevice()
                                                        .getDefaultConfiguration();
        try {
            screenRecorder = new CaptureHelper(
                    gc, null,
                    new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                            Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null, videoDir, methodName);

            screenRecorder.start();
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }



    // Stop record video
    public static File stopRecord() {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();
                File video = VideoUtil.convertAviToMp4(videoDir);
                return video;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
