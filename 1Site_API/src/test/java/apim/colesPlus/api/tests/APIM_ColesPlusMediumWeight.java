package apim.colesPlus.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIM_ColesPlusMediumWeight extends Base_Class_API implements ITest {
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
				FunLibrary.excelData.get("QueryParamValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
				FunLibrary.excelData.get("QueryParamValue3"));
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
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
				FunLibrary.excelData.get("QueryParamValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
				FunLibrary.excelData.get("QueryParamValue3"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	@Test( description = "AC01- validateColesPlusActiveCard")
	public void validateColesPlusActiveCard() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String cardexpiry = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardExpiry");
				String cardtype = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardType");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "true", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("rate", cost, rate);
				
                if(status.equals("1") && state.equalsIgnoreCase("Active")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "Card", paymentname);
				funLibrary.validate_Contains("cardexpiry", "12/2025", cardexpiry);
				funLibrary.validate_Contains("cardtype", "MASTERCARD", cardtype);
				

				funLibrary.Assert.assertAll();

				}
	
	@Test( description = "AC01- validateColesPlusActivePaypal")
	public void validateColesPlusActivePaypal() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double moneySavedWithOnlineOrders = Double.parseDouble(jsonPathId.getString("subscriptions[0].moneySavedWithOnlineOrders"));
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				double timeSavedWithOnlineOrders = Double.parseDouble(jsonPathId.getString("subscriptions[0].timeSavedWithOnlineOrders"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String paypalemail = jsonPathId.getString("subscriptions[0].paymentMethod.data.paypalEmail");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = dbUtil.getValues("subscription", "PAYMENT_FREQ",
						"member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				//double moneysaved = Double.parseDouble(dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "sum(Money_Saved)",
						//"member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				//double timesaved = Double.parseDouble(dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "sum(Time_Saved)",
						//"member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "false", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				//funLibrary.validate_Equals("moneySavedWithOnlineOrders", moneysaved, moneySavedWithOnlineOrders);
				funLibrary.validate_Equals("rate", cost, rate);
				//funLibrary.validate_Equals("timeSavedWithOnlineOrders", timesaved, timeSavedWithOnlineOrders);
				
                if(status.equals("1") && state.equalsIgnoreCase("Active")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "PayPal", paymentname);
				funLibrary.validate_Contains("paypalemail", "colestest2@getnada.com", paypalemail);
				

				funLibrary.Assert.assertAll();

				}
	
	
	@Test( description = "AC02- validateCancelledSubscription")
	public void validateCancelledSubscription() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String canceldt = jsonPathId.getString("subscriptions[0].cancelDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String cardexpiry = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardExpiry");
				String cardtype = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardType");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				String canceldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				//Verify response with database

				if(billfreq.equals("1") && billingFrequency.equalsIgnoreCase("Monthly")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("canceldate", canceldate, canceldt);
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "false", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("rate", cost, rate);
				
				 if(status.equals("3") && state.equalsIgnoreCase("Cancelled")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				 funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "Card", paymentname);
				funLibrary.validate_Contains("cardexpiry", "12/2026", cardexpiry);
				funLibrary.validate_Contains("cardtype", "MASTERCARD", cardtype);
				

				funLibrary.Assert.assertAll();

				}
	
	/*@Test( description = "AC03- validateTrialPeriod")
	public void validateTrialPeriod() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String cardexpiry = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardExpiry");
				String cardtype = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardType");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "false", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("rate", cost, rate);
				
				 if(status.equals("3") && state.equalsIgnoreCase("PendingCancel")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				 funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "Card", paymentname);
				funLibrary.validate_Contains("cardexpiry", "12/2026", cardexpiry);
				funLibrary.validate_Contains("cardtype", "VISA", cardtype);
				

				funLibrary.Assert.assertAll();

				}
				
	@Test( description = "AC04- validateColesPlusInactive")
	public void validateColesPlusInactive() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
		        Response response = restLibrary.executeAPI();
		        restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String cardexpiry = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardExpiry");
				String cardtype = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardType");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='0'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "false", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("rate", cost, rate);
				
				 if(status.equals("0") && state.equalsIgnoreCase("InActive")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				 funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "Card", paymentname);
				funLibrary.validate_Contains("cardexpiry", "12/2026", cardexpiry);
				funLibrary.validate_Contains("cardtype", "VISA", cardtype);
				

				funLibrary.Assert.assertAll();

				}
	
	@Test( description = "AC05- validatePendingCancel")
	public void validatePendingCancel() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");
				String cardexpiry = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardExpiry");
				String cardtype = jsonPathId.getString("subscriptions[0].paymentMethod.data.cardType");

				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='3'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "false", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("rate", cost, rate);
				
				 if(status.equals("3") && state.equalsIgnoreCase("PendingCancel")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				 funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				funLibrary.validate_Contains("paymentname", "Card", paymentname);
				funLibrary.validate_Contains("cardexpiry", "12/2026", cardexpiry);
				funLibrary.validate_Contains("cardtype", "VISA", cardtype);
				

				funLibrary.Assert.assertAll();

				}*/
	
	/*@Test( description = "AC06- validateFetchPaymentFalse")
	public void validateFetchPaymentFalse() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
			
				//Get the required path of each field
				String billingFrequency = jsonPathId.getString("subscriptions[0].billingFrequency");
				String nextBillingDate = jsonPathId.getString("subscriptions[0].nextBillingDate");
				String hasUserRedeemedRetentionPromotion = jsonPathId.getString("subscriptions[0].hasUserRedeemedRetentionPromotion");
				String description = jsonPathId.getString("subscriptions[0].description");
				String recurringOrderId = jsonPathId.getString("subscriptions[0].recurringOrderId");
				double moneySavedWithOnlineOrders = Double.parseDouble(jsonPathId.getString("subscriptions[0].moneySavedWithOnlineOrders"));
				double rate = Double.parseDouble(jsonPathId.getString("subscriptions[0].rate"));
				double timeSavedWithOnlineOrders = Double.parseDouble(jsonPathId.getString("subscriptions[0].timeSavedWithOnlineOrders"));
				String state = jsonPathId.getString("subscriptions[0].state");
				String subscriptionId = jsonPathId.getString("subscriptions[0].subscriptionId");
				String startDate = jsonPathId.getString("subscriptions[0].startDate");
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");
				String isUserInGracePeriod = jsonPathId.getString("subscriptions[0].isUserInGracePeriod");
				String paymentname = jsonPathId.getString("subscriptions[0].paymentMethod.name");


				// Get the value from database
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String billfreq = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"PAYMENT_FREQ","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				
				String nextbilldate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", 
						"NEXTPAYMENTDATE","member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				String desc = dbUtil.getValues("subscription", "DESCRIPTION",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String orderid = dbUtil.getValues("subscription", "Orders_id",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				double moneysaved = Double.parseDouble(dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "sum(Money_Saved)",
						"member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
				double cost = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				double timesaved = Double.parseDouble(dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "sum(Time_Saved)",
						"member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
				String status = dbUtil.getValues("subscription", "STATUS",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String subscid = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", 
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'");
				String stdate = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("subscription", "STARTDATE",
						"member_id=" + FunLibrary.excelData.get("Member_Id") + " and status='1'"));
				
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
				funLibrary.validate_Contains("hasUserRedeemedRetentionPromotion", "true", hasUserRedeemedRetentionPromotion);
				funLibrary.validate_Equals("description", desc, description);
				funLibrary.validate_Equals("recurringOrderId", orderid, recurringOrderId);
				funLibrary.validate_Equals("moneySavedWithOnlineOrders", moneysaved, moneySavedWithOnlineOrders);
				funLibrary.validate_Equals("rate", cost, rate);
				
                if(status.equals("1") && state.equalsIgnoreCase("Active")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("subscriptionId", subscid, subscriptionId);
				funLibrary.validate_Equals("startDate", stdate, startDate);
				funLibrary.validate_Contains("neverSubscribed", "false", neverSubscribed);
				funLibrary.validate_Contains("isUserInGracePeriod", "false", isUserInGracePeriod);
				
                if(paymentname.equals(null)) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
					
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				funLibrary.validate_Equals("timeSavedWithOnlineOrders", timesaved, timeSavedWithOnlineOrders);
				
                          
				

				funLibrary.Assert.assertAll();

				}*/
			
	@Test( description = "AC07- validateNonCloesPlus")
	public void validateNonCloesPlus() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
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
	

	@Test( description = "AC09- invalidMandatoryParameter")
	public void invalidMandatoryParameter() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				//Get the response
				restLibrary.getResponseBody(response);
				
				
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
				

				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC10- authenticationFails")
	public void authenticationFails() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
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
	
	@Test( description = "AC11- wrongMethodOperation")
	public void wrongMethodOperation() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
				funLibrary.Assert.assertAll();
				}
	
	@Test( description = "AC12- wrongEndPoint")
	public void wrongEndPoint() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
						FunLibrary.excelData.get("QueryParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"),
						FunLibrary.excelData.get("QueryParamValue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"),
						FunLibrary.excelData.get("QueryParamValue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				// Verify response
				
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
				funLibrary.Assert.assertAll();
				}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
