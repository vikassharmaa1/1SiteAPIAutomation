package ps.addressContactUpdate.api.tests;

import org.junit.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PS_AddressContactUpdate extends Base_Class_API implements ITest {
	
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);

		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Update phone number for HD address")
	public void updateMPNContactHDAddress() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String ccpaddressid = jsonPathId.getString("ccpAddressId");
				String userid = jsonPathId.getString("userId");
				String addressid = jsonPathId.getString("addressId");
				
			    
		        // Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				
				String ccpaddress = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
						"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") );
			
			
				String memberid = dbUtil.getValues("ADDRESS", "MEMBER_ID",
						"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
				String phonenumber = dbUtil.getValues("ADDRESS", "PHONE1", 
						"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
				String phonetype = dbUtil.getValues("ADDRESS", "PHONE1TYPE", 
						"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");

				// Verify response
				
		         
				funLibrary.validate_Equals("CCP_ADDRESS_ID", ccpaddress, ccpaddressid);
				funLibrary.validate_Equals("MEMBER_ID", memberid, userid);
				funLibrary.validate_Equals("PHONE_NUMBER", phonenumber, FunLibrary.excelData.get("Phone_Number"));
				funLibrary.validate_Contains("PHONE_TYPE", "MPN", phonetype);
				funLibrary.validate_Equals("ADDRESS_ID", FunLibrary.excelData.get("ADDRESS_ID"),addressid);

				funLibrary.Assert.assertAll();
				}

	
	
	
	@Test(description = "Update phone number for HD address")
	public void updateHPNContactHDAddress() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
        JsonPath jsonPathId = response.jsonPath();
		
      //Get the required path of each field
		String ccpaddressid = jsonPathId.getString("ccpAddressId");
		String userid = jsonPathId.getString("userId");
		String addressid = jsonPathId.getString("addressId");
		
	    
        // Get the value from database
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String ccpaddress = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") );
	
	
		String memberid = dbUtil.getValues("ADDRESS", "MEMBER_ID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonenumber = dbUtil.getValues("ADDRESS", "PHONE1", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonetype = dbUtil.getValues("ADDRESS", "PHONE1TYPE", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");

		// Verify response
		
         
		funLibrary.validate_Equals("CCP_ADDRESS_ID", ccpaddress, ccpaddressid);
		funLibrary.validate_Equals("MEMBER_ID", memberid, userid);
		funLibrary.validate_Equals("PHONE_NUMBER", phonenumber, FunLibrary.excelData.get("Phone_Number"));
		funLibrary.validate_Contains("PHONE_TYPE", "HPN", phonetype);
		funLibrary.validate_Equals("ADDRESS_ID", FunLibrary.excelData.get("ADDRESS_ID"),addressid);

		funLibrary.Assert.assertAll();
		
		
	}
	
	@Test(description = "Update phone number for RD address")
	public void updateMPNContactRDAddress() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
        JsonPath jsonPathId = response.jsonPath();
		
      //Get the required path of each field
		String ccpaddressid = jsonPathId.getString("ccpAddressId");
		String userid = jsonPathId.getString("userId");
		String addressid = jsonPathId.getString("addressId");
		
	    
        // Get the value from database
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String ccpaddress = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") );
	
	
		String memberid = dbUtil.getValues("ADDRESS", "MEMBER_ID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonenumber = dbUtil.getValues("ADDRESS", "PHONE1", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonetype = dbUtil.getValues("ADDRESS", "PHONE1TYPE", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");

		// Verify response
		
         
		funLibrary.validate_Equals("CCP_ADDRESS_ID", ccpaddress, ccpaddressid);
		funLibrary.validate_Equals("MEMBER_ID", memberid, userid);
		funLibrary.validate_Equals("PHONE_NUMBER", phonenumber, FunLibrary.excelData.get("Phone_Number"));
		funLibrary.validate_Contains("PHONE_TYPE", "MPN", phonetype);
		funLibrary.validate_Equals("ADDRESS_ID", FunLibrary.excelData.get("ADDRESS_ID"),addressid);

		funLibrary.Assert.assertAll();
		
		
	}
	
	@Test(description = "Update phone number for RD address")
	public void updateHPNContactRDAddress() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
        JsonPath jsonPathId = response.jsonPath();
		
      //Get the required path of each field
		String ccpaddressid = jsonPathId.getString("ccpAddressId");
		String userid = jsonPathId.getString("userId");
		String addressid = jsonPathId.getString("addressId");
		
	    
        // Get the value from database
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String ccpaddress = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") );
	
	
		String memberid = dbUtil.getValues("ADDRESS", "MEMBER_ID",
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonenumber = dbUtil.getValues("ADDRESS", "PHONE1", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");
		String phonetype = dbUtil.getValues("ADDRESS", "PHONE1TYPE", 
				"address_id=" + FunLibrary.excelData.get("ADDRESS_ID") + " and status='P'");

		// Verify response
		
         
		funLibrary.validate_Equals("CCP_ADDRESS_ID", ccpaddress, ccpaddressid);
		funLibrary.validate_Equals("MEMBER_ID", memberid, userid);
		funLibrary.validate_Equals("PHONE_NUMBER", phonenumber, FunLibrary.excelData.get("Phone_Number"));
		funLibrary.validate_Contains("PHONE_TYPE", "HPN", phonetype);
		funLibrary.validate_Equals("ADDRESS_ID", FunLibrary.excelData.get("ADDRESS_ID"),addressid);

		funLibrary.Assert.assertAll();
		
		
	}
	
	
	
	@Test(description = "Address id doesn't belong to user")
	public void invalidCcpAddressId() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}
	
	@Test(description = "mandatory parameter missing(CCPProfileid)")
	public void mandatoryParameterMissing() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}
	
	@Test(description = "Incorrect address id")
	public void incorrectCcpAddressId() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}
	
	@Test(description = "Incorrect profile id")
	public void incorrectCcpProfileId() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}



	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
