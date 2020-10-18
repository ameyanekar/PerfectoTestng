package com.perfecto.testng;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;

public class Utils {

    /**
     * Creates reportium client
     * @param driver
     * @param reportiumClient
     * @return
     * @throws Exception
     */
    public static ReportiumClient setReportiumClient(RemoteWebDriver driver, ReportiumClient reportiumClient, String tag) throws Exception {
        PerfectoExecutionContext perfectoExecutionContext;
        // Reporting client. For more details, see https://developers.perfectomobile.com/display/PD/Java
        if(System.getProperty("reportium-job-name") != null) {
            perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                    .withProject(new Project("My Project", "1.0"))
                    .withJob(new Job(System.getProperty("reportium-job-name") , Integer.parseInt(System.getProperty("reportium-job-number"))))
                    .withContextTags(tag)
                    .withWebDriver(driver)
                    .build();
        } else {
            perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                    .withProject(new Project("My Project", "1.0"))
                    .withContextTags(tag)
                    .withWebDriver(driver)
                    .build();
        }
        reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
        if (reportiumClient == null) {
            throw new Exception("Reportium client not created!");
        }
        return reportiumClient;
    }


}