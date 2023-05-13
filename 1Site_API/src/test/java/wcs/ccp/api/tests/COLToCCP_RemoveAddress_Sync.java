package wcs.ccp.api.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import com.page.objects.WCS_MyAddressesPage;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

import addresses.pojo.AddressData;
import addresses.pojo.Root;
import io.restassured.response.Response;

public class COLToCCP_RemoveAddress_Sync extends Base_Class_API {

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

	@Test(description = "verify that Removing an address from coles removes it from the ccp profile")
	public void validateRemoveAddressSync() throws Exception {

		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();
		WCS_MyAddressesPage wcsAddressPage = new WCS_MyAddressesPage();
		String nickName = "RandomNickName-" + funLibrary.generateRandomString(5);
		int i = 0;
		String db_AddressId = null;

		// login in coles online and Add Address
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(excelData.get("UserName")).enterLoginPassword(excelData.get("Password")).clickLogin().clickWelcomeBackOKButton().clickMyAccount().clickMyAddresses().clickAddNewADdress().enterStreetAddress(excelData.get("StreetAddress"))
				.enterSuburb(excelData.get("Suburb")).enterPostCode(excelData.get("Postcode")).selectState(excelData.get("State")).enterNickName(nickName).enterFirstName(excelData.get("FirstName")).enterLastName(excelData.get("LastName")).enterMobileNumber(excelData.get("MobileNo"))
				.clickSaveChanges().selectFirstAddress();
		// validating in database that address is added successfully
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		Thread.sleep(5000);
		db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + excelData.get("UserName") + "') and NICKNAME='" + nickName + "'");
		if(db_AddressId.isEmpty()) {
			testLog.error("Something went wrong. Address is not addded in coles profile");
		} else {
			
			String db_status = null;
			while (i < 30) {
				db_status = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_AddressId + "' and ACTION = 'addressCreate'");
				if(db_status.equals("SUCCESS")) {
					break;
				} else {
					Thread.sleep(10000);
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
			// get ccp addresses
			RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au", "/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "GET");
			restLibrary.addHeader("Authorization", "Bearer " + BearerToken);

			Response response = restLibrary.executeAPI();

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			// checking the nickname exists in ccp
			AddressData addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(nickName)).findFirst().orElse(null);

			if(addressData == null) {
				testLog.error("Address is not added to ccp profile");
				funLibrary.assertCheck("Address addition", "Address is not added to ccp profile");
			} else {

				// login in coles online and deleting the address
				wcsAddressPage.deleteAddress(nickName).deleteAddress_ConfirmButton();

				while (i < 25) {
					db_status = dbUtil.getValues("WCSOWNER.XCOLTOCCPADDRESS", "STATUS", "MEMBER_ID=(select users_id from users where field1='" + excelData.get("UserName") + "') and COL_ADDRESSID='" + db_AddressId + "' and ACTION = 'addressDelete'");
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

				// again call api and check for the address in api response and cehck address count
				response = restLibrary.executeAPI();
				root = (Root) restLibrary.getResponseBody(response, Root.class);

				// checking the nickname exists in ccp
				addressData = root.getAddresses().stream().filter(x -> x.getNickname().equalsIgnoreCase(nickName)).findFirst().orElse(null);

				if(addressData == null) {
					testLog.info("Address Deleted successfully from CCP");
				} else {
					testLog.error("Address is not deleted from CCP");
					funLibrary.assertCheck("DeleteAddress", "Address is not deleted from CCP");
				}

				funLibrary.Assert.assertAll();
			}

		}
	}

}
