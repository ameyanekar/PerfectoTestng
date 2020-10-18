package com.perfecto.testng;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;
import org.openqa.selenium.Platform;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.URL;

public class DesktopWinChrome {
    RemoteWebDriver driver;
    ReportiumClient reportiumClient;

    @Test
    public void seleniumTest() throws Exception {

        String cloudName = "testingcloud";
        //Replace <<security token>> with your perfecto security token or pass it as maven properties: -DsecurityToken=<<SECURITY TOKEN>>  More info: https://developers.perfectomobile.com/display/PD/Generate+security+tokens
        String securityToken = "<token>";

        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "86");
        capabilities.setCapability("location", "US East");
        capabilities.setCapability("resolution", "1024x768");
        capabilities.setCapability("securityToken", securityToken);

        try{
            driver = new RemoteWebDriver(new URL("https://" + cloudName + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);

        } catch(SessionNotCreatedException e) {

            throw new RuntimeException("Driver not created with capabilities: " + capabilities.toString());
        }

        try {
            reportiumClient = Utils.setReportiumClient(driver, reportiumClient, "DesktopWinChrome"); //Creates reportiumClient
            reportiumClient.testStart("Perfecto Desktop Win Chrome", new TestContext("Win", "Chrome")); //Starts the reportium test
            reportiumClient.stepStart("Browser navigate to Google"); //Starts a reportium step
            driver.get("https://google.com/");
            reportiumClient.stepEnd();

            reportiumClient.stepStart("Verify title");
            String aTitle = driver.getTitle();

            if (aTitle.equals("Google")) {
                reportiumClient.reportiumAssert("Title matches. Test successful", true);
            } else {
                reportiumClient.reportiumAssert("Title mismatch. Test failed. Title: " + aTitle, false);
                assert aTitle.equals("Google") : "Title mismatch. Test failed. Title: " + aTitle;
            }

            reportiumClient.stepEnd();

        } catch(Exception e) {
            driver.close();
            driver.quit();
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        //STOP TEST
        TestResult testResult = null;

        if(result.getStatus() == result.SUCCESS) {
            testResult = TestResultFactory.createSuccess();
        }
        else if (result.getStatus() == result.FAILURE) {
            testResult = TestResultFactory.createFailure(result.getThrowable());
        }
        reportiumClient.testStop(testResult);

        driver.close();
        driver.quit();

    }
}

