package wcs.ccp.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.page.objects.PM_HomePage;
import com.page.objects.PM_LoginPage;
import com.page.objects.WCS_HomePage;
import com.page.objects.WCS_LoginPage;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

public class WCS_COLToCCP_Tests {
	WebDriver driver = null;
	FunLibrary funLibrary;
	
	Logger testLog = Logger.getLogger("Log:");
	static Map<String, String> excelData = new HashMap<String, String>();
	static XLS_Reader datatable; 
	
	@BeforeMethod()
	@Parameters({"sheetname"})
	public void setup(String sheetname, Method method) {
		int currentRow=0;
		getProperties();
		XLS_Reader datatable = new XLS_Reader(System.getProperty("user.dir") + System.getProperty("DataFilePath") + System.getProperty("DataFile_WCS"));
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + System.getProperty("ChromeDriver"));
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
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
// Out of scope
//	@Test(description = "validate that Profile Sync from COL to CCP")
//	public void validateCreateProfile() throws Exception {
//		WCS_LoginPage wcsLoginPage= new WCS_LoginPage();
//		PM_LoginPage pmLoginPage = new PM_LoginPage();
//		PM_HomePage pmHomePage = new PM_HomePage();
//		String mailId = funLibrary.getUniqueEmaild();
//		String db_firstName = "", db_lastName = "", db_userId = "", db_email1,db_phone1,db_phone1Type;
//		int i=0;
//		driver.get(System.getProperty("SIT_URL"));
//		wcsLoginPage.clickLoginSignup().
//		clickNewToColesLink().enterFirstName(excelData.get("FirstName")).enterLastName(excelData.get("LastName"))
//		.enterEmailID(mailId).enterPassword(excelData.get("Password")).
//		ClickContinueButton().enterAddressDetails(excelData.get("streetAddr"),excelData.get("suburb"),excelData.get("postcode"),excelData.get("Nickname"),excelData.get("MobileNo"));
//		DatabaseUtilities dbUtil = new DatabaseUtilities();
//		Thread.sleep(5000);
//		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + mailId + "'");
//		db_firstName = dbUtil.getValues("ADDRESS", "FIRSTNAME", "MEMBER_ID='" + db_userId + "'");
//		db_lastName = dbUtil.getValues("ADDRESS", "LASTNAME", "MEMBER_ID='" + db_userId + "'");
//		db_email1 = dbUtil.getValues("ADDRESS", "EMAIL1", "MEMBER_ID='" + db_userId + "'");
//		db_phone1 = dbUtil.getValues("ADDRESS", "PHONE1", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
//		db_phone1Type = dbUtil.getValues("ADDRESS", "PHONE1TYPE", "MEMBER_ID='" + db_userId + "' and ADDRESSTYPE='B' and STATUS='P'");
//		funLibrary.validate_Equals("FirstName", excelData.get("FirstName"), db_firstName);
//		funLibrary.validate_Equals("LastName", excelData.get("LastName"), db_lastName);
//		funLibrary.validate_Equals("EmailID", mailId, db_email1);
//		funLibrary.validate_Equals("MobileNumber", excelData.get("MobileNo"), db_phone1);
//		funLibrary.validate_Equals("PhoneType", "MPN", db_phone1Type);
//				
//		while(i<25) {
//			if(dbUtil.getValues("WCSOWNER.XCOLTOCCP", "MEMBER_ID", "MEMBER_ID='" + db_userId + "'").equals(db_userId)) {
//				Thread.sleep(5000);
//				++i;
//			}else {
//				break;
//			}
//		}
//		dbUtil.closeDBConnection();
//		funLibrary.openURL(System.getProperty("PM_URL"));
//		pmLoginPage.enterUserID(db_email1).clickTermAndCondition().clickContinueButton().enterPassword(excelData.get("Password")).clickLogin();
//		funLibrary.validate_Equals("FirstName", excelData.get("FirstName"), pmHomePage.getFirstName());
//		funLibrary.validate_Equals("LastName", excelData.get("LastName"), pmHomePage.getLastName());
//		funLibrary.validate_Equals("MobileNumber", excelData.get("MobileNo"), pmHomePage.getMobileNumber());
//		funLibrary.Assert.assertAll();
//	}

