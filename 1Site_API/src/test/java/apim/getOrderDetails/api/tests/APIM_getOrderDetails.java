package apim.getOrderDetails.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import java.io.IOException;

import org.json.JSONObject;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class APIM_getOrderDetails extends Base_Class_API implements ITest {
	

	RestLibrary restLibrary;
	Response response;
	public void getOrderDetails(String orderId) {
        restLibrary = new RestLibrary();
        response = APILibrary.getOrderDetail_APIM(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_userauthorization"),
                FunLibrary.excelData.get("Header_authorization"),    
                FunLibrary.excelData.get("Header_subscriptionKey"),
                orderId,
                FunLibrary.excelData.get("Ver"));
        
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodes(String testname) {
		// Getting store id
		getOrderDetails(FunLibrary.excelData.get("orderId"));
		// Validating status code
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("ErrorMessage"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateInvalidErrorCodes(String testname) {
		// Getting store id
		getOrderDetails(FunLibrary.excelData.get("orderId"));
		// Validating status code
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;

		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(dataProvider = "functional", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		getOrderDetails(FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;

		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test()
	public void validateOrderDetails() throws IOException {
		restLibrary = new RestLibrary();
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("User"), FunLibrary.excelData.get("Header_userauthorization"),
				"0404");

		// Add Item To Trolley
		APILibrary.addItem_APIM("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"),
				FunLibrary.excelData.get("Header_subscriptionKey"), FunLibrary.excelData.get("storeId"), "3967235", "2",
				"false");
		// Localizing to HD
		APILibrary.localizationBySuburbPostCode_APIM("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"),
				FunLibrary.excelData.get("Header_subscriptionKey"), "20509", "3121", "RICHMOND");

		APILibrary.localizationBySuburbPostCode_APIM("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"),
				FunLibrary.excelData.get("Header_subscriptionKey"), "20510", "0820", "Bayview");

		// Get slot id and ccp address id
		String slotId = funLibrary.getSlotID("HD", "0404HD", "", 1);
		String ccpAddressId = FunLibrary.getCCPAddressID(FunLibrary.excelData.get("User"), "Home Bayview");

		// reserve slot
		restLibrary.getResponseBody(
				APILibrary.reserveSlot_APIM("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId));

		// call process API
		APILibrary.orderProcessAPIM_1("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"),
				FunLibrary.excelData.get("Header_subscriptionKey"), "1.1");

		Response orderSubmit = APILibrary.orderSubmit_APIM("", "", "", "0404",
				FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"),
				FunLibrary.excelData.get("Header_subscriptionKey"), "", "", "1.1");
		restLibrary.getResponseBody(orderSubmit);
		// Validating status code
		funLibrary.validateStatusCode(orderSubmit, FunLibrary.excelData.get("StatusCode"));
		String submitOrderResponse = orderSubmit.getBody().asString();
		// Creating json object and getting order id
		JSONObject jsonObject = new JSONObject(submitOrderResponse);
		String orderId = jsonObject.getString("orderId");
		// Executing get order details API
		getOrderDetails(orderId);
		String getOrderResponse = response.getBody().asString().replace("\"orderStatus\": \"M\",", "");
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// Validation order details in both api response
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "serviceType", response, "serviceType");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "dlAddressId", response, "dlAddressId");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderId", response, "orderId");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "totalQty", response, "totalQty");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderAttributes.orderBaggingPreference", response,
				"orderAttributes.orderBaggingPreference");

		// Validation customer details in both api response
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.firstName", response,
				"customerDetailsOnOrderSubmission.firstName");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.lastName", response,
				"customerDetailsOnOrderSubmission.lastName");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.email1", response,
				"customerDetailsOnOrderSubmission.email1");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.deliveryPostcode",
				response, "customerDetailsOnOrderSubmission.deliveryPostcode");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.deliveryState",
				response, "customerDetailsOnOrderSubmission.deliveryState");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.deliveryCountry",
				response, "customerDetailsOnOrderSubmission.deliveryCountry");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.deliveryAddressLine",
				response, "customerDetailsOnOrderSubmission.deliveryAddressLine");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "customerDetailsOnOrderSubmission.deliverySuburb",
				response, "customerDetailsOnOrderSubmission.deliverySuburb");

		// Validation items details in both api response
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].unitPrice", response,
				"orderItems[0].unitPrice");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].itemTotal", response,
				"orderItems[0].itemTotal");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].itemSaving", response,
				"orderItems[0].itemSaving");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].orderItemId", response,
				"orderItems[0].orderItemId");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].qty", response, "orderItems[0].qty");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "orderItems[0].partNumber", response,
				"orderItems[0].partNumber");

		// validating payment details in both api response
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "paymentMethod.data.cardExpiry", response,
				"paymentMethod.data.cardExpiry");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "paymentMethod.data.cardType", response,
				"paymentMethod.data.cardType");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "paymentMethod.data.maskedCardNumber", response,
				"paymentMethod.data.maskedCardNumber");
		funLibrary.validateTwoJSONPathValue_Equals(orderSubmit, "paymentMethod.name", response, "paymentMethod.name");

		// Validating both the API response are identical or not
		funLibrary.compare_TwoJsonObjects(getOrderResponse, submitOrderResponse);
		funLibrary.Assert.assertAll();
	}


	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
