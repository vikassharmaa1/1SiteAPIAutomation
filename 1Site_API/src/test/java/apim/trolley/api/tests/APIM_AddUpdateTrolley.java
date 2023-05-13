package apim.trolley.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class APIM_AddUpdateTrolley extends Base_Class_API implements ITest {

	Response response = null;
	RestLibrary restLibrary = null;
	
	public void AddUpdateTrolley_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.addUpdateTrolley_APIM(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("Header_Ocp-Apim-Subscription-Key"),
				FunLibrary.excelData.get("RequestBody"));
		restLibrary.getResponseBody(response);
	}

	public void updateItem(String partNumber, String orderItemId, String qty, String additionFields) {
		restLibrary = new RestLibrary();

		response = APILibrary.updateItem_APIM(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("Header_Ocp-Apim-Subscription-Key"), FunLibrary.excelData.get("StoreId"),
				partNumber, orderItemId, qty, additionFields);
		restLibrary.getResponseBody(response);
	}

	public void addItem(String partNumber, String qty, String additionFields) {
		restLibrary = new RestLibrary();
		response = APILibrary.addItem_APIM(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("StoreId"), FunLibrary.excelData.get("Header_Ocp-Apim-Subscription-Key"),
				partNumber, qty, additionFields);
		restLibrary.getResponseBody(response);
	}
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_ValidRequest(String testname) {
		AddUpdateTrolley_API();

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "message", "Resource not found");
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_InvalidRequest(String testname) {
		AddUpdateTrolley_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
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
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Add, Update and Remove items from trolley. AdditionDataRequired is true")
	public void validateAddRemoveItems() {
		String unitPrice = "";
		String totalProduct = "";
		String qty;
		String orderId = "";
		String orderItemId = "";
		String partNumber = FunLibrary.excelData.get("PartNumber");

		// Removing all item from cart
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("TestUser"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("StoreId"));
		APILibrary.localizationByAddressId_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", "59484036");
		// Adding items to cart
		AddUpdateTrolley_API();

		// validate status code
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// validate schema
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add");

		// getting order item values from DB
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("TestUser") + "') and STATUS='P'");
		unitPrice = dbUtils.getValues("ORDERITEMS", "PRICE", "ORDERS_ID='" + orderId + "'");
		qty = dbUtils.getValues("ORDERITEMS", "QUANTITY", "ORDERS_ID='" + orderId + "'").split("\\.")[0];
		partNumber = dbUtils.getValues("ORDERITEMS", "PARTNUM", "ORDERS_ID='" + orderId + "'");
		totalProduct = dbUtils.getValues("ORDERITEMS", "TOTALPRODUCT", "ORDERS_ID='" + orderId + "'");
		dbUtils.closeDBConnection();

		Double unitPrice_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].unitPrice"));
		Double itemTotal_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].itemTotal"));
		Double itemSaving_d = Double.parseDouble(funLibrary.getJsonPathValue(response, "orderItems[0].itemSaving"));

		// validate other attribute of response
		funLibrary.validateJSONPathValue_Equals(response, "orderID", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", qty);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", partNumber);
		funLibrary.validate_Equals("UnitPrice", Double.parseDouble(unitPrice), unitPrice_d);
		funLibrary.validate_Equals("Total", Double.parseDouble(totalProduct), (itemTotal_d + itemSaving_d));

		orderItemId = response.jsonPath().get("orderItems[0].orderItemId");

		// Updating existing item in cart
		updateItem(partNumber, orderItemId, "5", "true");

		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add");
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", "5");

		// Deleting the item
		updateItem(partNumber, orderItemId, "0", "true");

		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_delete");
		funLibrary.validateJSONPathValue_Equals(response, "orderID", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "totalQty", "0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", "0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", "0");
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

		// getting order item values from DB
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		String orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("TestUser") + "') and STATUS='P'");

		String partNumber = dbUtils.getValues("ORDERITEMS", "PARTNUM", "ORDERS_ID='" + orderId + "'");

		dbUtils.closeDBConnection();

		// Updating existing item in cart
		orderItemId = response.jsonPath().get("orderItems[0].orderItemId");
		updateItem(partNumber, orderItemId, "2", "false");

		// validations
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "addupdatetrolley_add_additionalFieldsFalse");

		// Deleting the item
		updateItem(partNumber, orderItemId, "0", "false");

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
