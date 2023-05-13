package wcs.cartAttributes.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.api.WCS;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_RD_CartAttributes extends Base_Class_API implements ITest{
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	

	@Test(description = "AC01 & AC02- Verify profile udate")
	public void validateProfileUpdate() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		//Verify ORDERS_ID in db and json
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String orderIdDB = dbUtil.getValues("SHIPINFO", "ORDERS_ID", "ORDERS_ID = '" + orderIdJson + "'");
		funLibrary.validate_Equals("orderId", orderIdJson, orderIdDB);
		
		//Verify Instructions in SHIPINFO and XADDRESS are saved correctly
		
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		funLibrary.validate_Equals("Instructions", instructionsDB_Shipinfo, instructionsDB_XADDRESS);
		
		if(instructionsDB_Shipinfo.contains("__ADDRESS") && instructionsDB_Shipinfo.contains("__ACCOUNT") && instructionsDB_Shipinfo.contains("__INSTRUCTIONS")) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct separator in Shipinfo table");
			funLibrary.testLog.info("Instructions saved with correct separator in Shipinfo table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct separator in Shipinfo table");
			funLibrary.testLog.info("Instructions not saved with correct separator in Shipinfo table");
		}
		
		
		if(instructionsDB_XADDRESS.contains("__ADDRESS") && instructionsDB_XADDRESS.contains("__ACCOUNT") && instructionsDB_XADDRESS.contains("__INSTRUCTIONS")) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct separator in XADDRESS table");
			funLibrary.testLog.info("Instructions saved with correct separator in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct separator in XADDRESS table");
			funLibrary.testLog.info("Instructions not saved with correct separator in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	
	@Test(description = "AC03- Verify delivery address update")
	public void validateDeliveryAddressUpdate() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		//Verify ORDERS_ID in db and json
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String orderIdDB = dbUtil.getValues("SHIPINFO", "ORDERS_ID", "ORDERS_ID = '" + orderIdJson + "'");
		funLibrary.validate_Equals("orderId", orderIdJson, orderIdDB);
		
		//Verify Instructions in SHIPINFO and XADDRESS are saved correctly
		
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		funLibrary.validate_Equals("Instructions", instructionsDB_Shipinfo, instructionsDB_XADDRESS);
		
		if(instructionsDB_Shipinfo.contains("__ACCOUNT ") && !(instructionsDB_Shipinfo.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Instructions saved with correct blank values in Shipinfo table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Instructions not saved with correct blank values in Shipinfo table");
		}
		
		
		if(instructionsDB_XADDRESS.contains("__ACCOUNT ") && !(instructionsDB_XADDRESS.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Instructions saved with correct blank values in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Instructions not saved with correct blank values in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC04 (a)- Verify empty delivery address not updated for less than 15 white spaces")
	public void validateWhiteSpaceLessChars() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", "              ", "", "","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		String ADDRESS1DB = dbUtil.getValues("ADDRESS", "ADDRESS1", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		String CityDB = dbUtil.getValues("ADDRESS", "CITY", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		String StateDB = dbUtil.getValues("ADDRESS", "STATE", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		String ZipcodeDB = dbUtil.getValues("ADDRESS", "ZIPCODE", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		String FinalAddress = (ADDRESS1DB+", "+CityDB+", "+StateDB+", "+ZipcodeDB);
		
		
		if((instructionsDB_Shipinfo).contains(FinalAddress) && (instructionsDB_XADDRESS).contains(FinalAddress)) {
		
			funLibrary.Assert.assertTrue(true, "Previous delivery instructions saved in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Previous delivery instructions saved in SHIPINFO and XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Previous delivery instructions not saved in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Previous delivery instructions not saved in SHIPINFO and XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC04 (b)- Verify empty delivery address updated for more than 15 white spaces")
	public void validateWhiteSpaceMoreChars() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", "                ", "", "","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		
		if((instructionsDB_Shipinfo).contains("                ") && (instructionsDB_XADDRESS).contains("                ")) {
		
			funLibrary.Assert.assertTrue(true, "Delivery instructions with white space saved in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions with white space saved in SHIPINFO and XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions with white space not saved in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions with white space not saved in SHIPINFO and XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	
	@Test(description = "AC05 & AC14- Verify new instructions reflect in DB upto the defined characters")
	public void validateNewInstructionsOverwritten() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", "", "","1","1");
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd2")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		
		if((instructionsDB_Shipinfo).contains(FunLibrary.excelData.get("DeliveryAdd2")) && (instructionsDB_Shipinfo).contains(FunLibrary.excelData.get("Account")) && instructionsDB_Shipinfo.contains(FunLibrary.excelData.get("Instructions"))) {
		
			funLibrary.Assert.assertTrue(true, "Delivery instructions overwritten in SHIPINFO table");
			funLibrary.testLog.info("Delivery instructions overwritten in SHIPINFO table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions not overwritten in SHIPINFO table");
			funLibrary.testLog.info("Delivery instructions not overwritten in SHIPINFO table");
		}
		
		
		if((instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("DeliveryAdd2")) && (instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("Account")) && (instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("Instructions"))) {
			
			funLibrary.Assert.assertTrue(true, "Delivery instructions overwritten in XADDRESS table");
			funLibrary.testLog.info("Delivery instructions overwritten in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions not overwritten in XADDRESS table");
			funLibrary.testLog.info("Delivery instructions not overwritten in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	
	@Test(description = "AC06- Verify account and instructions reflecting correctly for blank values")
	public void validateBlankAccountInstructions() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd2")+"", "", "","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		
		if((instructionsDB_Shipinfo).contains(FunLibrary.excelData.get("DeliveryAdd2")) && (instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("DeliveryAdd2"))) {
		
			funLibrary.Assert.assertTrue(true, "Delivery instructions overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions overwritten in SHIPINFO and XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions not overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions not overwritten in SHIPINFO and XADDRESS and XADDRESS table");
		}
		
		
		if(instructionsDB_Shipinfo.contains("__ACCOUNT ") && !(instructionsDB_Shipinfo.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Account and instructions saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Account and instructions saved with correct blank values in Shipinfo table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Account and instructions not saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Account and instructions not saved with correct blank values in Shipinfo table");
		}
		
		
		if(instructionsDB_XADDRESS.contains("__ACCOUNT ") && !(instructionsDB_XADDRESS.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Account and instructions saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Account and instructions saved with correct blank values in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Account and instructions not saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Account and instructions not saved with correct blank values in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC07- Verify instructions reflecting correctly for blank values")
	public void validateBlankInstructions() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd2")+"", "", ""+FunLibrary.excelData.get("Account")+"","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		
		if((instructionsDB_Shipinfo).contains(FunLibrary.excelData.get("DeliveryAdd2")) && (instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("DeliveryAdd2"))) {
		
			funLibrary.Assert.assertTrue(true, "Delivery instructions overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions overwritten in SHIPINFO and XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions not overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions not overwritten in SHIPINFO and XADDRESS and XADDRESS table");
		}
		
		
		if(instructionsDB_Shipinfo.contains(FunLibrary.excelData.get("Account")) && !(instructionsDB_Shipinfo.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Instructions saved with correct blank values in Shipinfo table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Instructions not saved with correct blank values in Shipinfo table");
		}
		
		
		if(instructionsDB_XADDRESS.contains(FunLibrary.excelData.get("Account")) && !(instructionsDB_XADDRESS.contains("__INSTRUCTIONS "))) {
			
			funLibrary.Assert.assertTrue(true, "Instructions saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Instructions saved with correct blank values in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Instructions not saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Instructions not saved with correct blank values in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC08- Verify account reflecting correctly for blank values")
	public void validateBlankAccount() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd2")+"", ""+FunLibrary.excelData.get("Instructions")+"", "","1","1");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String instructionsDB_Shipinfo = dbUtil.getValues("SHIPINFO", "INSTRUCTIONS", "ORDERS_ID = '" + orderIdJson + "'");
		String rdAddressIdDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'rdAddressId'");
		String instructionsDB_XADDRESS = dbUtil.getValues("XADDRESS", "DLVINSTR", "ADDRESS_ID = '" + rdAddressIdDB + "'");
		
		if((instructionsDB_Shipinfo).contains(FunLibrary.excelData.get("DeliveryAdd2")) && (instructionsDB_XADDRESS).contains(FunLibrary.excelData.get("DeliveryAdd2"))) {
		
			funLibrary.Assert.assertTrue(true, "Delivery instructions overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions overwritten in SHIPINFO and XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Delivery instructions not overwritten in SHIPINFO and XADDRESS table");
			funLibrary.testLog.info("Delivery instructions not overwritten in SHIPINFO and XADDRESS and XADDRESS table");
		}
		
		
		if(!instructionsDB_Shipinfo.contains("__ACCOUNT  ") && (instructionsDB_Shipinfo.contains(FunLibrary.excelData.get("Instructions")))) {
			
			funLibrary.Assert.assertTrue(true, "Account saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Account saved with correct blank values in Shipinfo table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Account not saved with correct blank values in Shipinfo table");
			funLibrary.testLog.info("Account not saved with correct blank values in Shipinfo table");
		}
		
		
		if(!instructionsDB_XADDRESS.contains("__ACCOUNT  ") && (instructionsDB_XADDRESS.contains(FunLibrary.excelData.get("Instructions")))) {
			
			funLibrary.Assert.assertTrue(true, "Account saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Account saved with correct blank values in XADDRESS table");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Account not saved with correct blank values in XADDRESS table");
			funLibrary.testLog.info("Account not saved with correct blank values in XADDRESS table");
		}
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC09- Verify response when set RD address is missing in request body")
	public void validateSetRDAddressMissingResponse() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "AC10- Verify response when remote delivery address is missing in request body")
	public void validateRemoteDeliveryAddressMissingResponse() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "AC11- Verify response when set RD address is set to false in request body")
	public void validateSetRDAddressSetFalse() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	
	@Test(description = "AC12- Verify bagging option and unattended type values updated correctly")
	public void validateBaggingAndUnattendedValues() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd2")+"", "", ""+FunLibrary.excelData.get("Account")+"","2","2");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String orderIdJson = funLibrary.getJsonPathValue(response, "orderId");
		String OrderBaggingPreferenceDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'OrderBaggingPreference'");
		String OrderOrderUnattendedAllowedDB = dbUtil.getValues("xorderattr", "Value", "ORDERS_ID = '" + orderIdJson + "' and NAME = 'OrderUnattendedAllowed'");
		
		if((OrderBaggingPreferenceDB).contentEquals("2") && (OrderOrderUnattendedAllowedDB).contentEquals("2")) {
			
			funLibrary.Assert.assertTrue(true, "Bagging option and unattended type updated successfully");
			funLibrary.testLog.info("Bagging option and unattended type updated successfully");
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Bagging option and unattended type not updated successfully");
			funLibrary.testLog.info("Bagging option and unattended type not updated successfully");
		}
		
		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "AC13- Verify response when WCS unable to save data")
	public void validateResponseDataNotSaved() {
		
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = WCS.updateRDCustomerDetails("", "", "", authorization, userAuthorization, "0404", "1.1", ""+FunLibrary.excelData.get("DeliveryAdd1")+"", ""+FunLibrary.excelData.get("Instructions")+"", ""+FunLibrary.excelData.get("Account")+"","1","1");
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
				
	}
	

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
