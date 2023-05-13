package wcs.trolley.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;
import orderFullSummary.pojo.OrderAttributes;
import orderFullSummary.pojo.OrderItem;
import orderFullSummary.pojo.Root;

public class WCS_FullOrderSummary extends Base_Class_API implements ITest {

	Response response = null;
	String baseURI;
	String basePath;
	String requestType;
	String storeId;
	Root root;

	public void OrderFullSummaryAPI() {

		String baseURI = Base_Class_API.BaseURI;
		String basePath = FunLibrary.excelData.get("BasePath");
		String requestType = FunLibrary.excelData.get("RequestType");
		storeId = FunLibrary.excelData.get("PathParameterValue");

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), storeId);
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey"), FunLibrary.excelData.get("QueryParamValue"));
		response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		if(response.getStatusCode() == 200) {
			root = (Root) restLibrary.getResponseBody(response, Root.class);
		}

	}

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		OrderFullSummaryAPI();
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Contains(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateEmptyTrolley_HD() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='"+FunLibrary.excelData.get("NickName")+"' and STATUS='P' and ADDRESSTYPE='SB'");
		String db_OrderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+"')");
		dbUtil.closeDBConnection();
		funLibrary.localizationByAddressId("0404", db_AddressId);
		
		OrderFullSummaryAPI();
		funLibrary.validate_Equals("serviceType", FunLibrary.excelData.get("ServiceType"), root.getServiceType());
		funLibrary.validate_Equals("dlAddressId", db_AddressId, root.getDlAddressId());
		funLibrary.validate_Equals("catalogId", "11051", root.getCatalogId());
		funLibrary.validate_Equals("orderId", db_OrderId, root.getOrderId());
		funLibrary.validate_Equals("totalQty", 0, root.getTotalQty());
		//funLibrary.validate_Equals("orderShipping", "0.00", root.getOrderShipping().toString());
//		funLibrary.validate_Equals("orderAttributes", "{}", root.getOrderAttributes());
		funLibrary.validate_Equals("storeId", "20510", root.getStoreId());
		funLibrary.validate_Equals("overallMOV", "50", root.getOverallMOV());
		funLibrary.validate_Equals("orderItems", "[]", root.getOrderItems().toString());
		funLibrary.validate_Equals("orderSaving", 0, root.getOrderSaving());
		funLibrary.validate_Equals("orderSubTotal", "0", root.getOrderSubTotal().toString());
		
	}
	
	@Test
	public void validateEmptyTrolley_CC() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_OrderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+"')");
		dbUtil.closeDBConnection();
		funLibrary.localizationByLocationId(FunLibrary.excelData.get("PathParameterValue"),FunLibrary.excelData.get("LocationId") );
		
		OrderFullSummaryAPI();
		funLibrary.validate_Equals("serviceType", FunLibrary.excelData.get("ServiceType"), root.getServiceType());
		funLibrary.validate_Equals("catalogId", "11051", root.getCatalogId());
		funLibrary.validate_Equals("orderId", db_OrderId, root.getOrderId());
		funLibrary.validate_Equals("totalQty", 0, root.getTotalQty());
		funLibrary.validate_Equals("storeId", "20510", root.getStoreId());
		funLibrary.validate_Equals("ccLocationId", FunLibrary.excelData.get("LocationId"), root.getCcLocationId());
		//funLibrary.validate_Equals("orderShipping", "0.00", root.getOrderShipping().toString());
//		funLibrary.validate_Equals("orderAttributes", "{}", root.getOrderAttributes().toString());
		funLibrary.validate_Equals("orderItems", "[]", root.getOrderItems().toString());
		funLibrary.validate_Equals("orderSaving", 0, root.getOrderSaving());
		funLibrary.validate_Equals("orderSubTotal", "0", root.getOrderSubTotal().toString());
		
	}

	@Test
	public void validateEmptyTrolley_RD() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_OrderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+"')");
		dbUtil.closeDBConnection();
		funLibrary.localizationByPostCodeSuburb("0404", FunLibrary.excelData.get("Postcode"), FunLibrary.excelData.get("Suburb"));
				
		OrderFullSummaryAPI();
		funLibrary.validate_Equals("serviceType",  FunLibrary.excelData.get("ServiceType"), root.serviceType);
		funLibrary.validate_Equals("suburbOfSPOnlyLocalisation", FunLibrary.excelData.get("Suburb"), root.getSuburbOfSPOnlyLocalisation());
		funLibrary.validate_Equals("catalogId", "11051", root.getCatalogId());
		funLibrary.validate_Equals("orderId", db_OrderId, root.getOrderId());
		funLibrary.validate_Equals("totalQty", 0, root.totalQty);
		//funLibrary.validate_Equals("orderShipping", "0.00", root.getOrderShipping().toString());
//		funLibrary.validate_Equals("orderAttributes", "{}", root.getOrderAttributes().toString());
		funLibrary.validate_Equals("storeId", "20510", root.storeId);
		funLibrary.validate_Equals("postcodeOfSPOnlyLocalisation", FunLibrary.excelData.get("Postcode"), root.getPostcodeOfSPOnlyLocalisation());
		funLibrary.validate_Equals("orderItems", "[]", root.getOrderItems().toString());
		funLibrary.validate_Equals("orderSaving", 0, root.getOrderSaving());
		funLibrary.validate_Equals("orderSubTotal", "0", root.getOrderSubTotal().toString());
	}

	public void validateOrderItems() throws Exception {
		String userId = FunLibrary.excelData.get("user");
		String[] orderItemId = {"",""};
		String[] partNumber = {
				"4499631", "3216913"
		};
		Integer totalQty = 0;
		Number subTotal = 0;
		
		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		String db_AddressId = dbUtils.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='"+FunLibrary.excelData.get("NickName")+"' and STATUS='P' and ADDRESSTYPE='SB'");
		String orderItemCount = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		dbUtils.closeDBConnection();
		//Localizing to HD
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

	}
	
	public void validateOrderItems_MultiBuy() throws Exception {
		String userId = FunLibrary.excelData.get("user");
		String orderItemId = "";
		String partNumber ="3216913"; //multinbuy partnumber
		
		Integer totalQty = 0;
		Number subTotal = 0;
		
		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		String db_AddressId = dbUtils.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='"+FunLibrary.excelData.get("NickName")+"' and STATUS='P' and ADDRESSTYPE='SB'");
		String orderItemCount = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		dbUtils.closeDBConnection();
		//Localizing to HD
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

	}
	
	public void validateOrderAttributes_HD() {
		
		String partNumber ="3216913";
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and NICKNAME='"+FunLibrary.excelData.get("NickName")+"' and STATUS='P' and ADDRESSTYPE='SB'");
		String db_OrderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+"')");
		String orderItemCount = dbUtil.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and status = 'P')");
		dbUtil.closeDBConnection();
		//Localizing to HD
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