	@Test(description = "Validate that ccp profile updates when user updates the col profile")
	public void validateUpdateProfile() throws Exception {
		WCS_LoginPage wcsLoginPage= new WCS_LoginPage();
		PM_LoginPage pmLoginPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		String firstName = "AutomationTest" + funLibrary.generateRandomString(5);
		String lastName = "user" + funLibrary.generateRandomString(5);
		Integer random = 10000000+new Random().nextInt(90000000);
		String mobileNo = "04" + random.toString();
		String db_userId="";
		int i=0;
		driver.get(System.getProperty("SIT_URL"));
		
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickAccountEdit()
		.enterFirstName(firstName).enterLastName(lastName).enterMobileNo(mobileNo).clickSaveChanges();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		
		while(i<25) {
			if(dbUtil.getValues("WCSOWNER.XCOLTOCCP", "MEMBER_ID", "MEMBER_ID='" + db_userId + "'").equals(db_userId)) {
				Thread.sleep(5000);
				++i;
			}else {
				break;
			}
		}
		funLibrary.openURL(System.getProperty("PM_URL"));
		pmLoginPage.enterUserID(excelData.get("UserName")).enterPassword(excelData.get("Password")).clickLogin();
		funLibrary.validate_Equals("FirstName", firstName, pmHomePage.getFirstName());
		funLibrary.validate_Equals("LastName", lastName, pmHomePage.getLastName());
		funLibrary.validate_Equals("MobileNumber", mobileNo, pmHomePage.getMobileNumber());
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateCreateAndDeleteContact() throws Exception {
		WCS_LoginPage wcsLoginPage= new WCS_LoginPage();
		WCS_HomePage wcsHomePage= new WCS_HomePage();
		PM_LoginPage pmLoginPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		String db_userId="";
		int i=0;
		driver.get(System.getProperty("SIT_URL"));
		
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickAccountEdit()
		.clickChangeContactNoLink().enterHomeNo(excelData.get("HomeNo")).clickSaveChanges();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		
		while(i<25) {
			if(dbUtil.getValues("WCSOWNER.XCOLTOCCP", "MEMBER_ID", "MEMBER_ID='" + db_userId + "'").equals(db_userId)) {
				Thread.sleep(5000);
				++i;
			}else {
				break;
			}
		}
		funLibrary.openURL(System.getProperty("PM_URL"));
		pmLoginPage.enterUserID(excelData.get("UserName")).enterPassword(excelData.get("Password")).clickLogin();
		funLibrary.validate_Equals("HomeNumber", excelData.get("HomeNo"), pmHomePage.getHomeNumber());
		funLibrary.validate_Equals("MoileNumber", "", pmHomePage.getMobileNumber());
		driver.get(System.getProperty("SIT_URL"));
//		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
//		.clickLogin().clickWelcomeBackOKButton()
		wcsHomePage.clickMyAccount().clickAccountEdit()
		.clickChangeContactNoLink().enterMobileNo(excelData.get("MobileNo")).clickSaveChanges();
		while(i<25) {
			if(dbUtil.getValues("WCSOWNER.XCOLTOCCP", "MEMBER_ID", "MEMBER_ID='" + db_userId + "'").equals(db_userId)) {
				Thread.sleep(5000);
				++i;
			}else {
				break;
			}
		}
		funLibrary.openURL(System.getProperty("PM_URL"));
		//pmLoginPage.enterUserID(excelData.get("UserName")).clickTermAndCondition().clickContinueButton().enterPassword(excelData.get("Password")).clickLogin();
		funLibrary.validate_Equals("HomeNumber", "", pmHomePage.getHomeNumber());
		funLibrary.validate_Equals("MoileNumber", excelData.get("MobileNo"), pmHomePage.getMobileNumber());
		funLibrary.Assert.assertAll();
	}
	
	public void validateSecurityLevelPreference() throws Exception {
		WCS_LoginPage wcsLoginPage= new WCS_LoginPage();
		PM_LoginPage pmLoginPage = new PM_LoginPage();
		PM_HomePage pmHomePage = new PM_HomePage();
		String firstName = "AutomationTest" + funLibrary.generateRandomString(5);
		String db_userId="";
		int i=0;
		driver.get(excelData.get("SIT_URL"));
		
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickAccountEdit()
		.clickChangeSecurityPreference().clickSecurityPreference_High().clickSaveChanges().clickSecurityPreference_OK_Button().clickLogout();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		db_userId = dbUtil.getValues("USERS", "USERS_ID", "FIELD1='" + excelData.get("UserName") + "'");
		
		while(i<25) {
			if(dbUtil.getValues("WCSOWNER.XCOLTOCCP", "MEMBER_ID", "MEMBER_ID='" + db_userId + "'").equals(db_userId)) {
				Thread.sleep(5000);
				++i;
			}else {
				break;
			}
		}
		funLibrary.openURL(System.getProperty("PM_URL"));
		pmLoginPage.enterUserID(excelData.get("UserName")).clickTermAndCondition().clickContinueButton().enterPassword(excelData.get("Password")).clickLogin();
		funLibrary.validate_Equals("High", firstName, pmHomePage.getSecurityPreference());
		funLibrary.Assert.assertAll();
		//changing preference back to Low 
		pmHomePage.clickChangePrefernce().selectSecurityPreference("Low").clickSavePreference();
	}
}

