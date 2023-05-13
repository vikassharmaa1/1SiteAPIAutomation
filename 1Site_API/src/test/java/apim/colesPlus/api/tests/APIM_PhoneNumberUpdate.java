package apim.colesPlus.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class APIM_PhoneNumberUpdate extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
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
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
		
	}
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
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
	
	
	@Test(description = "AC02- Verify phone number update when is associated to an active account")
	public void validateDuplicatePhnNumber_ActiveAcnt() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String typeDBUser1 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String ATTRIBUTEVALUE = dbUtil.getValues("wcsowner.x_aux_plandata", "ATTRIBUTEVALUE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String ATTRIBUTENAME = dbUtil.getValues("wcsowner.x_aux_plandata", "ATTRIBUTENAME", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		
		if(typeDBUser1.equals("COLESPLUSTRIAL")) {
			
			funLibrary.Assert.assertTrue(true, "First user is Coles plus member");
			funLibrary.testLog.info("First user is Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "First user is Non Coles plus member");
			funLibrary.testLog.info("First user is Non Coles plus member");
		}
		
		
		String typeDBUser2 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "'");

		if(typeDBUser2.equals("No Record Found")) {
			
			funLibrary.Assert.assertTrue(true, "Second user is Non Coles plus member");
			funLibrary.testLog.info("Second user is Non Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Second user is Coles plus member");
			funLibrary.testLog.info("Second user is Coles plus member");
		}
		
		String phoneUpdateResponse = funLibrary.getJsonPathValue(response, "exists");
		funLibrary.validate_Equals("response", phoneUpdateResponse, "true");
		String phone1DBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1DBUser2.equals(ATTRIBUTEVALUE)) {
			
			funLibrary.Assert.assertTrue(true, "Phone number updated for second user");
			funLibrary.testLog.info("Phone number updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone number not updated for second user");
			funLibrary.testLog.info("Phone number not updated for second user");
		}
		
		String phone1TypeDBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1TypeDBUser2.equals("MPN") && ATTRIBUTENAME.equals("phone")) {
			
			funLibrary.Assert.assertTrue(true, "Phone type updated for second user");
			funLibrary.testLog.info("Phone type updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone type not updated for second user");
			funLibrary.testLog.info("Phone type not updated for second user");
		}
		
		funLibrary.Assert.assertAll();
		
		
	}
	
	
	@Test(description = "AC03- Verify phone number update when is associated to an disabled account")
	public void validateDuplicatePhnNumber_DisabledAcnt() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String typeDBUser1 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String ATTRIBUTEVALUE = dbUtil.getValues("wcsowner.x_aux_plandata", "ATTRIBUTEVALUE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String ATTRIBUTENAME = dbUtil.getValues("wcsowner.x_aux_plandata", "ATTRIBUTENAME", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String disabledDBUser1 = dbUtil.getValues("wcsowner.userreg", "STATUS", "users_id = '" + FunLibrary.excelData.get("userId") + "'");
		
		if(typeDBUser1.equals("COLESPLUSTRIAL")) {
			
			funLibrary.Assert.assertTrue(true, "First user is Coles plus member");
			funLibrary.testLog.info("First user is Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "First user is Non Coles plus member");
			funLibrary.testLog.info("First user is Non Coles plus member");
		}
		
		if(disabledDBUser1.equals("0")) {
			
			funLibrary.Assert.assertTrue(true, "First user is disabled/inactive member");
			funLibrary.testLog.info("First user is disabled/inactive member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "First user is active member");
			funLibrary.testLog.info("First user is active member");
		}
		
		
		String typeDBUser2 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "'");

		if(typeDBUser2.equals("No Record Found")) {
			
			funLibrary.Assert.assertTrue(true, "Second user is Non Coles plus member");
			funLibrary.testLog.info("Second user is Non Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Second user is Coles plus member");
			funLibrary.testLog.info("Second user is Coles plus member");
		}
		
		String phoneUpdateResponse = funLibrary.getJsonPathValue(response, "exists");
		funLibrary.validate_Equals("response", phoneUpdateResponse, "true");
		String phone1DBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1DBUser2.equals(ATTRIBUTEVALUE)) {
			
			funLibrary.Assert.assertTrue(true, "Phone number updated for second user");
			funLibrary.testLog.info("Phone number updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone number not updated for second user");
			funLibrary.testLog.info("Phone number not updated for second user");
		}
		
		String phone1TypeDBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1TypeDBUser2.equals("MPN") && ATTRIBUTENAME.equals("phone")) {
			
			funLibrary.Assert.assertTrue(true, "Phone type updated for second user");
			funLibrary.testLog.info("Phone type updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone type not updated for second user");
			funLibrary.testLog.info("Phone type not updated for second user");
		}
		
		funLibrary.Assert.assertAll();
		
		
	}
	
	
	
	@Test(description = "AC04- Verify phone number update when is associated to an active account")
	public void validateDuplicatePhnNumber_NonColesPlusAcnt() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String typeDBUser1 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		String phone1DBUser1 = dbUtil.getValues("wcsowner.address", "PHONE1", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and Status ='P' and SELFADDRESS ='1'");
		String phone1TypeDBUser1 = dbUtil.getValues("wcsowner.address", "PHONE1TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and Status ='P' and SELFADDRESS ='1'");
		if(typeDBUser1.equals("No Record Found")) {
			
			funLibrary.Assert.assertTrue(true, "First user is Non Coles plus member");
			funLibrary.testLog.info("First user is Non Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "First user is Coles plus member");
			funLibrary.testLog.info("First user is Coles plus member");
		}
		
		
		String typeDBUser2 = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "'");

		if(typeDBUser2.equals("No Record Found")) {
			
			funLibrary.Assert.assertTrue(true, "Second user is Non Coles plus member");
			funLibrary.testLog.info("Second user is Non Coles plus member");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Second user is Coles plus member");
			funLibrary.testLog.info("Second user is Coles plus member");
		}
		
		String phoneUpdateResponse = funLibrary.getJsonPathValue(response, "exists");
		funLibrary.validate_Equals("response", phoneUpdateResponse, "false");
		String phone1DBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1DBUser2.equals(phone1DBUser1)) {
			
			funLibrary.Assert.assertTrue(true, "Phone number updated for second user");
			funLibrary.testLog.info("Phone number updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone number not updated for second user");
			funLibrary.testLog.info("Phone number not updated for second user");
		}
		
		String phone1TypeDBUser2 = dbUtil.getValues("wcsowner.address", "PHONE1TYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId2") + "' and Status ='P' and SELFADDRESS ='1'");
		if(phone1TypeDBUser2.equals(phone1TypeDBUser1)) {
			
			funLibrary.Assert.assertTrue(true, "Phone type updated for second user");
			funLibrary.testLog.info("Phone type updated for second user");
			
		}
		else {
			funLibrary.Assert.assertTrue(false, "Phone type not updated for second user");
			funLibrary.testLog.info("Phone type not updated for second user");
		}
		
		funLibrary.Assert.assertAll();
		
		
	}
	
	
	@Test(description = "AC05- Verify when phone number passed from CUSP is invalid")
	public void validateInvalidPhnNumber() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),FunLibrary.excelData.get("QueryParamValue1"));
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
