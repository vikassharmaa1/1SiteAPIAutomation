package wcs.colesPlus.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_ColesPlusFullWeight extends Base_Class_API implements ITest {
	
	/*@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),
				FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);

		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'",
					FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}*/
	
	

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),FunLibrary.excelData.get("PathParamValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"), FunLibrary.excelData.get("QueryParamvalue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"), FunLibrary.excelData.get("QueryParamvalue3"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	
	
	@Test( description = "AC01- Validate coles plus active user details like plan, profile and payment")
	public void validateDetailsColesPlusActiveUser() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),FunLibrary.excelData.get("PathParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"), FunLibrary.excelData.get("QueryParamvalue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"), FunLibrary.excelData.get("QueryParamvalue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the required path of each field
				//subscription
				String subscriptionIdJson = funLibrary.getJsonPathValue(response, "subscriptions[0].subscriptionId");
				String descriptionJson = funLibrary.getJsonPathValue(response, "subscriptions[0].description");
				String rateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].rate");
				String moneySavedWithOnlineOrdersJson = funLibrary.getJsonPathValue(response, "subscriptions[0].moneySavedWithOnlineOrders");
				String timeSavedWithOnlineOrdersJson = funLibrary.getJsonPathValue(response, "subscriptions[0].timeSavedWithOnlineOrders");
				String billingFrequencyJson = funLibrary.getJsonPathValue(response, "subscriptions[0].billingFrequency");
				String recurringOrderIdJson = funLibrary.getJsonPathValue(response, "subscriptions[0].recurringOrderId");
				String isUserInGracePeriodJson = funLibrary.getJsonPathValue(response, "subscriptions[0].isUserInGracePeriod");
				String hasUserRedeemedRetentionPromotionJson = funLibrary.getJsonPathValue(response, "subscriptions[0].hasUserRedeemedRetentionPromotion");
				String stateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].state");
				String startDateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].startDate");
				String nextBillingDateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].nextBillingDate");
				String neverSubscribedJson = funLibrary.getJsonPathValue(response, "subscriptions[0].neverSubscribed");
				String paymentMethodnameJson = funLibrary.getJsonPathValue(response, "subscriptions[0].paymentMethod.name");
				//plan
				String plansDescriptionJson = funLibrary.getJsonPathValue(response, "plans[0].description");
				String plansRateJson = funLibrary.getJsonPathValue(response, "plans[0].rate");
				String plansMembershipTypeJson = funLibrary.getJsonPathValue(response, "plans[0].membershipType");//billingFrequencyDB
				String plansSKUJson = funLibrary.getJsonPathValue(response, "plans[0].sku");
				//profile
				String profilePhoneAvailableJson = funLibrary.getJsonPathValue(response, "profile.phoneAvailable"); 
				String profileDobAvailableJson = funLibrary.getJsonPathValue(response, "profile.dobAvailable");
				String profileCpXAuxTrialPhoneAppliedJson = funLibrary.getJsonPathValue(response, "profile.cpXAuxTrialPhoneApplied");
				String profileFlyBuysLinkedToAccountJson = funLibrary.getJsonPathValue(response, "profile.flyBuysLinkedToAccount");
				String profileBusinessUserJson = funLibrary.getJsonPathValue(response, "profile.businessUser");
				

				// Get the response from db
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				//need to provide user id in sheet
				String subscriptionIdDB = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String descriptionDB = dbUtil.getValues("subscription", "DESCRIPTION", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String rateDB = dbUtil.getValues("subscription", "TOTALCOST", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String moneySavedWithOnlineOrdersDB = dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "MONEY_SAVED", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String timeSavedWithOnlineOrdersDB = dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "TIME_SAVED", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String billingFrequencyDB = dbUtil.getValues("subscription", "PAYMENTFREQ_UOM", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String recurringOrderIdDB = dbUtil.getValues("subscription", "ORDERS_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String isUserInGracePeriodDB = dbUtil.getValues("wcsowner.mbrgrpmbr", "MBRGRP_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and mbrgrp_id in (select mbrgrp_id from mbrgrp where mbrgrpname='SubscriptionPaymentFailedGracePeriod')");
				String hasUserRedeemedRetentionPromotionDB = dbUtil.getValues("wcsowner.mbrgrpmbr", "MBRGRP_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and mbrgrp_id in (select mbrgrp_id from mbrgrp where mbrgrpname='RetainColesPlusMembershipGroup')");
				String stateDB = dbUtil.getValues("subscription", "STATUS", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String startDate = dbUtil.getValues("subscription", "STARTDATE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String startDateDB= funLibrary.storeDateBeforeWhitespace(startDate);
				String nextBillingDate = dbUtil.getValues("subscription", "NEXTPAYMENTDATE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String nextBillingDateDB= funLibrary.storeDateBeforeWhitespace(nextBillingDate);
				String neverSubscribedDB = dbUtil.getValues("wcsowner.edppayinst", "EDPPAYINST_ID", "EDPORDER_ID = '" + FunLibrary.excelData.get("EDPORDER_ID") + "'");
				String paymentMethodnameDB = dbUtil.getValues("wcsowner.edppayinst", "PAYMENTMETHOD", "EDPORDER_ID = '" + FunLibrary.excelData.get("EDPORDER_ID") + "'");
				
				String plansSKUDB = dbUtil.getValues("wcsowner.CATENTRY", "PARTNUMBER", "MFNAME = '" + FunLibrary.excelData.get("MFNAME") + "'");//MFNAME=Coles Plus $19
				
				String profilePhoneAvailableDB = dbUtil.getValues("wcsowner.address", "PHONE1", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String profileDobDB = dbUtil.getValues("wcsowner.userdemo", "DATEOFBIRTH", "users_id = '" + FunLibrary.excelData.get("userId") + "'");
				String profileCpXAuxTrialPhoneAppliedDB = dbUtil.getValues("wcsowner.x_aux_plandata", "TYPE", "ATTRIBUTEVALUE = '" + FunLibrary.excelData.get("ATTRIBUTEVALUE") + "' and TYPE = 'COLESPLUSTRIAL' and ATTRIBUTENAME = 'phone'");
				String profileFlyBuysLinkedToAccountDB = dbUtil.getValues("wcsowner.MBRATTRVAL", "STRINGVALUE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and mbrattr_id = '610'");
				String profileBusinessUserDB = dbUtil.getValues("wcsowner.users", "dn", "USERS_ID = '" + FunLibrary.excelData.get("userId") + "'");
				
				
				// Subscription
				funLibrary.validate_Equals("subscriptionId", subscriptionIdJson, subscriptionIdDB);
				funLibrary.validate_Equals("description", descriptionJson, descriptionDB);
				funLibrary.validate_Contains("rate", rateDB, rateJson);
				
				
				if (moneySavedWithOnlineOrdersDB.contains("No Record Found") && moneySavedWithOnlineOrdersJson.contains("0.0")) {
					
					funLibrary.Assert.assertTrue(true, "No online money saved, behaviour as expected");
					funLibrary.testLog.info("No online money saved, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Behaviour unexpected for money saved");
					funLibrary.testLog.info("Behaviour unexpected for money saved");
				}
				
				
				if (timeSavedWithOnlineOrdersJson.contains("0.0") && timeSavedWithOnlineOrdersDB.contains("No Record Found")) {
					
					funLibrary.Assert.assertTrue(true, "No online time saved, behaviour as expected");
					funLibrary.testLog.info("No online time saved, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Behaviour unexpected for time saved");
					funLibrary.testLog.info("Behaviour unexpected for time saved");
				}
				
				
				if (billingFrequencyJson.contentEquals("Monthly") && billingFrequencyDB.contains("MON")) {
					
					funLibrary.Assert.assertTrue(true, "Billing Frequency matched");
					funLibrary.testLog.info("Billing Frequency matched");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Billing Frequency not matched");
					funLibrary.testLog.info("Billing Frequency not matched");
				}
				
				
				funLibrary.validate_Equals("recurringOrderId", recurringOrderIdJson, recurringOrderIdDB);
				
				
				if (isUserInGracePeriodDB.contains("No Record Found") && isUserInGracePeriodJson.contains("false")) {
					
					funLibrary.Assert.assertTrue(true, "User not in grace period, behaviour as expected");
					funLibrary.testLog.info("User not in grace period, behaviour as expected");
				}
				
				else if (isUserInGracePeriodDB !=null && isUserInGracePeriodJson.contains("true")){
					funLibrary.Assert.assertTrue(true, "User in grace period, behaviour as expected");
					funLibrary.testLog.info("User in grace period, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Grace period data mismatch");
					funLibrary.testLog.info("Grace period data mismatch");
				}
				
				
				if (hasUserRedeemedRetentionPromotionDB.contains("No Record Found") && hasUserRedeemedRetentionPromotionJson.contains("false")) {
					
					funLibrary.Assert.assertTrue(true, "User not redeemed promotion, behaviour as expected");
					funLibrary.testLog.info("User not redeemed promotion, behaviour as expected");
				}
				
				else if (hasUserRedeemedRetentionPromotionDB !=null && hasUserRedeemedRetentionPromotionJson.contains("true")){
					funLibrary.Assert.assertTrue(true, "User redeemed promotion, behaviour as expected");
					funLibrary.testLog.info("User redeemed promotion, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Redemption promo data mismatch");
					funLibrary.testLog.info("edemption promo data mismatch");
				}
				 
				
				if (stateJson.contentEquals("Active") && stateDB.contentEquals("1")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				
				funLibrary.validate_Equals("startDate", startDateJson, startDateDB);
				funLibrary.validate_Equals("nextBillingDate", nextBillingDateJson, nextBillingDateDB);
				
				if (neverSubscribedJson.contentEquals("false") && neverSubscribedDB != null) {
					
					funLibrary.Assert.assertTrue(true, "User has subscribed for coles plus, behaviour as expected");
					funLibrary.testLog.info("User has subscribed for coles plus, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "User has never subscribed for coles plus, behaviour unexpected");
					funLibrary.testLog.info("User has never subscribed for coles plus, behaviour unexpected");
				}
				
				if (paymentMethodnameJson !=null && paymentMethodnameDB.contentEquals("MPGSSavedCard")) {
					
					funLibrary.Assert.assertTrue(true, "User has valid payment method");
					funLibrary.testLog.info("User has valid payment method");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "User has no payment method");
					funLibrary.testLog.info("User has no payment method");
				}
				
				
				
				//plan
				funLibrary.validate_Equals("description", plansDescriptionJson, descriptionDB);
				funLibrary.validate_Contains("rate", rateDB, plansRateJson);
				
				if (plansMembershipTypeJson.contentEquals("Monthly") && billingFrequencyDB.contains("MON")) {
					
					funLibrary.Assert.assertTrue(true, "Membership type matched");
					funLibrary.testLog.info("Membership type matched");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Membership type not matched");
					funLibrary.testLog.info("Membership type not matched");
				}
				
				funLibrary.validate_Equals("SKU", plansSKUJson, plansSKUDB);
				
				//profile
				
				if (profilePhoneAvailableJson.contains("true") && profilePhoneAvailableDB != null) {
					
					funLibrary.Assert.assertTrue(true, "Profile phone available in the profile, expected behaviour");
					funLibrary.testLog.info("Profile phone available in the profile, expected behaviour");
				}
				
				else if (profilePhoneAvailableJson.contains("false") && profilePhoneAvailableDB == null){
					funLibrary.Assert.assertTrue(true, "Profile phone not available in the profile, expected behaviour");
					funLibrary.testLog.info("Profile phone not available in the profile, expected behaviour");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Profile phone data mismatch");
					funLibrary.testLog.info("Profile phone data mismatch");
				}
				
				
				if (profileDobAvailableJson.contains("true") && profileDobDB != null) {
					
					funLibrary.Assert.assertTrue(true, "DOB available in the profile, expected behaviour");
					funLibrary.testLog.info("DOB available in the profile, expected behaviour");
				}
				
				else if (profileDobAvailableJson.contains("false") && profileDobDB == null){
					funLibrary.Assert.assertTrue(true, "DOB not available in the profile, expected behaviour");
					funLibrary.testLog.info("DOB not available in the profile, expected behaviour");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Profile DOB data mismatch");
					funLibrary.testLog.info("Profile DOB data mismatch");
				}
				
				
				if (profileCpXAuxTrialPhoneAppliedJson.contains("true") && profileCpXAuxTrialPhoneAppliedDB.contains("COLESPLUSTRIAL")) {
					
					funLibrary.Assert.assertTrue(true, "Phone number used to avail trial period, expected behaviour");
					funLibrary.testLog.info("Phone number used to avail trial period, expected behaviour");
				}
				
				else if (profileCpXAuxTrialPhoneAppliedJson.contains("false") && profileCpXAuxTrialPhoneAppliedDB == null){
					funLibrary.Assert.assertTrue(true, "Phone number not used to avail trial period, expected behaviour");
					funLibrary.testLog.info("Phone number not used to avail trial period, expected behaviour");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Profile phone number data mismatch");
					funLibrary.testLog.info("Profile phone number data mismatch");
				}
				
				
				if (profileFlyBuysLinkedToAccountJson.contains("true") && profileFlyBuysLinkedToAccountDB !=null) {
					
					funLibrary.Assert.assertTrue(true, "Flybuys linked to the profile, expected behaviour");
					funLibrary.testLog.info("Flybuys linked to the profile, expected behaviour");
				}
				
				else if (profileFlyBuysLinkedToAccountJson.contains("false") && profileFlyBuysLinkedToAccountDB == null){
					funLibrary.Assert.assertTrue(true, "Flybuys not linked to the profile, expected behaviour");
					funLibrary.testLog.info("Flybuys not linked to the profile, expected behaviour");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Profile flybuys data mismatch");
					funLibrary.testLog.info("Profile flybuys data mismatch");
				}
				
				
				
				if (profileBusinessUserJson.contains("false") && profileBusinessUserDB.contains("o=cgl_customers,o=cgl")) {
					
					funLibrary.Assert.assertTrue(true, "Profile is of a non business user, expected behaviour");
					funLibrary.testLog.info("Profile is of a non business user, expected behaviour");
				}
				
				else if (profileBusinessUserJson.contains("true") && profileBusinessUserDB.contains("o=coles b2b buyer organization,o=cgl")){
					funLibrary.Assert.assertTrue(true, "Profile is of a business user, expected behaviour");
					funLibrary.testLog.info("Profile is of a business user, expected behaviour");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Business profile data mismatch");
					funLibrary.testLog.info("Business profile data mismatch");
				}
				
				funLibrary.Assert.assertAll();
			}
	
	
	
	@Test( description = "AC02- Validate response for cancelled state user")
	public void validateResponseCancelledState() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),FunLibrary.excelData.get("PathParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"), FunLibrary.excelData.get("QueryParamvalue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"), FunLibrary.excelData.get("QueryParamvalue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				
				//Get the required path of each field
				
				String stateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].state");
				String cancelDateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].cancelDate");
				
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				String nextFFMDate = dbUtil.getValues("subscription", "NEXTFFMDATE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String nextFFMDateDB= funLibrary.storeDateBeforeWhitespace(nextFFMDate);
				
				// Verify response
				
				if (stateJson.contentEquals("Cancelled")) {
					
					funLibrary.validate_Equals("cancelDate", cancelDateJson, nextFFMDateDB);
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Cancel date data mismatch");
					funLibrary.testLog.info("Cancel date data mismatch");
				}
				
				

				funLibrary.Assert.assertAll();
			}
	
	
	
	
	@Test( description = "AC06- Validate response for selected fetch methods")
	public void validateResponseSelectedFetchedData() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),FunLibrary.excelData.get("PathParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"), FunLibrary.excelData.get("QueryParamvalue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"), FunLibrary.excelData.get("QueryParamvalue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				//Get the required path of each field
				//subscription
				String subscriptionIdJson = funLibrary.getJsonPathValue(response, "subscriptions[0].subscriptionId");
				String descriptionJson = funLibrary.getJsonPathValue(response, "subscriptions[0].description");
				String rateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].rate");
				String moneySavedWithOnlineOrdersJson = funLibrary.getJsonPathValue(response, "subscriptions[0].moneySavedWithOnlineOrders");
				String timeSavedWithOnlineOrdersJson = funLibrary.getJsonPathValue(response, "subscriptions[0].timeSavedWithOnlineOrders");
				String billingFrequencyJson = funLibrary.getJsonPathValue(response, "subscriptions[0].billingFrequency");
				String recurringOrderIdJson = funLibrary.getJsonPathValue(response, "subscriptions[0].recurringOrderId");
				String isUserInGracePeriodJson = funLibrary.getJsonPathValue(response, "subscriptions[0].isUserInGracePeriod");
				String hasUserRedeemedRetentionPromotionJson = funLibrary.getJsonPathValue(response, "subscriptions[0].hasUserRedeemedRetentionPromotion");
				String stateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].state");
				String startDateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].startDate");
				String nextBillingDateJson = funLibrary.getJsonPathValue(response, "subscriptions[0].nextBillingDate");
				String neverSubscribedJson = funLibrary.getJsonPathValue(response, "subscriptions[0].neverSubscribed");
				String paymentMethodnameJson = funLibrary.getJsonPathValue(response, "subscriptions[0].paymentMethod.name");
				
				// Get the response from db
				DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
				//need to provide user id in sheet
				String subscriptionIdDB = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String descriptionDB = dbUtil.getValues("subscription", "DESCRIPTION", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String rateDB = dbUtil.getValues("subscription", "TOTALCOST", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String moneySavedWithOnlineOrdersDB = dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "MONEY_SAVED", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String timeSavedWithOnlineOrdersDB = dbUtil.getValues("wcsowner.X_COLESPLUS_STATS", "TIME_SAVED", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String billingFrequencyDB = dbUtil.getValues("subscription", "PAYMENTFREQ_UOM", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String recurringOrderIdDB = dbUtil.getValues("subscription", "ORDERS_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String isUserInGracePeriodDB = dbUtil.getValues("wcsowner.mbrgrpmbr", "MBRGRP_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and mbrgrp_id in (select mbrgrp_id from mbrgrp where mbrgrpname='SubscriptionPaymentFailedGracePeriod')");
				String hasUserRedeemedRetentionPromotionDB = dbUtil.getValues("wcsowner.mbrgrpmbr", "MBRGRP_ID", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "' and mbrgrp_id in (select mbrgrp_id from mbrgrp where mbrgrpname='RetainColesPlusMembershipGroup')");
				String stateDB = dbUtil.getValues("subscription", "STATUS", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String startDate = dbUtil.getValues("subscription", "STARTDATE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String startDateDB= funLibrary.storeDateBeforeWhitespace(startDate);
				String nextBillingDate = dbUtil.getValues("subscription", "NEXTPAYMENTDATE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
				String nextBillingDateDB= funLibrary.storeDateBeforeWhitespace(nextBillingDate);
				String neverSubscribedDB = dbUtil.getValues("wcsowner.edppayinst", "EDPPAYINST_ID", "EDPORDER_ID = '" + FunLibrary.excelData.get("EDPORDER_ID") + "'");
				String paymentMethodnameDB = dbUtil.getValues("wcsowner.edppayinst", "PAYMENTMETHOD", "EDPORDER_ID = '" + FunLibrary.excelData.get("EDPORDER_ID") + "'");
				
				// //Verify response for Subscription
				funLibrary.validate_Equals("subscriptionId", subscriptionIdJson, subscriptionIdDB);
				funLibrary.validate_Equals("description", descriptionJson, descriptionDB);
				funLibrary.validate_Contains("rate", rateDB, rateJson);
				
				
				if (moneySavedWithOnlineOrdersDB.contains("No Record Found") && moneySavedWithOnlineOrdersJson.contains("0.0")) {
					
					funLibrary.Assert.assertTrue(true, "No online money saved, behaviour as expected");
					funLibrary.testLog.info("No online money saved, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Behaviour unexpected for money saved");
					funLibrary.testLog.info("Behaviour unexpected for money saved");
				}
				
				
				if (timeSavedWithOnlineOrdersJson.contains("0.0") && timeSavedWithOnlineOrdersDB.contains("No Record Found")) {
					
					funLibrary.Assert.assertTrue(true, "No online time saved, behaviour as expected");
					funLibrary.testLog.info("No online time saved, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Behaviour unexpected for time saved");
					funLibrary.testLog.info("Behaviour unexpected for time saved");
				}
				
				
				if (billingFrequencyJson.contentEquals("Monthly") && billingFrequencyDB.contains("MON")) {
					
					funLibrary.Assert.assertTrue(true, "Billing Frequency matched");
					funLibrary.testLog.info("Billing Frequency matched");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Billing Frequency not matched");
					funLibrary.testLog.info("Billing Frequency not matched");
				}
				
				
				funLibrary.validate_Equals("recurringOrderId", recurringOrderIdJson, recurringOrderIdDB);
				
				
				if (isUserInGracePeriodDB.contains("No Record Found") && isUserInGracePeriodJson.contains("false")) {
					
					funLibrary.Assert.assertTrue(true, "User not in grace period, behaviour as expected");
					funLibrary.testLog.info("User not in grace period, behaviour as expected");
				}
				
				else if (isUserInGracePeriodDB !=null && isUserInGracePeriodJson.contains("true")){
					funLibrary.Assert.assertTrue(true, "User in grace period, behaviour as expected");
					funLibrary.testLog.info("User in grace period, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Grace period data mismatch");
					funLibrary.testLog.info("Grace period data mismatch");
				}
				
				
				if (hasUserRedeemedRetentionPromotionDB.contains("No Record Found") && hasUserRedeemedRetentionPromotionJson.contains("false")) {
					
					funLibrary.Assert.assertTrue(true, "User not redeemed promotion, behaviour as expected");
					funLibrary.testLog.info("User not redeemed promotion, behaviour as expected");
				}
				
				else if (hasUserRedeemedRetentionPromotionDB !=null && hasUserRedeemedRetentionPromotionJson.contains("true")){
					funLibrary.Assert.assertTrue(true, "User redeemed promotion, behaviour as expected");
					funLibrary.testLog.info("User redeemed promotion, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Redemption promo data mismatch");
					funLibrary.testLog.info("edemption promo data mismatch");
				}
				 
				
				if (stateJson.contentEquals("Active") && stateDB.contentEquals("1")) {
					
					funLibrary.Assert.assertTrue(true, "Status matched");
					funLibrary.testLog.info("Status matched");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				
				funLibrary.validate_Equals("startDate", startDateJson, startDateDB);
				funLibrary.validate_Equals("nextBillingDate", nextBillingDateJson, nextBillingDateDB);
				
				if (neverSubscribedJson.contentEquals("false") && neverSubscribedDB != null) {
					
					funLibrary.Assert.assertTrue(true, "User has subscribed for coles plus, behaviour as expected");
					funLibrary.testLog.info("User has subscribed for coles plus, behaviour as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "User has never subscribed for coles plus, behaviour unexpected");
					funLibrary.testLog.info("User has never subscribed for coles plus, behaviour unexpected");
				}
				
				if (paymentMethodnameJson !=null && paymentMethodnameDB.contentEquals("MPGSSavedCard")) {
					
					funLibrary.Assert.assertTrue(true, "User has valid payment method");
					funLibrary.testLog.info("User has valid payment method");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "User has no payment method");
					funLibrary.testLog.info("User has no payment method");
				}

				
				//Verify non existing fields
				if (response.jsonPath().getJsonObject("paymentMethod") == null) {
					
					funLibrary.Assert.assertTrue(true, "paymentMethod not found, as expected");
					funLibrary.testLog.info("paymentMethod not found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "paymentMethod found, not expected");
					funLibrary.testLog.info("paymentMethod found, not expected");
				}
				
				
				if (response.jsonPath().getJsonObject("plans") == null) {
					
					funLibrary.Assert.assertTrue(true, "plans not found, as expected");
					funLibrary.testLog.info("plans not found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "plans found, not expected");
					funLibrary.testLog.info("plans found, not expected");
				}
				
				if (response.jsonPath().getJsonObject("profile") == null) {
					
					funLibrary.Assert.assertTrue(true, "profile not found, as expected");
					funLibrary.testLog.info("profile not found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "profile found, not expected");
					funLibrary.testLog.info("profile found, not expected");
				}

				funLibrary.Assert.assertAll();
			}
	
	
	
	@Test( description = "AC07- Validate response for non coles plus member")
	public void validateResponseNonCloesPlus() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"),FunLibrary.excelData.get("PathParamValue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey2"), FunLibrary.excelData.get("QueryParamvalue2"));
				restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey3"), FunLibrary.excelData.get("QueryParamvalue3"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
			
				String neverSubscribed = jsonPathId.getString("subscriptions[0].neverSubscribed");

				// Verify response
				
				funLibrary.validate_Contains("neverSubscribed", "true", neverSubscribed);
				
				if (response.jsonPath().getJsonObject("subscriptions[0].paymentMethod") == null) {
					
					funLibrary.Assert.assertTrue(true, "paymentMethod not found, as expected");
					funLibrary.testLog.info("paymentMethod not found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "paymentMethod found, not expected");
					funLibrary.testLog.info("paymentMethod found, not expected");
				}
				
				
				if (response.jsonPath().getJsonObject("plans") != null) {
					
					funLibrary.Assert.assertTrue(true, "plans found, as expected");
					funLibrary.testLog.info("plans found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "plans not found, not expected");
					funLibrary.testLog.info("plans not found, not expected");
				}
				
				if (response.jsonPath().getJsonObject("profile") != null) {
					
					funLibrary.Assert.assertTrue(true, "profile found, as expected");
					funLibrary.testLog.info("profile found, as expected");
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "profile not found, not expected");
					funLibrary.testLog.info("profile not found, not expected");
				}

				funLibrary.Assert.assertAll();
			}
	
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}