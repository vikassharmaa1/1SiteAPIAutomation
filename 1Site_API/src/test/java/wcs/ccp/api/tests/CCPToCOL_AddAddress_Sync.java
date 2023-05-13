package wcs.ccp.api.tests;

import java.io.File;
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

import com.page.objects.PM_LoginPage;
import com.page.objects.WCS_LoginPage;
import com.page.objects.WCS_MyAddressesPage;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.XLS_Reader;

import io.restassured.response.Response;

public class CCPToCOL_AddAddress_Sync extends Base_Class_API {

	@BeforeMethod()
	@Parameters({ "sheetname" })
	public void setup(String sheetname, Method method) {
		int currentRow = 0;
		getProperties();
		XLS_Reader datatable = new XLS_Reader(System.getProperty("user.dir") + System.getProperty("DataFilePath")
				+ System.getProperty("DataFile_WCS"));
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + System.getProperty("ChromeDriver"));
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		funLibrary = new FunLibrary(driver);
		for (int i = 2; i <= datatable.getRowCount(sheetname); i++) {
			if (datatable.getCellData(sheetname, "TestMethodName", i).equals(method.getName())) {
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
			properties
					.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/java/baseConfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PropertyConfigurator.configure(properties);
		for (String name : properties.stringPropertyNames()) {
			String value = properties.getProperty(name);
			System.setProperty(name, value);
		}
	}

	// CCP profile is created successfully using PM UI Url
	public void validateCreateProfile(String firstName, String lastName, String mailId) throws Exception {
		PM_LoginPage pmLognPage = new PM_LoginPage();

		driver.get(System.getProperty("PM_URL"));
		pmLognPage.registerCCPUser(mailId, firstName, lastName);

	}

	// description = "Create address for users using CCP API
	public void createAddress(String mailId) throws Exception {
		File jsonbody = new File(
				"C:\\OneSite\\Test\\1SITEAPIAUTOMATION\\1Site_API\\src\\test\\resources\\__files\\json\\CCPToCOL_AddAddress.json");
		// Adding new adddress via CCP API
		CCPToken ct = new CCPToken();
		String BearerToken = ct.getToken();

		// get ccp profile id -- connect to coles DB
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_CCP_ProfileID = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE", 
				"MBRATTR_ID='1050' and member_id=(select users_id from users where field1='"+ mailId + "')");
		RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au",
				"/digital/customer-profile/v1/CustomerProfiles/" + db_CCP_ProfileID + "/addresses", "POST");
		restLibrary.addHeader("Authorization", "Bearer " + BearerToken);
		restLibrary.addBody(jsonbody);
		Response response = restLibrary.executeAPI();
		funLibrary.validateStatusCode(response, "201");
		response.prettyPrint();
	}

	WebDriver driver = null;
	FunLibrary funLibrary;

	Logger testLog = Logger.getLogger("Log:");
	static Map<String, String> excelData = new HashMap<String, String>();
	static XLS_Reader datatable;

	// description="Verify the address sync in coles online"
	public void verifyAddressinCOL(String mailId, String password) throws Exception {
		// login in coles online and verify address sync
		
		WCS_LoginPage wcsLoginPage = new WCS_LoginPage();

		String nickName = FunLibrary.excelData.get("NickName");
		driver.get(System.getProperty("SIT_URL"));
		wcsLoginPage.clickLoginSignup().enterUserName(mailId).enterLoginPassword(password).clickLogin()
				.clickWelcomeBackOKButton().clickMyAccount().clickMyAddresses();
		
		WCS_MyAddressesPage addresspage = new WCS_MyAddressesPage();
		addresspage.editAddress(nickName);
		
		funLibrary.validate_Equals("AddressLine",FunLibrary.excelData.get("Street Address"),addresspage.getStreetAddressLine());
		funLibrary.validate_Equals("Suburb",FunLibrary.excelData.get("Suburb"),addresspage.getSuburb());
		funLibrary.validate_Equals("Postcode",FunLibrary.excelData.get("Postcode"),addresspage.getPostcode());
		//funLibrary.validate_Equals("State",FunLibrary.excelData.get("State"),addresspage.getState());
		funLibrary.validate_Equals("NickName",FunLibrary.excelData.get("NickName"),addresspage.getNickName());
			
	}

	@Test(description = "AC01 - Verify address Sync")
	public void verifyaddAddressSync() throws Exception {
		String mailId = funLibrary.getUniqueEmaild(); 
		String password = FunLibrary.excelData.get("Password");
		String firstName = FunLibrary.excelData.get("FirstName");
		String lastName = FunLibrary.excelData.get("LastName");
		validateCreateProfile(firstName,lastName,mailId);
		createAddress(mailId);
		verifyAddressinCOL(mailId, password);
	}
}
