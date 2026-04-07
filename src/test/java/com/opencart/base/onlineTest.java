package com.opencart.base;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class onlineTest {
    WebDriver driver;

    @BeforeMethod
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void LoginStudentSuccess(){
        driver.get("https://htql.ctu.edu.vn/");

        driver.findElement(By.id("usernameUserInput")).sendKeys("B2205950");
        driver.findElement(By.id("password")).sendKeys("C$@5UxSs");
        driver.findElement(By.id("sign-in-button")).click();
        String username = driver.findElement(By.id("user-login")).getText();
        Assert.assertTrue(username.contains("Nguyễn Hoàng Hồng Nhung (B2205950)"),"login failue");
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
