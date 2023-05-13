package com.rest.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;

	public static ExtentReports getExtentInstance(String timeStamp) {
		if (extent == null) {
			createInstance(timeStamp);
		}
		return extent;
	}

	private static ExtentReports createInstance(String timeStamp) {

//		File file = new File(System.getProperty("user.dir") + "/ExtentReports/" + timeStamp);
//		file.mkdir();

		String rprtName = System.getProperty("user.dir") + "/ExtentReports/" + "/Test-Report"+timeStamp+".html";

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(rprtName);

		htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("Commerce Services - API Automation Project");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Commerce Services - API Automation Report");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Execution Platform", "Remote Desktop");
		extent.setSystemInfo("Environemnt", "SIT");
		extent.setSystemInfo("User", "Commerce Services - Test Automation Team");
		return extent;
	}
}
