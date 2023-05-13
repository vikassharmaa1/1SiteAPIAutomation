package wcs.ccp.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

import addresses.pojo.AddressData;
import addresses.pojo.Contact;
import addresses.pojo.Root;
import io.restassured.response.Response;

public class COLToCCP_AddAddress_Sync extends Base_Class_API {

	WebDriver driver = null;
	FunLibrary funLibrary;

	Logger testLog = Logger.getLogger("Log:");
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

	@Test(description = "AC01 - verify that address gets added to ccp profle whe user creates in coles profile. ccp id is known at coles side.")
	public void validateAddressAdditionSync_ExistingUser() throws Exception {

		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();
		String nickName = "RandomNickName-" + funLibrary.generateRandomString(5);
		int i = 0;
		
		// login in coles online and Add Address
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password")).clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickMyAddresses().clickAddNewADdress().enterStreetAddress(excelData.get("StreetAddress"))
				.enterSuburb(excelData.get("Suburb")).enterPostCode(excelData.get("Postcode")).selectState(excelData.get("State")).enterNickName(nickName).enterFirstName(excelData.get("FirstName")).enterLastName(excelData.get("LastName")).enterMobileNumber(excelData.get("MobileNo"))
				.clickSaveChanges().selectFirstAddress();

		DatabaseUtilities dbUtil = new DatabaseUtilities();
		Thread.sleep(5000);
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + nickName + "'");
		if(db_AddressId.isEmpty()) {
			testLog.error("Something went wrong. Address is not addded in coles profile");
		} else {
			String db_status = null;
			while (i < 30) {
				db_status = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + nickName + "'");
				if(db_status.equals("SUCCESS")) {
					break;
				} else {
					Thread.sleep(5000);
					++i;
				}
			}

			if(!db_status.equals("SUCCESS")) {
				testLog.error("Address sync status is still not success after waiting for 120 seconds");
				funLibrary.validate_Equals("STATUS", "SUCCESS", db_status);
				funLibrary.Assert.assertAll();
			}

			// getting ccp token
			CCPToken ct = new CCPToken();
			String BearerToken = ct.getToken();
			// get ccp profile id
			String db_CCP_ProfileID = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE", "MBRATTR_ID='1050' and member_id=(select users_id from users where field1='" + excelData.get("UserName") + "')");

			RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au", "/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "GET");
			restLibrary.addHeader("Authorization", "Bearer " + BearerToken);

			Response response = restLibrary.executeAPI();

			response.body().prettyPrint();
			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			// checking the nickname exists in ccp
			AddressData addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(nickName)).findFirst().orElse(null);

			if(addressData.equals(null)) {
				testLog.error("Address is not synced to CCP");
			} else {
				// validating address sync ccp profile
				funLibrary.validate_Equals("Nick Name", nickName, addressData.getNickname());
				funLibrary.validate_Equals("recipientName", excelData.get("FirstName"), addressData.getRecipientFirstName());
				funLibrary.validate_Equals("recipientLastName", excelData.get("LastName"), addressData.getRecipientLastName());
				funLibrary.validate_Equals("Address Type", "S", addressData.getAddressType());
				funLibrary.validate_Equals("Address Line 1", excelData.get("StreetAddress"), addressData.getAddressLine1());
				funLibrary.validate_Equals("Postcode", excelData.get("Postcode"), addressData.getPostcode());
				funLibrary.validate_Equals("City", excelData.get("Suburb"), addressData.getCity());
				funLibrary.validate_Equals("State", excelData.get("State"), addressData.getState());
				funLibrary.validate_Equals("Country", excelData.get("Country"), addressData.getCountry());

				funLibrary.validate_NotNull("Verification Id", addressData.getVerificationId());

				List<Contact> contact = addressData.getContacts();
				funLibrary.validate_Equals("Phone", excelData.get("MobileNo"), contact.get(0).getValue());
				funLibrary.validate_Equals("Email", excelData.get("UserName"), contact.get(1).getValue());

				funLibrary.Assert.assertAll();
			}

		}
	}

}
