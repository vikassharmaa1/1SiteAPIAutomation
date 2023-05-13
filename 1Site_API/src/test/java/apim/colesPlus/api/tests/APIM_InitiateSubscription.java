package apim.colesPlus.api.tests;



import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import org.junit.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class APIM_InitiateSubscription extends Base_Class_API implements ITest{

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		
		if(FunLibrary.excelData.get("CardType_SavedCard").equalsIgnoreCase("SavedCard")) {
			
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
			restLibrary.addBody(FunLibrary.excelData.get("RequestBodySavedCard"));
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
		
		
		if(FunLibrary.excelData.get("CardType_Card").equalsIgnoreCase("Card")) {
			
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
			restLibrary.addBody(FunLibrary.excelData.get("RequestBodyCard"));
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
				
	}
	
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		String identifier = APILibrary.getMPGSCardToken("5111111111111118", "12", "25", "100");
		RestAssured.reset();
		restLibrary.addBody(APILibrary.initiateSubscriptionNewCard_PS(FunLibrary.excelData.get("billingFrequency"), FunLibrary.excelData.get("CardType_Card"), identifier));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	
	
	@Test(description = "AC02- Verify initiate subscription details based on the value set for 3DS enabled")
	public void validateInitiateSubDetails() {
		
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String ThreeDS_Value = dbUtil.getValues("x_coles_config", "VALUE", "name= 'payments.mpgs.threeds.enabled.for'");
		
		switch (ThreeDS_Value) {
		
		case "new_cards":
			
				if(ThreeDS_Value.contentEquals("new_cards") && FunLibrary.excelData.get("CardType_Card").equalsIgnoreCase("Card")) {
					
					RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
					String identifier = APILibrary.getMPGSCardToken("5123450000000008", "12", "25", "100");
					RestAssured.reset();
					restLibrary.addBody(APILibrary.initiateSubscriptionNewCard_PS(FunLibrary.excelData.get("billingFrequency"), FunLibrary.excelData.get("CardType_Card"), identifier));
					Response response = restLibrary.executeAPI();
					restLibrary.getResponseBody(response);
					funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));

					
					JsonPath jsonPath = response.jsonPath();
					
					String recurringOrderId = jsonPath.getString("recurringOrderId");
					String ORDERS_ID = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
					funLibrary.validate_Equals("recurringOrderId", ORDERS_ID, recurringOrderId);
					
					
					String STATUS_DB = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
					if(STATUS_DB.equals("J")) {
						
						funLibrary.Assert.assertTrue(true, "Status matched");
						funLibrary.testLog.info("Status matched");
						
					}
					
					else {
						funLibrary.Assert.assertTrue(false, "Status mismatched");
						funLibrary.testLog.info("Status mismatched");
					}
					
					funLibrary.Assert.assertAll();
				}
				
				else {
					
					funLibrary.Assert.assertTrue(false, "Data Mismatch");
				}
				
				
		case "all_cards":
				
				if(ThreeDS_Value.contentEquals("all_cards") && FunLibrary.excelData.get("CardType_Card").equalsIgnoreCase("Card")) {
					
					RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
					String identifier = APILibrary.getMPGSCardToken("5123450000000008", "12", "25", "100");
					RestAssured.reset();
					restLibrary.addBody(APILibrary.initiateSubscriptionNewCard_PS(FunLibrary.excelData.get("billingFrequency"), FunLibrary.excelData.get("CardType_Card"), identifier));
					Response response = restLibrary.executeAPI();
					restLibrary.getResponseBody(response);
					funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));

					
					JsonPath jsonPath = response.jsonPath();
					
					String recurringOrderId = jsonPath.getString("recurringOrderId");
					String ORDERS_ID = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
					funLibrary.validate_Equals("recurringOrderId", ORDERS_ID, recurringOrderId);
					
					
					String STATUS_DB = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
					if(STATUS_DB.equals("J")) {
						
						funLibrary.Assert.assertTrue(true, "Status matched");
						funLibrary.testLog.info("Status matched");
						
					}
					
					else {
						funLibrary.Assert.assertTrue(false, "Status mismatched");
						funLibrary.testLog.info("Status mismatched");
					}
					
					funLibrary.Assert.assertAll();
				}
				
				else {
					
					funLibrary.Assert.assertTrue(false, "Data Mismatch");
				}
				
		case "none":			
			
				if(ThreeDS_Value.contentEquals("none") && FunLibrary.excelData.get("CardType_Card").equalsIgnoreCase("Card")) {
					
					RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
					restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
					String identifier = APILibrary.getMPGSCardToken("5123450000000008", "12", "25", "100");
					RestAssured.reset();
					restLibrary.addBody(APILibrary.initiateSubscriptionNewCard_PS(FunLibrary.excelData.get("billingFrequency"), FunLibrary.excelData.get("CardType_Card"), identifier));
					Response response = restLibrary.executeAPI();
					restLibrary.getResponseBody(response);
					funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

					
					JsonPath jsonPath = response.jsonPath();
					
					String ORDERS_ID = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("MemberId") + "'");
					String recurringOrderId = jsonPath.getString("recurringOrderId");
					funLibrary.validate_Equals("recurringOrderId", ORDERS_ID, recurringOrderId);
					
					
					String STATUS_DB = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
					if(STATUS_DB.equals("I")) {
						
						funLibrary.Assert.assertTrue(true, "Status matched");
						funLibrary.testLog.info("Status matched");
						
					}
					
					else {
						funLibrary.Assert.assertTrue(false, "Status mismatched");
						funLibrary.testLog.info("Status mismatched");
					}
					
					funLibrary.Assert.assertAll();
				}
				
				else {
					
					funLibrary.Assert.assertTrue(false, "Data Mismatch");
				}
				
		default:
			break;
			
		}
		
		
	}
	
	

	@Override
	public String getTestName() {
		return testName.get();

	}

}
