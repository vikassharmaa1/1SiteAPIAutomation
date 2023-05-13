package ps.userAddresses.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_UserAddresses extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;
	String identifier;
	
	public void getUserAddress_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.getUserAddress_PS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userAuthorization"),             
                FunLibrary.excelData.get("StoreId"));        	
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		
		getUserAddress_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		//restLibrary.getResponseBody(response);
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "404":
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		getUserAddress_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate user addresses home delivery")
	public void validateUserAddresses_HD() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		getUserAddress_API();
		int addresscount = response.jsonPath().getInt("contact.size()");
		for(int i=0; i<addresscount; i++)
		{
			if(response.jsonPath().getString("contact["+i+"].serviceType").equalsIgnoreCase("HD"))
			{	
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				String address_id= response.jsonPath().getString("contact["+i+"].addressId");
				System.out.println(response.jsonPath().getString("contact["+i+"].personTitle"));
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "userId", dbUtil.getValues("USERS", "USERS_ID", "FIELD1='deepaktest@mailinator.com'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].serviceType", FunLibrary.excelData.get("serviceType"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].lastName", dbUtil.getValues("ADDRESS", "LASTNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].country", dbUtil.getValues("ADDRESS", "COUNTRY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].nickName", dbUtil.getValues("ADDRESS", "NICKNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].addressType", dbUtil.getValues("ADDRESS", "ADDRESSTYPE", "ADDRESS_ID='"+address_id+"'").trim());
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].postcode", dbUtil.getValues("ADDRESS", "ZIPCODE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].addressId", dbUtil.getValues("ADDRESS", "ADDRESS_ID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].phone1", dbUtil.getValues("ADDRESS", "PHONE1", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].phone1Type", dbUtil.getValues("ADDRESS", "PHONE1TYPE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].firstName", dbUtil.getValues("ADDRESS", "FIRSTNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].email1", dbUtil.getValues("ADDRESS", "EMAIL1", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].selfAddress",dbUtil.getValues("ADDRESS", "SELFADDRESS", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].suburb", dbUtil.getValues("ADDRESS", "CITY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].state", dbUtil.getValues("ADDRESS", "STATE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].primary", dbUtil.getValues("ADDRESS", "ISPRIMARY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoQASid", dbUtil.getValues("XADDRESS", "VALIDATIONID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].ccpAddressId", dbUtil.getValues("XADDRESS", "CCPADDRESSID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoLongitude", dbUtil.getValues("XADDRESS", "LONGITUDE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoLatitude", dbUtil.getValues("XADDRESS", "LATITUDE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.Assert.assertAll();
			}
		}
	}

	@Test(description = "validate user addresses remote delivery")
	public void validateUserAddresses_RD() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		getUserAddress_API();
		int addresscount = response.jsonPath().getInt("contact.size()");
		for(int i=0; i<addresscount; i++)
		{
			if(response.jsonPath().getString("contact["+i+"].serviceType")=="RD")
			{
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				String address_id= response.jsonPath().getString("contact["+i+"].addressId");
				System.out.println(response.jsonPath().getString("contact["+i+"].personTitle"));
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "userId", dbUtil.getValues("USERS", "USERS_ID", "FIELD1='deepaktest@mailinator.com'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].serviceType", FunLibrary.excelData.get("serviceType"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].lastName", dbUtil.getValues("ADDRESS", "LASTNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].country", dbUtil.getValues("ADDRESS", "COUNTRY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].nickName", dbUtil.getValues("ADDRESS", "NICKNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].addressType", dbUtil.getValues("ADDRESS", "ADDRESSTYPE", "ADDRESS_ID='"+address_id+"'").trim());
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].postcode", dbUtil.getValues("ADDRESS", "ZIPCODE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].addressId", dbUtil.getValues("ADDRESS", "ADDRESS_ID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].phone1", dbUtil.getValues("ADDRESS", "PHONE1", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].phone1Type", dbUtil.getValues("ADDRESS", "PHONE1TYPE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].firstName", dbUtil.getValues("ADDRESS", "FIRSTNAME", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].email1", dbUtil.getValues("ADDRESS", "EMAIL1", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].selfAddress",dbUtil.getValues("ADDRESS", "SELFADDRESS", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].suburb", dbUtil.getValues("ADDRESS", "CITY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].state", dbUtil.getValues("ADDRESS", "STATE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].primary", dbUtil.getValues("ADDRESS", "ISPRIMARY", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoQASid", dbUtil.getValues("XADDRESS", "VALIDATIONID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].ccpAddressId", dbUtil.getValues("XADDRESS", "CCPADDRESSID", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoLongitude", dbUtil.getValues("XADDRESS", "LONGITUDE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.validateJSONPathValue_Equals(response, "contact["+i+"].geoLatitude", dbUtil.getValues("XADDRESS", "LATITUDE", "ADDRESS_ID='"+address_id+"'"));
				funLibrary.Assert.assertAll();
			}
		}
	}

	@Test(description = "validate user addresses home delivery")
	public void validateUserwith_NoAddresses() {
		getUserAddress_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "userId", FunLibrary.excelData.get("userId"));
		funLibrary.Assert.assertAll();
	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
