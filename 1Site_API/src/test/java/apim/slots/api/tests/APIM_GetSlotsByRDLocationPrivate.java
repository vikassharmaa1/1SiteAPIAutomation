package apim.slots.api.tests;

import java.sql.SQLException;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.rest.api.PS;
import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.Reporting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIM_GetSlotsByRDLocationPrivate extends Base_Class_API implements ITest{
	
	static String jsonId = "";
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
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
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	@Test(description = "AC01 and AC06- Verify remoteDeliveryPartner related attributes")
	public void validateremoteDeliveryPartnerDetails() {
		String date = null;	
		date = funLibrary.getDateAddDays(1);
		RestLibrary restLibrary = new RestLibrary();
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header3_Value");
		String storeId = FunLibrary.excelData.get("StoreId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		String Subscription_Key= FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = Payload.getSlotByRDLocationDate_Private_APIM("", "", "", authorization, userAuthorization, Subscription_Key,storeId, collectionPointId, date, date, "ALL");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		JsonPath jsonPath = response.jsonPath() ;		        
		//verifying remoteDeliveryPartner details in response
		String rdPartnerId = jsonPath.getString("remoteDeliveryPartner.rdPartnerId");
        funLibrary.validate_Equals("RD Partner ID", collectionPointId,rdPartnerId);
        String rdPartnerName = jsonPath.getString("remoteDeliveryPartner.rdPartnerName");
        String stlocds_id=dbUtil.getValues("stloc", "STLOC_ID","IDENTIFIER ='"+collectionPointId+ "'");
        String rdPartnerName_DB = dbUtil.getValues("STLOCDS", "NAME","STLOCDS_ID ="+stlocds_id);
        funLibrary.validate_Equals("rdPartnerName", rdPartnerName_DB.replaceAll("via", "").trim(),rdPartnerName);
        String rdPartnerPhone = jsonPath.getString("remoteDeliveryPartner.rdPartnerPhone");
        String rdPartnerPhone_db = dbUtil.getValues("stloc", "PHONE","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerPhone", rdPartnerPhone_db,rdPartnerPhone);
        String rdPartnerPostcode = jsonPath.getString("remoteDeliveryPartner.rdPartnerPostcode");
        String rdPartnerPostcode_db = dbUtil.getValues("stloc", "ZIPCODE","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerPostcode", rdPartnerPostcode_db,rdPartnerPostcode);
        String rdPartnerAddressLine = jsonPath.getString("remoteDeliveryPartner.rdPartnerAddressLine");
        String rdPartnerAddressLine_db =dbUtil.getValues("stloc", "ADDRESS1","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerAddressLine", rdPartnerAddressLine_db,rdPartnerAddressLine);
        String rdPartnerSuburb = jsonPath.getString("remoteDeliveryPartner.rdPartnerSuburb");
        String rdPartnerSuburb_db = dbUtil.getValues("stloc", "CITY","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerSuburb", rdPartnerSuburb_db,rdPartnerSuburb);
        String rdPartnerState = jsonPath.getString("remoteDeliveryPartner.rdPartnerState");
        String rdPartnerState_db = dbUtil.getValues("stloc", "STATE","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerState", rdPartnerState_db,rdPartnerState);
        String rdPartnerCountry = jsonPath.getString("remoteDeliveryPartner.rdPartnerCountry");
        String rdPartnerCountry_db = dbUtil.getValues("stloc", "COUNTRY","IDENTIFIER ='"+collectionPointId+"'");
        funLibrary.validate_Equals("rdPartnerCountry", rdPartnerCountry_db,rdPartnerCountry);
        dbUtil.closeDBConnection();
        funLibrary.Assert.assertAll();
}
	@Test(description = "AC02- Verify validateSlotBaggingBitFlag should have value as 0")
	public void validateSlotBaggingBitFlag() {
	String date = null;
	date = funLibrary.getDateAddDays(1);
		RestLibrary restLibrary = new RestLibrary();
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header3_Value");
		String storeId = FunLibrary.excelData.get("StoreId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		String Subscription_Key= FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = Payload.getSlotByRDLocationDate_Private_APIM("", "", "", authorization, userAuthorization, Subscription_Key,storeId, collectionPointId, date, date, "ALL");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		//verifying slotBagging details in response
		 // funLibrary.validateJsonStructure(response, "getSlotForRD/getSLotForRD");
			//verifying slotBagging as 0
		  JsonPath jsonPath = response.jsonPath() ;
		  DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		  String baggingOption = dbUtil.getValues("delcollection_point", "BAGGING_OPTIONS","COLLECTIONPOINT_NAME='0404RD0002'");
			String slotBaggingBitFlag = jsonPath.getString("slotBagging.slotBaggingBitFlag") ;
			funLibrary.validate_Equals("slotBaggingBitFlag",baggingOption, slotBaggingBitFlag);
			funLibrary.validate_Equals("slotBaggingBitFlag","0", slotBaggingBitFlag);
			funLibrary.Assert.assertAll();
	}
	@Test(description = "AC02- Verify unattenedType should have value as 0")
	public void validateunattendedType() throws SQLException {
	String date = null;
	date = funLibrary.getDateAddDays(1);
		RestLibrary restLibrary = new RestLibrary();
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header3_Value");
		String storeId = FunLibrary.excelData.get("StoreId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		String Subscription_Key= FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		response = Payload.getSlotByRDLocationDate_Private_APIM("", "", "", authorization, userAuthorization, Subscription_Key,storeId, collectionPointId, date, date, "ALL");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath() ;
		//getting the count of slots 		
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		int count =dbUtil_DM.getRecordCount("delwindow",  "status = 'OPEN' and " + "service_type = 'RD' and " + 
	     		   "window_start_time like '%"+date+"%' and " + 
	     		   "window_end_time like '%"+date+"%'and COLLECTIONPOINT_NAME = '0404RD0002'");
		System.out.println(count);
		//verifying the unattendedType attribute as 0
		for(int i=0; i<count; i++) {
		String unattendedType = jsonPath.getString("slots.RD["+i+"].unattendedType");
		funLibrary.validate_Equals("unattendedType","0", unattendedType);
		funLibrary.Assert.assertAll();
		}
	}
	@Test(description = "AC03- Verify there is not Slots available for selected 3pl")
	public void validateNoSlotsAvailableForSelected3PL() throws SQLException {
		String date = null;
		//date = funLibrary.getDateAddDays(4);
		RestLibrary restLibrary = new RestLibrary();
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header3_Value");
		String storeId = FunLibrary.excelData.get("StoreId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		String Subscription_Key= FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		int i,count=0;
		int maxNoOfDays=6;
		for (i = 1; i <= maxNoOfDays;) {
			date = funLibrary.getDateAddDays(i);
			System.out.println("day" + i);
			count = dbUtil_DM.getRecordCount("delwindow",
					"status != 'CLOSED' and " + "service_type = 'RD' and " + "window_start_time like '%" + date
							+ "%' and " + "window_end_time like '%" + date
							+ "%'and COLLECTIONPOINT_NAME = '0418RD0006'");
			if (count == 0) {
				response = Payload.getSlotByRDLocationDate_Private_APIM("", "", "", authorization, userAuthorization,
						Subscription_Key, storeId, collectionPointId, date, date, "ALL");
				restLibrary.getResponseBody(response);
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validate_Equals("numberOfAvailableSlot", 0, count);
				break;
			} else {
				i++;
			}
		}
		if (i > maxNoOfDays) {
			funLibrary.validate_Equals("numberOfAvailableSlot", 0, count);
			testLog.info("The slots Are available for next Given Days");
		}
		dbUtil_DM.closeDBConnection();
		funLibrary.Assert.assertAll();		
	}
	@Override
	public String getTestName() {
		return testName.get();

	}

}
