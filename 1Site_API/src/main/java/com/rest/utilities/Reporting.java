package com.rest.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class Reporting implements ITestListener {

	static String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	private static ExtentReports extent = ExtentManager.getExtentInstance(timeStamp);
	private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	public ExtentTest child;

	@Override
	public synchronized void onFinish(ITestContext result) {
		extent.flush();
	}

	@Override
	public synchronized void onStart(ITestContext result) {
		ExtentTest parent = extent.createTest(result.getName());
		parentTest.set(parent);
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		try {
			test.set(child);
			test.get().fail(MarkupHelper.createLabel("FAILED: Please find the details below", ExtentColor.RED));
			test.get().fail("Reason of Failure:\n " + result.getThrowable());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {

	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		child = parentTest.get().createNode(result.getName());
		test.set(child);
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		test.get().log(Status.PASS, MarkupHelper.createLabel("Test Outcome : Pass", ExtentColor.GREEN));
	}

}
