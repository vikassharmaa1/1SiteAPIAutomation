package wcs.ccp.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.page.objects.PM_HomePage;
import com.page.objects.PM_LoginPage;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

public class WCS_CCPToCOL_Tests {
	WebDriver driver = null;
	FunLibrary funLibrary;
	static Map<String, String> excelData = new HashMap<String, String>();
	static XLS_Reader datatable;

	@BeforeMethod()
	@Parameters({"sheetname"})
	public void setup(String sheetname, Method method) {
		int currentRow = 0;
		getProperties();
		XLS_Reader datatable = new XLS_Reader(System.getProperty("user.dir") + System.getProperty("DataFilePath") + System.getProperty("DataFile_WCS"));
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + System.getProperty("ChromeDriver"));
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.get(System.getProperty("PM_URL"));
		funLibrary = new FunLibrary(driver);

		for (int i = 2; i <= datatable.getRowCount(sheetname); i++) {
			if(datatable.getCellData(sheetname, "TestMethodName", i).equals(method.getName())) {
				currentRow = i;
				System.out.println("Test Started:"+method.getName());
				break;
			}
		}
		excelData = datatable.getTestData(sheetname, currentRow);
	}

	@AfterMethod()
	public void tearDown() {
		driver.close();
		driver.quit();
	}

	public void getProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/java/baseConfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PropertyConfigurator.configure(properties);
		for (String name : properties.stringPropertyNames()) {
			String value = properties.getProperty(name);
			System.setProperty(name, value);
		}
	}

	@Test(description = "validate that CCP profile is created successfully using PM UI Url and synced in WCS. validate the user in WCS database")
	public void validateCreateProfile() throws Exception {
		PM_LoginPage pmLognPage = new PM_LoginPage();
		String mailId = funLibrary.getUniqueEmaild();
		String firstName = excelData.get("FirstName");
		String lastName = excelData.get("LastName");
		String db_firstName = "", db_lastName = "", db_userId = "", db_email1;
		DatabaseUtilities dbUtil = new DatabaseUtilities();

		pmLognPage.registerCCPUser(mailId, firstName, lastName);
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + mailId + "'");
		db_firstName = dbUtil.getValues("ADDRESS", "FIRSTNAME", "MEMBER_ID='" + db_userId + "'");
		db_lastName = dbUtil.getValues("ADDRESS", "LASTNAME", "MEMBER_ID='" + db_userId + "'");
		db_email1 = dbUtil.getValues("ADDRESS", "EMAIL1", "MEMBER_ID='" + db_userId + "'");
		dbUtil.closeDBConnection();
		funLibrary.validate_Equals("FirstName", firstName, db_firstName);
		funLibrary.validate_Equals("LastName", lastName, db_lastName);
		funLibrary.validate_Equals("EmailID", mailId, db_email1);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate that CCP user is able to update profile")
	public void validateUpdateProfile() throws Exception {
		PM_LoginPage pmLognPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		Integer random = 10000000+new Random().nextInt(90000000);
		String mobileNo = "04" + random.toString();
		String firstName = "AutomationTest" + funLibrary.generateRandomString(5);
		String lastName = "User" + funLibrary.generateRandomString(5);
		String db_firstName = "", db_lastName = "", db_userId = "", db_phone1 = "", db_phone1Type = "";

		pmLognPage.loginPM(excelData.get("UserName"), excelData.get("Password"));
		pmHomePage.editFirstName().enterFirstName(firstName).saveFirstName()
		.editLastName().enterLastName(lastName).saveLastName().enterMobile(mobileNo);

		Thread.sleep(2000);
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		db_firstName = dbUtil.getValues("ADDRESS", "FIRSTNAME", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_lastName = dbUtil.getValues("ADDRESS", "LASTNAME", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		dbUtil.closeDBConnection();
		funLibrary.validate_Equals("FirstName", firstName, db_firstName);
		funLibrary.validate_Equals("LastName", lastName, db_lastName);
		funLibrary.validate_Equals("MobileNumber", mobileNo, db_phone1);
		funLibrary.validate_Equals("PhoneType", "MPN", db_phone1Type);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate that user is able to update Home phone number")
	public void validateUpdateContact() throws Exception {
		PM_LoginPage pmLognPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		String db_phone1 = "", db_phone1Type = "", db_userId = "";
		Integer random = 10000000+new Random().nextInt(90000000);
		String homeNo = "03" + random.toString();

		pmLognPage.loginPM(excelData.get("UserName"), excelData.get("Password"));
		pmHomePage.enterHomeNumber(homeNo);
		Thread.sleep(10000);
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "'and ADDRESSTYPE='B' and STATUS='P'");
		dbUtil.closeDBConnection();
		funLibrary.validate_Equals("HomeNumber", homeNo, db_phone1);
		funLibrary.validate_Equals("PhoneType", "HPN", db_phone1Type);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate that CCP profile is created successfully using PM UI Url and synced in WCS. validate the user in WCS database")
	public void validateCreateAndDeleteContact() throws Exception {
		PM_LoginPage pmLoginPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_phone1 = "", db_phone1Type = "",db_CCP_ContactType="";
		String db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		Integer random = 10000000+new Random().nextInt(90000000);
		String homeNo = "03" + random.toString();
		String mobileNo = "04" + random.toString();
		//Login
		pmLoginPage.loginPM(excelData.get("UserName"), excelData.get("Password"));
		//Add Mobile and Home Number
		pmHomePage.enterHomeNumber(homeNo);
		pmHomePage.enterMobile(mobileNo);
		//validate that when both numbers are updated, MPN will remain as primary
		Thread.sleep(5000);
		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_CCP_ContactType=dbUtil.getValues("WCSOWNER.XCCPCONTACT", "CCP_CONTACTTYPE", "MEMBER_ID='" + db_userId + "'");
		funLibrary.validate_Equals("MobileNumber", mobileNo, db_phone1);
		funLibrary.validate_Equals("PhoneType", "MPN", db_phone1Type);
		funLibrary.validate_Equals("HOME_PhoneType", "No Record Found", db_CCP_ContactType);
		//Deleting Mobile Number
		pmHomePage.deleteMobileNmber();
		Thread.sleep(5000);
		//Validating that after deleting mobile number, Home Number should be primary
		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_CCP_ContactType=dbUtil.getValues("WCSOWNER.XCCPCONTACT", "CCP_CONTACTTYPE", "MEMBER_ID='" + db_userId + "'");
		funLibrary.validate_Equals("HomeNumber", homeNo, db_phone1);
		funLibrary.validate_Equals("PhoneType", "HPN", db_phone1Type);
		funLibrary.validate_Equals("HOME_PhoneType", "phone", db_CCP_ContactType);
		//Deleting Home Number		
		pmHomePage.deleteHomeNmber();
		//Validation
		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
		db_CCP_ContactType=dbUtil.getValues("WCSOWNER.XCCPCONTACT", "CCP_CONTACTTYPE", "MEMBER_ID='" + db_userId + "'");
		dbUtil.closeDBConnection();
		funLibrary.checkNull("PhoneNumber", db_phone1Type);
		funLibrary.checkNull("PhoneType", db_phone1Type);
		funLibrary.validate_Equals("HOME_PhoneType", "No Record Found", db_CCP_ContactType);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate that user is able to change security reference")
	public void validateSecurityLevelPreference() throws Exception {
		PM_LoginPage pmLoginPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_securityPreference = "";
		String db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");

		pmLoginPage.loginPM(excelData.get("UserName"), excelData.get("Password"));
		pmHomePage.clickChangePrefernce().selectSecurityPreference("High").clickSavePreference();
		db_securityPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE ", "MEMBER_ID='" + db_userId + "'  AND MBRATTR_ID='840'");
		funLibrary.validate_Equals("SecurityPereference", "1", db_securityPreference);

		pmHomePage.clickChangePrefernce().selectSecurityPreference("Medium").clickSavePreference();
		db_securityPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE ", "MEMBER_ID='" + db_userId + "'  AND MBRATTR_ID='840'");
		funLibrary.validate_Equals("SecurityPereference", "0", db_securityPreference);
		
		pmHomePage.clickChangePrefernce().selectSecurityPreference("Low").clickSavePreference();
		db_securityPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE ", "MEMBER_ID='" + db_userId + "'  AND MBRATTR_ID='840'");
		funLibrary.validate_Equals("SecurityPereference", "-1", db_securityPreference);
	}

}
