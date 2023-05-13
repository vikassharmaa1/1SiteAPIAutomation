package com.rest.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.rest.utilities.XLS_Reader;

public class Base_Class_API {

	public FunLibrary funLibrary;
	public Logger testLog = Logger.getLogger("Log:");
	public ThreadLocal<String> testName = new ThreadLocal<>();
	public static ThreadLocal<String> method_name = new ThreadLocal<String>();
	public static ThreadLocal<String> sheet_name = new ThreadLocal<String>();
	public static ThreadLocal<Integer> currentRow = new ThreadLocal<Integer>();
	public static ThreadLocal<XLS_Reader> datatable = new ThreadLocal<XLS_Reader>();
	public static String current_Dir = System.getProperty("user.dir");
	public static String BaseURI = "";
	public static String WCS_Base_URL = "";
	public static String APIM_Base_URL = "";
	public static String PS_Base_URL_1 = "";
	public static String PS_Base_URL_2 = "";
	private static String data_file = "";
	public static String environment="";
	@BeforeClass(alwaysRun = true)
	@Parameters({"sheetname","env","layer"})
	public void beforeClass(String sheetname, String env, String layer) throws Exception {
		environment = env;
		getProperties();
		
		switch(env.toLowerCase()) {
		case "sit":
		case "b2b":
		case "bluedot":
			WCS_Base_URL = System.getProperty("SIT_WCS_Base_URL");
			APIM_Base_URL = System.getProperty("SIT_APIM_Base_URL");
			PS_Base_URL_1 =  System.getProperty("SIT_PS_Base_URL_1");
			PS_Base_URL_2 = System.getProperty("SIT_PS_Base_URL_2");
			break;
		case "preprod":
			WCS_Base_URL = System.getProperty("PreProd_WCS_Base_URL");
			APIM_Base_URL = System.getProperty("PreProd_APIM_Base_URL");
			PS_Base_URL_1 =  System.getProperty("PreProd_PS_Base_URL_1");
			PS_Base_URL_2 = System.getProperty("PreProd_PS_Base_URL_2");
			break;
		}
		
		switch (layer.toLowerCase()) {		
		case "ps":
			BaseURI = PS_Base_URL_1;
			data_file = getDataFileName("ps",env.toLowerCase());
			break;			
		case "ps_profile":
			BaseURI = PS_Base_URL_2;
			data_file = getDataFileName("ps",env.toLowerCase());
			break;
		case "apim":
			BaseURI = APIM_Base_URL;
			data_file = getDataFileName("apim",env.toLowerCase());
			break;
		case "wcs":
			BaseURI = WCS_Base_URL;
			data_file = getDataFileName("wcs",env.toLowerCase());
			break;
		case "ps_tests_e2e":
			BaseURI = PS_Base_URL_1;
			data_file = System.getProperty("DataFile_E2E");
			break;
		default:
			testLog.error("Please pass correct Base URL value from TestNG xml file");
			break;
		}
		
		try {
			if(BaseURI.equalsIgnoreCase("") || BaseURI == null) {
				testLog.error("Base URL cannot be empty");
				System.exit(1);
			}
		} catch (Exception e) {
			testLog.error("Exception while getting Base URL -" + e.toString());
			System.exit(1);
		}
		setDatatable();
		sheet_name.set(sheetname);
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, Object[] testData, ITestContext ctx) throws Exception {
		method_name.set(method.getName());
		if(testData.length > 0 && !(sheet_name.get().equals("TestData"))) {
			testName.set(method.getName() + "_" + testData[0]);
			ctx.setAttribute("testName", testName.get());
			method_name.set(testData[0].toString());
		}
	
		if(isTCIDFound()){	
			testLog.info("************************************************");
			testLog.info("TEST STARTED: " + method_name.get());
			testLog.info("************************************************");
		
			funLibrary = new FunLibrary();
		}else {
testLog.error("Test case - " + method_name.get() + " is not present in the sheet");
//			throw new SkipException("Test case - " + method_name.get() + " is not present in the sheet");
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		testName.set(null);
	}

	public boolean isTCIDFound() {
		String sheetname = sheet_name.get();
		for (int i = 2; i <= datatable.get().getRowCount(sheetname); i++) {
			if(datatable.get().getCellData(sheetname, "TestMethodName", i).equals(method_name.get())) {
						
				currentRow.set(i);
				return true;
			}
		}
		currentRow.set(-1);
		return false;
	}

	public void setDatatable() {
		datatable.set(new XLS_Reader(current_Dir + "/src/main/java/" + data_file));
	}

	/* Set url, datafile, path etc from baseConfig file */
	private void getProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(current_Dir + "/src/main/java/baseConfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PropertyConfigurator.configure(properties);
		for (String name : properties.stringPropertyNames()) {
			String value = properties.getProperty(name);
			System.setProperty(name, value);
		}

		try {
			properties.load(new FileInputStream(current_Dir + "/src/main/java/conflib/log4j_Rest.properties"));
			PropertyConfigurator.configure(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		System.setProperty("logfilename", "API_Log" + timeStamp);

	}
	
	public String getDataFileName(String layer, String env) {
		switch(layer) {
		case "ps":			
			switch(env) {
			case "sit":
				data_file = System.getProperty("DataFile_PS_SIT");
				break;
			case "b2b":
				data_file = System.getProperty("DataFile_PS_B2B");
				break;
			case "bluedot":
				data_file = System.getProperty("Bluedot_DataFile_PS");
				break;
			case "preprod":
				data_file = System.getProperty("DataFile_PS_PreProd");
				break;
			}
			break;
		case "wcs":
			switch(env) {
			case "sit":
				data_file = System.getProperty("DataFile_WCS_SIT");
				break;
			case "b2b":
				data_file = "";
			case "bluedot":
				data_file = System.getProperty("Bluedot_DataFile_WCS");
				break;
			}
			break;
		case "apim":
			switch(env) {
			case "sit":
				data_file = System.getProperty("DataFile_APIM_SIT");
				break;
			case "b2b":
				data_file = "";
			case "bluedot":
				data_file = System.getProperty("Bluedot_DataFile_APIM");
				break;
			}
			break;
		}
		
		return data_file;
	}
	
	public void setDB(String env) {
		
		switch(env) {
		case "sit":
			
			break;
		case "preprod":
			break;
		}
	}

}
