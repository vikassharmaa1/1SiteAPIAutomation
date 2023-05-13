package ps.trolley.api.tests;

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
import orderFullSummary.pojo.OrderAttributes;
import orderFullSummary.pojo.OrderItem;
import orderFullSummary.pojo.Root;

public class PS_FullOrderSummary extends Base_Class_API implements ITest {

	Response response = null;
	String storeId;
	Root root;
	 
	public void OrderFullSummaryAPI() {

		RestLibrary restLibrary = new RestLibrary();
		String baseURI = Base_Class_API.BaseURI;
		String basePath = FunLibrary.excelData.get("BasePath");
		String requestType = FunLibrary.excelData.get("RequestType");
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String ver = FunLibrary.excelData.get("QueryParam_ver");
		storeId = FunLibrary.excelData.get("QueryParam_storeId");

		response = APILibrary.OrderFullWeightSummary_PS(baseURI, basePath, requestType, authorization, userAuthorization, storeId, ver);

		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}

	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodes(String testname) {
		OrderFullSummaryAPI();
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			//funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("StatusCode"));
			//funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			//funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("StatusCode"));
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateInvalidErrorCodes(String testname) {
		OrderFullSummaryAPI();
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functional", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		OrderFullSummaryAPI();
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateEmptyTrolley_HD() {
		RestLibrary restLibrary = new RestLibrary();
		String addressId = FunLibrary.getAddressID(FunLibrary.excelData.get("User"), "Home");
		String ordersId = FunLibrary.getCurrentOrdersID(FunLibrary.excelData.get("User"));

		restLibrary.getResponseBody(APILibrary.localizationByAddressId_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("QueryParam_storeId"), addressId));

		OrderFullSummaryAPI();

		funLibrary.validateJsonStructure(response, "FullOrderSummary/EmptyTrolley_HD");

		funLibrary.validate_Equals("serviceType", "HD", funLibrary.getJsonPathValue(response, "serviceType"));
		funLibrary.validate_Equals("dlAddressId", addressId, funLibrary.getJsonPathValue(response, "dlAddressId"));
		funLibrary.validate_Equals("orderId", ordersId,  funLibrary.getJsonPathValue(response, "orderId"));
		funLibrary.validate_Equals("totalQty", "0", funLibrary.getJsonPathValue(response, "totalQty"));
		funLibrary.validate_Equals("storeId", "20510",funLibrary.getJsonPathValue(response, "storeId"));
		funLibrary.validate_Equals("overallMOV", "50", funLibrary.getJsonPathValue(response, "overallMOV"));
		funLibrary.validate_Equals("orderSaving", "0",funLibrary.getJsonPathValue(response, "orderSaving"));
		funLibrary.validate_Equals("orderSubTotal", "0", funLibrary.getJsonPathValue(response, "orderSubTotal"));
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateEmptyTrolley_CC() {
		
		RestLibrary restLibrary = new RestLibrary();
		String ordersId = FunLibrary.getCurrentOrdersID(FunLibrary.excelData.get("User"));

		restLibrary.getResponseBody(APILibrary.localizationByLocationId_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("QueryParam_storeId"), "0404CC0404"));
		
		OrderFullSummaryAPI();

		funLibrary.validateJsonStructure(response, "FullOrderSummary/EmptyTrolley_CC");

		funLibrary.validate_Equals("serviceType", "CC", funLibrary.getJsonPathValue(response, "serviceType"));
		funLibrary.validate_Equals("catalogId", "11051", funLibrary.getJsonPathValue(response, "catalogId"));
		funLibrary.validate_Equals("catalogId", "0404CC0404", funLibrary.getJsonPathValue(response, "ccLocationId"));
		funLibrary.validate_Equals("orderId", ordersId, funLibrary.getJsonPathValue(response, "orderId"));
		funLibrary.validate_Equals("totalQty", "0", funLibrary.getJsonPathValue(response, "totalQty"));
		funLibrary.validate_Equals("storeId", "20510",funLibrary.getJsonPathValue(response, "storeId"));
		funLibrary.validate_Equals("overallMOV", "50", funLibrary.getJsonPathValue(response, "overallMOV"));
		funLibrary.validate_Equals("orderSaving", "0",funLibrary.getJsonPathValue(response, "orderSaving"));
		funLibrary.validate_Equals("orderSubTotal", "0", funLibrary.getJsonPathValue(response, "orderSubTotal"));
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateEmptyTrolley_RD() {
		RestLibrary restLibrary = new RestLibrary();
		String ordersId = FunLibrary.getCurrentOrdersID(FunLibrary.excelData.get("User"));
		
		restLibrary.getResponseBody(APILibrary.localizationByFullAddress_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("QueryParam_storeId"), "1.1", 
				"0822", "Daly River", "GANT_717057233", "16ade84a-3a52-4dc8-8c86-e8342022bf8b"));
		
		OrderFullSummaryAPI();
		
		funLibrary.validateJsonStructure(response, "FullOrderSummary/EmptyTrolley_RD");

		funLibrary.validate_Equals("serviceType", "RD", funLibrary.getJsonPathValue(response, "serviceType"));
		funLibrary.validate_Equals("orderId", ordersId, funLibrary.getJsonPathValue(response, "orderId"));
		funLibrary.validate_Equals("totalQty", "0", funLibrary.getJsonPathValue(response, "totalQty"));
		funLibrary.validate_Equals("storeId", "20510",funLibrary.getJsonPathValue(response, "storeId"));
		funLibrary.validate_Equals("overallMOV", "50", funLibrary.getJsonPathValue(response, "overallMOV"));
		funLibrary.validate_Equals("orderSaving", "0",funLibrary.getJsonPathValue(response, "orderSaving"));
		funLibrary.validate_Equals("orderSubTotal", "0", funLibrary.getJsonPathValue(response, "orderSubTotal"));
		funLibrary.Assert.assertAll();
	}
	/*@Test
	public void validateOrderItems() throws Exception {
		String userId = FunLibrary.excelData.get("user");
		String[] orderItemId = {
				"", ""
		};
		String[] partNumber = {
				"4499631", "3216913"
		};
		Integer totalQty = 0;
		Number subTotal = 0;

		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		String db_AddressId = dbUtils.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='" + FunLibrary.excelData.get("NickName") + "' and STATUS='P' and ADDRESSTYPE='SB'");
		String orderItemCount = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		dbUtils.closeDBConnection();
		// Localizing to HD
		funLibrary.localizationByAddressId("0404", db_AddressId);

		// if true add items to cart
		if(orderItemCount.equals("No Record Found")) {
			orderItemId[1] = funLibrary.addItemToTrolley(partNumber[0], "2");
			orderItemId[2] = funLibrary.addItemToTrolley(partNumber[1], "2");
		}

		// get items and their prices from tables on the basis of order item id and verify against the response from Full order summary api
		String unitPrice = dbUtils.getValues("orderitems", "PRICE", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		String itemTotal = dbUtils.getValues("orderitems", "TOTALPRODUCT", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		String quantity = dbUtils.getValues("orderitems", "QUANTITY", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		OrderFullSummaryAPI();

		for (int i = 0; i < 2; ++i) {
			String id = orderItemId[i];
			OrderItem Orderitem = root.getOrderItems().stream().filter(x -> x.getOrderItemId().equals(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("Unit Price", unitPrice, Orderitem.getUnitPrice().toString());
			funLibrary.validate_Equals("PartNumber", partNumber[i], Orderitem.getPartNumber().toString());
			funLibrary.validate_Equals("Item Total", itemTotal, Orderitem.getItemTotal().toString());
			funLibrary.validate_Equals("Item Saving", 0, Orderitem.getItemSaving());
			funLibrary.validate_Equals("Quantity", Integer.parseInt(quantity), Orderitem.getQty());

			totalQty = totalQty + Orderitem.getQty();
			subTotal = subTotal.doubleValue() + Orderitem.getItemTotal().doubleValue();
		}

		funLibrary.validate_Equals("Order Sub Total", subTotal.toString(), root.getOrderSubTotal().toString());
		funLibrary.validate_Equals("Total Quantity", totalQty, root.getTotalQty());

	}*/
	/*@Test
	public void validateOrderItems_MultiBuy() throws Exception {
		String userId = FunLibrary.excelData.get("user");
		String orderItemId = "";
		String partNumber = "3216913"; // multinbuy partnumber

		Integer totalQty = 0;
		Number subTotal = 0;

		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		String db_AddressId = dbUtils.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='" + FunLibrary.excelData.get("NickName") + "' and STATUS='P' and ADDRESSTYPE='SB'");
		String orderItemCount = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		dbUtils.closeDBConnection();
		// Localizing to HD
		funLibrary.localizationByAddressId("0404", db_AddressId);

		// if true add items to cart
		if(orderItemCount.equals("No Record Found")) {
			orderItemId = funLibrary.addItemToTrolley(partNumber, "2");
		}

		// get items and their prices from tables on the basis of order item id and verify against the response from Full order summary api
		String unitPrice = dbUtils.getValues("orderitems", "PRICE", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		String itemTotal = dbUtils.getValues("orderitems", "TOTALPRODUCT", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		String quantity = dbUtils.getValues("orderitems", "QUANTITY", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		OrderFullSummaryAPI();

		for (int i = 0; i < 2; ++i) {
			String id = orderItemId;
			OrderItem Orderitem = root.getOrderItems().stream().filter(x -> x.getOrderItemId().equals(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("Unit Price", unitPrice, Orderitem.getUnitPrice().toString());
			funLibrary.validate_Equals("PartNumber", partNumber, Orderitem.getPartNumber().toString());
			funLibrary.validate_Equals("Item Total", itemTotal, Orderitem.getItemTotal().toString());
			funLibrary.validate_Equals("Item Saving", 0, Orderitem.getItemSaving());
			funLibrary.validate_Equals("Quantity", Integer.parseInt(quantity), Orderitem.getQty());

			totalQty = totalQty + Orderitem.getQty();
			subTotal = subTotal.doubleValue() + Orderitem.getItemTotal().doubleValue();
		}

		funLibrary.validate_Equals("Order Sub Total", subTotal.toString(), root.getOrderSubTotal().toString());
		funLibrary.validate_Equals("Total Quantity", totalQty, root.getTotalQty());

	}*/
	/*@Test
	public void validateOrderAttributes_HD() {

		String partNumber = "3216913";
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='" + FunLibrary.excelData.get("NickName") + "' and STATUS='P' and ADDRESSTYPE='SB'");
		String db_OrderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "')");
		String orderItemCount = dbUtil.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and status = 'P')");
		dbUtil.closeDBConnection();
		// Localizing to HD
		funLibrary.localizationByAddressId("0404", db_AddressId);
		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		// if true add items to cart
		if(orderItemCount.equals("No Record Found")) {
			funLibrary.addItemToTrolley(partNumber, "2");
		}
		OrderAttributes orderAttr = root.getOrderAttributes();
		OrderFullSummaryAPI();
		funLibrary.validate_Equals("orderId", db_OrderId, root.getOrderId());
		funLibrary.validate_Equals("orderShipping", "", root.getOrderShipping().toString());
		funLibrary.validate_Equals("serviceType", "{}", orderAttr.getServiceType());
		funLibrary.validate_Equals("timeZone", "", orderAttr.getTimeZone());
		funLibrary.validate_Equals("deliverySlotBaggingOption", 0, orderAttr.getDeliverySlotBaggingOption());
		funLibrary.validate_Equals("orderTotalGapToGetFreeShipCharge", 0, orderAttr.getOrderTotalGapToGetFreeShipCharge());
		funLibrary.validate_Equals("orderFlybuyNumber", "", orderAttr.getOrderFlybuyNumber());
		funLibrary.validate_Equals("orderBagCountForBagless", "", orderAttr.getOrderBagCountForBagless());

	}
*/
	public void validateOrderAttributes_UnatttandedSlot() {
		// select unattended slot
		// validate slot info
	}

	public void validateOrderAttributes_CC() {
		// select unattended slot
		// validate slot info
	}

	public void validateCCSlot_WithBag() {
		// select unattended slot
		// validate slot info
	}

	public void validateCCSlot_WithoutBag() {
		// select unattended slot
		// validate slot info
	}

	public void validateDeliveryInstructions() {

	}

	public void validateCCPAttributes() {

	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
