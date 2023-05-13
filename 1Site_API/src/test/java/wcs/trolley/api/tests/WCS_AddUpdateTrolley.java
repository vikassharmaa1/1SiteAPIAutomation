package wcs.trolley.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_AddUpdateTrolley extends Base_Class_API implements ITest {

	Response response = null;
	RestLibrary restLibrary = null;

	public void AddUpdateTrolley_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.addUpdateTrolley_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"),
				FunLibrary.excelData.get("PathParam_sotreId"), FunLibrary.excelData.get("RequestBody"));
		restLibrary.getResponseBody(response);
	}

	public void updateItem(String partNumber, String orderItemId, String qty, String additionFields) {
		restLibrary = new RestLibrary();
		
		response = APILibrary.updateItem_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"),
				FunLibrary.excelData.get("PathParam_sotreId"), partNumber, orderItemId, qty, additionFields);
		restLibrary.getResponseBody(response);
	}

	public void addItem(String partNumber, String qty, String additionFields) {
		restLibrary = new RestLibrary();
		response = APILibrary.addItem_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_sotreId"),
				partNumber, qty, additionFields);
		restLibrary.getResponseBody(response);
	}
		
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_ValidRequest(String testname) {
		AddUpdateTrolley_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String description = FunLibrary.excelData.get("Description");
		String reasonCode = FunLibrary.excelData.get("ReasonCode");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "code", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "description", description);
		funLibrary.validateJSONPathValue_Equals(response, "reasonCode", reasonCode);

		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_InvalidRequest(String testname) {
		AddUpdateTrolley_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String description = FunLibrary.excelData.get("Description");
		String reasonCode = FunLibrary.excelData.get("ReasonCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorKey = FunLibrary.excelData.get("ErrorKey");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorKey", errorKey);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "code", errorCode);
			funLibrary.validateJSONPathValue_Equals(response, "description", description);
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", reasonCode);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functional", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_Functional(String testname) {
		AddUpdateTrolley_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Add, Update and Remove items from trolley. AdditionDataRequired is true")
	public void validateAddRemoveItems() {
		String unitPrice = "";
		String totalProduct = "";
		String qty ;
		String orderId = "";
		String orderItemId = "";
		String partNumber = FunLibrary.excelData.get("PartNumber");		
				
		// Removing all item from cart
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("userId"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_sotreId"));
		
		// Adding items to cart
		AddUpdateTrolley_API();
		// validate status code and schema
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add");
		// getting order item values from DB
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("TestUser") + "') and STATUS='P'");
		unitPrice = dbUtils.getValues("ORDERITEMS", "PRICE", "ORDERS_ID='" + orderId + "'");
		 qty = dbUtils.getValues("ORDERITEMS", "QUANTITY", "ORDERS_ID='" + orderId + "'").split("\\.")[0];
		partNumber = dbUtils.getValues("ORDERITEMS", "PARTNUM", "ORDERS_ID='" + orderId + "'");
		totalProduct = dbUtils.getValues("ORDERITEMS", "TOTALPRODUCT", "ORDERS_ID='" + orderId + "'");
		
		Double unitPrice_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].unitPrice"));
		Double itemTotal_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].itemTotal"));
		Double itemSaving_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].itemSaving"));
				
		// validate other attribute of response
		funLibrary.validateJSONPathValue_Equals(response, "orderID", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", qty);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", partNumber);
		funLibrary.validate_Equals("UnitPrice",Double.parseDouble(unitPrice),unitPrice_d);		
		funLibrary.validate_Equals("Total", Double.parseDouble(totalProduct), (itemTotal_d+itemSaving_d));

		orderItemId = response.jsonPath().get("orderItems[0].orderItemId");
		// Updating existing item in cart
		updateItem(FunLibrary.excelData.get("PartNumber"), orderItemId, "5", "true");
		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add");
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", "5");
				
		// Deleting the item
		updateItem(FunLibrary.excelData.get("PartNumber"), orderItemId, "0", "true");
		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_delete");
		funLibrary.validateJSONPathValue_Equals(response, "orderID", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "totalQty", "0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", "0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", "0");
		dbUtils.closeDBConnection();
		
		funLibrary.Assert.assertAll();
	}


	@Test(description = "Validate that additional fields are not visible when AdditionDataRequired is False")
	public void validateAdditionalDataRequiredFlase() {
		String orderItemId = "";
				
		// Adding items to cart
		AddUpdateTrolley_API();
		// validate status code and schema
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add_additionalFieldsFalse");
		
		
		// Updating existing item in cart
		orderItemId = response.jsonPath().get("orderItems[0].orderItemId");
		updateItem(FunLibrary.excelData.get("PartNumber"), orderItemId, "2", "false");
		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add_additionalFieldsFalse");

		// Deleting the item
		updateItem(FunLibrary.excelData.get("PartNumber"), orderItemId, "0", "false");
		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_delete_additionalFieldsFalse");
		funLibrary.Assert.assertAll();
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
}
