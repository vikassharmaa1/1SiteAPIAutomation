package wcs.trolley.api.tests;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_SyncValidation extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;

	public void orderValidation_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.orderValidation_WCS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_access-token"),
                FunLibrary.excelData.get("Header_user-jwt-token"),             
                FunLibrary.excelData.get("PathParam_storeId"),
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_ver"));
        
        // get the response body and print on console
        restLibrary.getResponseBody(response); 
	}

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		// ValidatableResponse
		funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("ErrorCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "dwServiceType", FunLibrary.excelData.get("ServiceType"));
			funLibrary.validateJSONPathValue_Equals(response, "maxItemsLimitMet", FunLibrary.excelData.get("MaxItemsLimitMet"));
			funLibrary.validateJSONPathValue_Equals(response, "minimumOrderValueMet", FunLibrary.excelData.get("MinimumOrderValueMet"));

			/*funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
			funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("OrderShipping"));
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", FunLibrary.excelData.get("ActionRequired"));
			funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("OrderSaving"));
			funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("OrderSubTotal"));*/
			break;
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	/*
	 * @Test(dataProvider = "r2Changes", dataProviderClass = API_DataProvider.class)
	 * public void validateFunctionalErrorCodesR2Changes(String testname) {
	 * RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
	 * FunLibrary.excelData.get("BasePath"),
	 * FunLibrary.excelData.get("RequestType"));
	 * 
	 * restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"),
	 * FunLibrary.excelData.get("Header1Value"));
	 * restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"),
	 * FunLibrary.excelData.get("Header2Value"));
	 * restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"),
	 * FunLibrary.excelData.get("pathParameterValue"));
	 * restLibrary.addQueryParameter(FunLibrary.excelData.get("queryParameterKey"),
	 * FunLibrary.excelData.get("queryParameterValue"));
	 * restLibrary.addBody(FunLibrary.excelData.get("RequestBody")); Response
	 * response = restLibrary.executeAPI(); restLibrary.getResponseBody(response);
	 * funLibrary.validateStatusCode(response,
	 * FunLibrary.excelData.get("StatusCode"));
	 * 
	 * // ValidatableResponse switch (FunLibrary.excelData.get("StatusCode")) { case
	 * "200": funLibrary.validateJSONPathValue_Equals(response, "actionRequired",
	 * FunLibrary.excelData.get("ActionRequired"));
	 * funLibrary.validateJSONPathValue_Equals(response, "dwSlotValid",
	 * FunLibrary.excelData.get("DwSlotValid"));
	 * funLibrary.validateJSONPathValue_Equals(response, "dwServiceType",
	 * FunLibrary.excelData.get("DwServiceType"));
	 * funLibrary.validateJSONPathValue_Equals(response, "hasValidAddress",
	 * FunLibrary.excelData.get("HasValidAddress"));
	 * funLibrary.validateJSONPathValue_Equals(response, "minimumOrderValueMet",
	 * FunLibrary.excelData.get("MinimumOrderValueMet"));
	 * funLibrary.validateJSONPathValue_Equals(response, "maxItemsLimitMet",
	 * FunLibrary.excelData.get("MaxItemsLimitMet"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderItemsInSync",
	 * FunLibrary.excelData.get("OrderItemsInSync"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderId",
	 * orderId);
	 * funLibrary.validateJSONPathValue_Equals(response, "hasValidPhone",
	 * FunLibrary.excelData.get("HasValidPhone"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderSaving",
	 * FunLibrary.excelData.get("OrderSaving"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderShipping",
	 * FunLibrary.excelData.get("OrderShipping"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal",
	 * FunLibrary.excelData.get("OrderSubTotal"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderItemsValid",
	 * FunLibrary.excelData.get("OrderItemsValid")); break;
	 * 
	 * case "206": funLibrary.validateJSONPathValue_Equals(response, "serviceType",
	 * FunLibrary.excelData.get("serviceType"));
	 * 
	 * if(!(FunLibrary.excelData.get("orderValid").equalsIgnoreCase("false"))) {
	 * funLibrary.validateJSONPathValue_Equals(response, "orderValid",
	 * FunLibrary.excelData.get("orderValid"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].orderItemsValid",
	 * FunLibrary.excelData.get("orderItemsValid"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].overallMinimumOrderValueMet",
	 * FunLibrary.excelData.get("overallMinimumOrderValueMet"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].hasAgeGateViewed",
	 * FunLibrary.excelData.get("hasAgeGateViewed"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].hasValidPhone",
	 * FunLibrary.excelData.get("hasValidPhone"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].overallMinimumOrderValue",
	 * FunLibrary.excelData.get("overallMinimumOrderValue"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "orderValidationDetails[0].maxItemsLimitMet",
	 * FunLibrary.excelData.get("maxItemsLimitMet")); }
	 * funLibrary.validateJSONPathValue_Equals(response, "slotValid",
	 * FunLibrary.excelData.get("slotValid"));
	 * funLibrary.validateJSONPathValue_Equals(response, "orderId",
	 * orderId);
	 * funLibrary.validateJSONPathValue_Equals(response, "orderShipping",
	 * FunLibrary.excelData.get("orderShipping"));
	 * funLibrary.validateJSONPathValue_Equals(response, "hasAgeGateViewed",
	 * FunLibrary.excelData.get("hasAgeGateViewed"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "slotValidationDetails[0].baggingOptionValid",
	 * FunLibrary.excelData.get("baggingOptionValid"));
	 * funLibrary.validateJSONPathValue_Equals(response,
	 * "slotValidationDetails[0].overallMinimumOrderValueMet",
	 * FunLibrary.excelData.get("overallMinimumOrderValueMet"));
	 * funLibrary.validateJSONPathValue_Equals(response, "actionRequired",
	 * FunLibrary.excelData.get("actionRequired"));
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * case "401": case "400": funLibrary.validateJSONPathValue_Equals(response,
	 * "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
	 * funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
	 * FunLibrary.excelData.get("ErrorMessage"));
	 * funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
	 * FunLibrary.excelData.get("ErrorLevel/Priority")); break; default:
	 * testLog.info(
	 * "No validation found for this test. Please implement validations for " +
	 * FunLibrary.excelData.get("StatusCode")); break; }
	 * funLibrary.Assert.assertAll(); }
	 */

	@Test(description = "Validate Slot Max Capacity ")
	public void validateSlotMaxCapacity() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String maxItemsLimit = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMMaxItems'");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.maxItemsLimit", maxItemsLimit);
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.maxItemsLimitMet", "false");
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.maxItemsLimit", maxItemsLimit);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.maxItemsLimitMet", "false");
				break;
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate response when delivery address does not match the service zone of the slot ")
	public void validateInvalidServiceZone() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String locationZoneId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMLocationZoneId'");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.locationZoneId", locationZoneId);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.hasValidAddressLocation", "false");
			break;		
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate Expired Slot")
	public void validateExpiredSlot( ) {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String slotId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowId'");
			String slotTimeStart = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowStartTime'");
			String slotTimeEnd = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowEndTime'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotTimeValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotId", slotId);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotTime.start", slotTimeStart.replace(" ", "T"));
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotTime.end", slotTimeEnd.replace(" ", "T"));
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "Validate Overall Minimum Order Value")
	public void validateOverallMinimumOrderValue() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String slotId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowId'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotId", slotId);
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.overallMinimumOrderValueMet", "false");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.overallMinimumOrderValueMet","false");
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.overallMinimumOrderValue", FunLibrary.excelData.get("overallMinimumOrderValue"));
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.overallMinimumOrderValue", FunLibrary.excelData.get("overallMinimumOrderValue"));
			
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate Valid Phone Number")
	public void validateValidPhoneNumber() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.hasValidPhone","true");
		
			
			break;
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate Age restricted product")
	public void validateAgeGate() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.hasAgeGateViewed","false");
			funLibrary.validateJSONPathValue_Equals(response, "hasAgeGateViewed","false");
		
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	
	
	@Test(description = "Validate Slot restricted product")
	public void validateSlotRestricted() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String slotId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowId'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "orderValid", "false");
			
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Contains(response, "slotValidationDetails.slotId", slotId);
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.actionRequiredOrderItems[0].validationErrMsg", "Age restricted item");
			funLibrary.validateJSONPathValue_Equals(response, "orderValidationDetails.actionRequiredOrderItems[0].validationCode", "8");
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.actionRequiredOrderItems[0].validationErrMsg", "DONUTS");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.actionRequiredOrderItems[0].validationCode", "6");
			
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate response when reserved slot's bagging option does not match with the order's bagging option")
	public void validateIncorrectBaggingOption() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String slotId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowId'");
			String slotBaggingOption = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliverySlotBaggingOptions'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			

			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotId",slotId);
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.baggingOptionValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotBaggingOption", slotBaggingOption);
			
			
			break;
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate response when reserved slot's attended/unattended does not match the attended/unattended option on the order")
	public void validateIncorrectAttendedType() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			String slotId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliveryWindowId'");
			String slotAttendedType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMDeliverySlotUnattendedType'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			

			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotId",slotId);
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.attendedTypeValid", "false");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.slotAttendedType", slotAttendedType);
			
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "Validate response when trolley contains items that cannot be left unattended And The slot reserved is of unattended type ")
	public void validateUnattendedSlot() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.actionRequiredOrderItems[0].validationErrMsg", "Item cannot be left unattended");
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.actionRequiredOrderItems[0].validationCode", "7");
			//funLibrary.validate_NotNullValue(response, "orderValidationDetails.orderItemsUnsuitableForUnattendedDelivery");	
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate response when reserved slot's service type does not match with the order's service type")
	public void validateIncorrectServiceType() {
		
		orderValidation_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			
			break;
			
		case "206":	
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='DMServiceType'");
			
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
			funLibrary.validateJSONPathValue_Equals(response, "actionRequired", "true");
			funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValid", "false");
			
			
			funLibrary.validateJSONPathValue_Equals(response, "slotValidationDetails.serviceTypeValid", "false");
			break;
			
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate response validationCode attribute and the error message when the trolly contains liquor items and the customer should be in liquor exclusion segment")
	public void validLiquorExclusionOrder() {
		restLibrary = new RestLibrary();
		//APILibrary.EmptyTrolley(FunLibrary.excelData.get("User"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"));
		
		//APILibrary.addItem_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"), "6593222", "2", "false");

		//Call order validation API
		orderValidation_API();
		String jsonString = response.getBody().asString() ;
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONObject sample = jsonObject.getJSONObject("orderValidationDetails");
		org.json.JSONArray array = sample.getJSONArray("actionRequiredOrderItems");
		ArrayList<String> sampleArray = new ArrayList<>();
		ArrayList<String> sampleArray2 = new ArrayList<>();
		for(int i=0; i < array.length(); i++) {
			JSONObject obj12 = array.getJSONObject(i);
			String value1 = obj12.getString("validationErrMsg");
			int value2 = obj12.getInt("validationCode");
			sampleArray.add(value1);
			sampleArray2.add(String.valueOf(value2));
		}
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validate_Equals("validationErrMsg", FunLibrary.excelData.get("ErrorMessage"), sampleArray.get(1));
		funLibrary.validate_Equals("validationCode", FunLibrary.excelData.get("ErrorCode"), sampleArray2.get(1));
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
}
