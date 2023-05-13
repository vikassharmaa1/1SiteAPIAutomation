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

import com.page.objects.WCS_LoginPage;
import com.page.objects.WCS_MyAddressesPage;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

import addresses.pojo.AddressData;
import addresses.pojo.Root;
import io.restassured.response.Response;

public class COLToCCP_UpdateAddress_Sync extends Base_Class_API {

	WebDriver driver = null;
	FunLibrary funLibrary;

	Logger testLog = Logger.getLogger("Log:");
	static Map<String, String> excelData = new HashMap<String, String>();
	static XLS_Reader datatable;

	@BeforeMethod()
	@Parameters({
			"sheetname"
	})
	public void setup(String sheetname, Method method) {
		int currentRow = 0;
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
				System.out.println("Test Started:" + method.getName());
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

	@Test(description = "Update firsname, last name, email id and phone number")
	public void validateUpdateAddressFields() throws Exception {

		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();

		String firstName = "AutomationTest" + funLibrary.generateRandomString(5);
		String lastName = "user" + funLibrary.generateRandomString(5);
		Integer random = 10000000 + new Random().nextInt(90000000);
		String mobileNo = "04" + random.toString();
		String emailID = "randommailid" + funLibrary.generateRandomString(5) + "@mailinator.com";
		String db_AddressId = null;

		// login in coles online and updating address fields
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin()
		.clickWelcomeBackOKButton()
		.clickMyAccount().clickMyAddresses()
		.editAddress(excelData.get("NickName"))
		.enterFirstName(firstName)
		.enterLastName(lastName)
		.enterEmailID(emailID)
		.enterMobileNumber(mobileNo)
		.clickSaveChanges()
		.verifyAddress();

		// verify update success in database
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_CCP_ProfileID = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE", "MBRATTR_ID='1050' and member_id=(select users_id from users where field1='" + excelData.get("UserName") + "')");
		db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + excelData.get("NickName") + "' and STATUS='P' and ADDRESSTYPE='SB'");
		
		
		if(db_AddressId.equals("No Record Found")) {
			testLog.error("Something went wrong. Address HD NT is not found in coles profile");
		} else {
			int i = 0;
			String db_status = null;
			while (i < 40) {
				db_status = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_AddressId + "' and ACTION = 'addressUpdate'");
				if(db_status.equals("SUCCESS")) {
					break;
				} else {
					Thread.sleep(10000);
					++i;
				}
			}

			if(!db_status.equals("SUCCESS")) {
				testLog.error("Address Update status in coltoccpaddress table is still not success after waiting for 120 seconds");
				funLibrary.validate_Equals("STATUS", "SUCCESS", db_status);
				funLibrary.Assert.assertAll();
			}

			// getting ccp token
			CCPToken ct = new CCPToken();
			String BearerToken = ct.getToken();

			// calling ccp api to get address details
			RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au", "/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "GET");
			restLibrary.addHeader("Authorization", "Bearer " + BearerToken);
			Response response = restLibrary.executeAPI();
			response.getBody().prettyPrint();
			// Filter JSON response for updated address using nickname
			Root root = (Root) restLibrary.getResponseBody(response, Root.class);
			AddressData addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(excelData.get("NickName"))).findFirst().orElse(null);

			if(addressData == null) {
				testLog.error("Address " + excelData.get("NickName") + " is not present in ccp profile");
				funLibrary.assertCheck("Address addition", "Address " + excelData.get("NickName") + " is not present in ccp profile");
			} else

				// validating address sync ccp profile
				funLibrary.validate_Equals("First Name",firstName, addressData.getRecipientFirstName());
			funLibrary.validate_Equals("Last Name", lastName, addressData.getRecipientLastName());
			funLibrary.validate_Equals("Phone", mobileNo, addressData.getContacts().get(0).getValue());
			funLibrary.validate_Equals("Email", emailID, addressData.getContacts().get(1).getValue());

		}

		funLibrary.Assert.assertAll();
	}

	@Test(description = "Update Nickname")
	public void validateUpdateAddressFields_NickName() throws Exception {

		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();

		String nickName = "AutomationTest" + funLibrary.generateRandomString(5);
		String db_OldAddressId = null, db_NewAddressId=null;

		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_CCP_ProfileID = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE", "MBRATTR_ID='1050' and member_id=(select users_id from users where field1='" + excelData.get("UserName") + "')");
		db_OldAddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and STATUS='P' and ADDRESSTYPE='SB'");
		
		// login in coles online and updating address fields
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickMyAddresses().editFirstAddress().enterNickName(nickName)
		.clickSaveChanges().verifyAddress();
		Thread.sleep(5000);
		// verify update success in database
		db_NewAddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + nickName + "' and STATUS='P' and ADDRESSTYPE='SB'");
		if(db_NewAddressId.equals("No Record Found")) {
			testLog.error("Something went wrong. Address "+nickName+" is not found in coles profile");
		} else {
			int i = 0;
			String db_status1 = null, db_status2 = null;
			while (i < 30) {
				db_status1 = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_OldAddressId + "' and ACTION = 'addressDelete'");
				db_status2 = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_NewAddressId + "' and ACTION = 'addressCreate'");
				if(db_status1.equals("SUCCESS") && db_status2.equals("SUCCESS")) {
					break;
				} else {
					Thread.sleep(10000);
					++i;
				}
			}

			if(!(db_status1.equals("SUCCESS") && db_status2.equals("SUCCESS"))) {
				testLog.error("Address Update status in coltoccpaddress table is still not success after waiting for 120 seconds");
				funLibrary.validate_Equals("STATUS", "SUCCESS", db_status1);
				funLibrary.validate_Equals("STATUS", "SUCCESS", db_status2);
				funLibrary.Assert.assertAll();
			}

			// getting ccp token
			CCPToken ct = new CCPToken();
			String BearerToken = ct.getToken();

			// calling ccp api to get address details
			RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au", "/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "GET");
			restLibrary.addHeader("Authorization", "Bearer " + BearerToken);
			Response response = restLibrary.executeAPI();
			response.getBody().prettyPrint();
			// Filter JSON response for updated address using nickname
			Root root = (Root) restLibrary.getResponseBody(response, Root.class);
			AddressData addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(nickName)).findFirst().orElse(null);

			if(addressData == null) {
				testLog.error("Address " + nickName + " is not present in ccp profile");
				funLibrary.assertCheck("Address addition", "Address " + nickName + " is not present in ccp profile");
			} else

				// validating address sync ccp profile
				funLibrary.validate_Equals("First Name", excelData.get("FirstName"), addressData.getRecipientFirstName());
			funLibrary.validate_Equals("Last Name", excelData.get("LastName"), addressData.getRecipientLastName());
			funLibrary.validate_Equals("Nick Name", nickName, addressData.getNickname());
			funLibrary.validate_Equals("Phone", excelData.get("MobileNo"), addressData.getContacts().get(0).getValue());
			funLibrary.validate_Equals("Email", excelData.get("UserName"), addressData.getContacts().get(1).getValue());

		}

		funLibrary.Assert.assertAll();
	}
	

	@Test(description = "validate that updating address line will remove the existing address and create new address")
	public void validateUpdateAddressLine() throws Exception {

		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();
		WCS_MyAddressesPage wcsMyAddressesPage = new WCS_MyAddressesPage();
		
		String nickName = excelData.get("NickName");
		String db_AddressId = null;
		int i = 0;
		String db_status1 = null;
		// login in coles online and updating address fields
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password"))
		.clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickMyAddresses().editAddress(nickName)
		.enterStreetAddress(excelData.get("StreetAddress")).enterSuburb(excelData.get("Suburb")).enterPostCode(excelData.get("Postcode"))
		.selectState(excelData.get("State")).clickSaveChanges().verifyAddress();
		Thread.sleep(5000);
		// verify update success in database
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_CCP_ProfileID = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE", "MBRATTR_ID='1050' and member_id=(select users_id from users where field1='" + excelData.get("UserName") + "')");
		db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + nickName + "' and STATUS='P' and ADDRESSTYPE='SB'");
		if(db_AddressId.isEmpty()) {
			testLog.error("Something went wrong. Address "+nickName+" is not found in coles profile");
		} else {
			
			while (i < 30) {
				db_status1 = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_AddressId + "' and ACTION = 'addressUpdateAdd'");
				if(db_status1.equals("SUCCESS")) {
					break;
				} else {
					Thread.sleep(10000);
					++i;
				}
			}

			if(!db_status1.equals("SUCCESS")) {
				testLog.error("Address Update status in coltoccpaddress table is still not success after waiting for 120 seconds");
				funLibrary.validate_Equals("STATUS", "SUCCESS", db_status1);
				funLibrary.Assert.assertAll();
			}

			// getting ccp token
			CCPToken ct = new CCPToken();
			String BearerToken = ct.getToken();

			// calling ccp api to get address details
			RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au", "/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "GET");
			restLibrary.addHeader("Authorization", "Bearer " + BearerToken);
			Response response = restLibrary.executeAPI();
			response.getBody().prettyPrint();
			// Filter JSON response for updated address using nickname
			Root root = (Root) restLibrary.getResponseBody(response, Root.class);
			AddressData addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(nickName)).findFirst().orElse(null);

			if(addressData == null) {
				testLog.error("Address " + nickName + " is not present in ccp profile");
				funLibrary.assertCheck("Address addition", "Address " + nickName + " is not present in ccp profile");
			} else

				// validating address sync ccp profile
				funLibrary.validate_Equals("Address Line1", excelData.get("StreetAddress"), addressData.getAddressLine1());
				funLibrary.validate_Equals("First Name", excelData.get("FirstName"), addressData.getRecipientFirstName());
				funLibrary.validate_Equals("Last Name", excelData.get("LastName"), addressData.getRecipientLastName());
				funLibrary.validate_Equals("Last Name", excelData.get("NickName"), addressData.getNickname());
				funLibrary.validate_Equals("Phone", excelData.get("MobileNo"), addressData.getContacts().get(0).getValue());
				funLibrary.validate_Equals("Email", excelData.get("UserName"), addressData.getContacts().get(1).getValue());

		}
		
		wcsMyAddressesPage.editAddress(nickName).enterStreetAddress("1 Bayiew").enterSuburb("Bayview").enterPostCode("0820")
		.selectState("NT").clickSaveChanges().verifyAddress();
		db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + excelData.get("NickName") + "' and STATUS='P' and ADDRESSTYPE='SB'");
		while (i < 30) {
			db_status1 = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_AddressId + "' and ACTION = 'addressUpdateAdd'");
			if(db_status1.equals("SUCCESS")) {
				break;
			} else {
				Thread.sleep(10000);
				++i;
			}
		}

		if(!db_status1.equals("SUCCESS")) {
			testLog.error("Address Update status in coltoccpaddress table is still not success after waiting for 120 seconds");
			funLibrary.validate_Equals("STATUS", "SUCCESS", db_status1);
			funLibrary.Assert.assertAll();
		}
		dbUtil.closeDBConnection();
		

		funLibrary.Assert.assertAll();
	}
}
