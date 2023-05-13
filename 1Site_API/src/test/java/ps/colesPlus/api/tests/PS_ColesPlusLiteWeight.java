package ps.colesPlus.api.tests;

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

public class PS_ColesPlusLiteWeight extends Base_Class_API implements ITest {
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
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
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test( description = "AC01- validateColesPlusActive")
	public void validateColesPlusActive() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String type = jsonPathId.getString("subscriptions[0].membershipType");
				String nextbillingdate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String description = jsonPathId.getString("subscriptions[0].description");
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
			    String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
			    
                // Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");

				// Verify response
				
                 if(billfreq.equals("1") && type.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("nextBillingDate", nextbilldate, nextbillingdate);
				funLibrary.validate_Equals("description", desc, description);
				
                if(status.equals("1") && state.equalsIgnoreCase("Active")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);

				funLibrary.Assert.assertAll();
				}
	
	
	@Test( description = "AC02- validateCancelledSubscription")
	public void validateCancelledSubscription() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		        Response response = restLibrary.executeAPI();
		        restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String type = jsonPathId.getString("subscriptions[0].membershipType");
				String cancelDt = jsonPathId.getString("subscriptions[0].cancelDate");
				String description = jsonPathId.getString("subscriptions[0].description");
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
			    String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
			    
			    // Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				String canceldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				
				//Verify response with database

				if(billfreq.equals("1") && type.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("canceldate", canceldate, cancelDt);
				funLibrary.validate_Equals("description", desc, description);
				
				 if(status.equals("3") && state.equalsIgnoreCase("Cancelled")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);

				funLibrary.Assert.assertAll();
				}
	
	/*@Test( description = "AC03- validateTrialPeriod")
	public void validateTrialPeriod() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String description = jsonPathId.getString("subscriptions[0].description");
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				
				//Verify response with database

				if(billfreq.equals("1") && billingFrequency.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("nextBillingDate", nextbilldate, nextBillingDate);
				funLibrary.validate_Equals("description", desc, description);
				
				 if(status.equals("3") && state.equalsIgnoreCase("PendingCancel")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);

				funLibrary.Assert.assertAll();
				}
				
	@Test( description = "AC04- validateColesPlusInactive")
	public void validateColesPlusInactive() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				///Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String description = jsonPathId.getString("subscriptions[0].description");
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				
				//Verify response with database

				if(billfreq.equals("1") && billingFrequency.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("nextBillingDate", nextbilldate, nextBillingDate);
				funLibrary.validate_Equals("description", desc, description);
				
				 if(status.equals("0") && state.equalsIgnoreCase("InActive")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				

				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC05- validatePendingCancel")
	public void validatePendingCancel() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String description = jsonPathId.getString("subscriptions[0].description");
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				
				//Verify response with database

				if(billfreq.equals("1") && billingFrequency.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("nextBillingDate", nextbilldate, nextBillingDate);
				funLibrary.validate_Equals("description", desc, description);
				
				 if(status.equals("3") && state.equalsIgnoreCase("PendingCancel")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				

				funLibrary.Assert.assertAll();
				}*/
			
	@Test( description = "AC06- validateNonCloesPlus")
	public void validateNonCloesPlus() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
			
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");

				// Verify response
				
				funLibrary.validate_Contains("neverSubscribed", "true", neverSubscribed);
				

				funLibrary.Assert.assertAll();
				}
	
	

	@Test( description = "AC08- invalidMandatoryParameter")
	public void invalidMandatoryParameter() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				//Get the response
				restLibrary.getResponseBody(response);
				
				
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
				

				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC09- authenticationFails")
	public void authenticationFails() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				//Get the response
				restLibrary.getResponseBody(response);

				// Verify response
				
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC10- wrongMethodOperation")
	public void wrongMethodOperation() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				Assert.assertEquals(response.getBody().asString().equals(""), true);
				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC11- wrongEndPoint")
	public void wrongEndPoint() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				Assert.assertEquals(response.getBody().asString().equals(""), true);
				funLibrary.Assert.assertAll();
				}
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
