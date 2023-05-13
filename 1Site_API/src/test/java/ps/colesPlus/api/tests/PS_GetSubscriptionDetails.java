package ps.colesPlus.api.tests;



import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
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
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class PS_GetSubscriptionDetails extends Base_Class_API implements ITest{

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
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
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	

	
	
	
	
	/*
	@Test(description = "AC01 (A)- Verify account details for colus plus member with payment method through saved card")
	public void validateAccountDetailsColesPlusMemberCard() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		JsonPath jsonPath = response.jsonPath();
				
		String subscriptionId_Db = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String subscriptionId_Json = jsonPath.getString("subscriptions[0].subscriptionId");		
		funLibrary.validate_Equals("subscriptionId", subscriptionId_Db, subscriptionId_Json);
		
		
		String description_Db = dbUtil.getValues("subscription", "DESCRIPTION", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String description_Json = jsonPath.getString("subscriptions[0].description");		
		funLibrary.validate_Equals("description", description_Db, description_Json);
		
		
		double rate_Db = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		double rate_Json = Double.parseDouble(jsonPath.getString("subscriptions[0].rate"));	
		Assert.assertEquals("Compare rate", rate_Db, rate_Json, 1);
		
		
		String recurringOrderId_Db = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String recurringOrderId_Json = jsonPath.getString("subscriptions[0].recurringOrderId");		
		funLibrary.validate_Equals("recurringOrderId", recurringOrderId_Db, recurringOrderId_Json);
		
		
		String state_Db = dbUtil.getValues("subscription", "STATUS", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String state_Json = jsonPath.getString("subscriptions[0].state");
		
		
		if(state_Db.equals("1") && state_Json.equalsIgnoreCase("Active")) {
			
			funLibrary.Assert.assertTrue(true, "Status matched");
			funLibrary.testLog.info("Status matched");
			
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Status mismatched");
			funLibrary.testLog.info("Status mismatched");
		}
		
		
		
		
		String startDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		String startDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].startDate"));		
		funLibrary.validate_Equals("startDate", startDate_Db, startDate_Json);
		
		
		String nextBillingDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "NEXTPAYMENTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		String nextBillingDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].nextBillingDate"));		
		funLibrary.validate_Equals("nextBillingDate", nextBillingDate_Db, nextBillingDate_Json);
		
		
		String catentryId_Db = dbUtil.getValues("orderitems", "CATENTRY_ID", "ORDERS_ID='" + recurringOrderId_Db + "'");
		String billingFrequency_Db = dbUtil.getValues("catentdesc", "NAME", "CATENTRY_ID='" + catentryId_Db + "'");
		String billingFrequency_Json = jsonPath.getString("subscriptions[0].billingFrequency");
		
		
		if(billingFrequency_Db.trim().equalsIgnoreCase("Plus") && billingFrequency_Json.equalsIgnoreCase("Monthly")) {
			
			funLibrary.Assert.assertTrue(true, "Billing frequency matched");
			funLibrary.testLog.info("Billing frequency matched");
			
			
		}
		
		else {
			
			funLibrary.Assert.assertTrue(false, "Billing frequency mismatched");
			funLibrary.testLog.info("Billing frequency mismatched");
		}
		
		funLibrary.Assert.assertAll();
		
		
	
		
		
		
		
		
	}
	
	
	@Test(description = "AC01 (B)- Verify account details for colus plus member with payment method through Paypal")
	public void validateAccountDetailsColesPlusMemberPaypal() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		 
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		JsonPath jsonPath = response.jsonPath();
				
		String subscriptionId_Db = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String subscriptionId_Json = jsonPath.getString("subscriptions[0].subscriptionId");		
		funLibrary.validate_Equals("subscriptionId", subscriptionId_Db, subscriptionId_Json);
		
		
		String description_Db = dbUtil.getValues("subscription", "DESCRIPTION", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String description_Json = jsonPath.getString("subscriptions[0].description");		
		funLibrary.validate_Equals("description", description_Db, description_Json);
		
		
		double rate_Db = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		double rate_Json = Double.parseDouble(jsonPath.getString("subscriptions[0].rate"));	
		Assert.assertEquals("Compare rate", rate_Db, rate_Json, 1);
		
		
		String recurringOrderId_Db = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String recurringOrderId_Json = jsonPath.getString("subscriptions[0].recurringOrderId");		
		funLibrary.validate_Equals("recurringOrderId", recurringOrderId_Db, recurringOrderId_Json);
		
		
		String state_Db = dbUtil.getValues("subscription", "STATUS", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String state_Json = jsonPath.getString("subscriptions[0].state");
		
		
		if(state_Db.equals("1") && state_Json.equalsIgnoreCase("Active")) {
			
			funLibrary.Assert.assertTrue(true, "Status matched");
			funLibrary.testLog.info("Status matched");
			
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Status mismatched");
			funLibrary.testLog.info("Status mismatched");
		}
		
		
		
		
		String startDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		String startDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].startDate"));		
		funLibrary.validate_Equals("startDate", startDate_Db, startDate_Json);
		
		
		String nextBillingDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "NEXTPAYMENTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		String nextBillingDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].nextBillingDate"));		
		funLibrary.validate_Equals("nextBillingDate", nextBillingDate_Db, nextBillingDate_Json);
		
		
		String catentryId_Db = dbUtil.getValues("orderitems", "CATENTRY_ID", "ORDERS_ID='" + recurringOrderId_Db + "'");
		String billingFrequency_Db = dbUtil.getValues("catentdesc", "NAME", "CATENTRY_ID='" + catentryId_Db + "'");
		String billingFrequency_Json = jsonPath.getString("subscriptions[0].billingFrequency");
		
		
		if(billingFrequency_Db.trim().equalsIgnoreCase("Plus") && billingFrequency_Json.equalsIgnoreCase("Monthly")) {
			
			funLibrary.Assert.assertTrue(true, "Billing frequency matched");
			funLibrary.testLog.info("Billing frequency matched");
			
			
		}
		
		else {
			
			funLibrary.Assert.assertTrue(false, "Billing frequency mismatched");
			funLibrary.testLog.info("Billing frequency mismatched");
		}
		
		funLibrary.Assert.assertAll();
		
	
		
		
	}
	*/
	
	
	@Test(description = "AC02- Verify account details for non colus plus member")
	public void validateAccountDetailsNonColesPlusMember() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		Assert.assertEquals(response.getBody().asString().equals(FunLibrary.excelData.get("Response")), true);
		
		
	}
	
	
	
	/*
	@Test(description = "AC03- Verify version details")
    public void validateVersionDetails() {
           
           RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
           restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
           restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
           restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
           Response response = restLibrary.executeAPI();
           restLibrary.getResponseBody(response);
           funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
           DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
           JsonPath jsonPath = response.jsonPath();
                        
           String subscriptionId_Db = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
           String subscriptionId_Json = jsonPath.getString("subscriptions[0].subscriptionId");              
           funLibrary.validate_Equals("subscriptionId", subscriptionId_Db, subscriptionId_Json);
           
           
           String description_Db = dbUtil.getValues("subscription", "DESCRIPTION", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
           String description_Json = jsonPath.getString("subscriptions[0].description");         
           funLibrary.validate_Equals("description", description_Db, description_Json);
           
           
           double rate_Db = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
           double rate_Json = Double.parseDouble(jsonPath.getString("subscriptions[0].rate"));       
           Assert.assertEquals("Compare rate", rate_Db, rate_Json, 1);
           
           
           String recurringOrderId_Db = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
           String recurringOrderId_Json = jsonPath.getString("subscriptions[0].recurringOrderId");          
           funLibrary.validate_Equals("recurringOrderId", recurringOrderId_Db, recurringOrderId_Json);
           
           
           String state_Db = dbUtil.getValues("subscription", "STATUS", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
           String state_Json = jsonPath.getString("subscriptions[0].state");
           
           
           if(state_Db.equals("1") && state_Json.equalsIgnoreCase("Active")) {
                  
                  funLibrary.Assert.assertTrue(true, "Status matched");
                  funLibrary.testLog.info("Status matched");
                  
           }
           
           else {
                  funLibrary.Assert.assertTrue(false, "Status mismatched");
                  funLibrary.testLog.info("Status mismatched");
           }
           
           
           
           
           String startDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
           String startDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].startDate"));          
           funLibrary.validate_Equals("startDate", startDate_Db, startDate_Json);
           
           
           String nextBillingDate_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "NEXTPAYMENTDATE", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
           String nextBillingDate_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].nextBillingDate"));              
           funLibrary.validate_Equals("nextBillingDate", nextBillingDate_Db, nextBillingDate_Json);
           
           
           String catentryId_Db = dbUtil.getValues("orderitems", "CATENTRY_ID", "ORDERS_ID='" + recurringOrderId_Db + "'");
           String billingFrequency_Db = dbUtil.getValues("catentdesc", "NAME", "CATENTRY_ID='" + catentryId_Db + "'");
           String billingFrequency_Json = jsonPath.getString("subscriptions[0].billingFrequency");
           
           
           if(billingFrequency_Db.trim().equalsIgnoreCase("Plus") && billingFrequency_Json.equalsIgnoreCase("Monthly")) {
                  
                  funLibrary.Assert.assertTrue(true, "Billing frequency matched");
                  funLibrary.testLog.info("Billing frequency matched");
                  
                  
           }
           
           else {
                  
                  funLibrary.Assert.assertTrue(false, "Billing frequency mismatched");
                  funLibrary.testLog.info("Billing frequency mismatched");
           }
           
           funLibrary.Assert.assertAll();
           
          
           
    }
    */


	
	

	

	@Override
	public String getTestName() {
		return testName.get();

	}

}
