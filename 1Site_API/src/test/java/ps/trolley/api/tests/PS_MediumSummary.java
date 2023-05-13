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

public class PS_MediumSummary extends Base_Class_API implements ITest {
	
	
	Response response = null;
	RestLibrary restLibrary = null;

	public void OrderMediumtWeightSummary_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.OrderMediumWeightSummary_PS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"),FunLibrary.excelData.get("QueryParam_ver"));
		//restLibrary.getResponseBody(response);
	}
	
	public void addItem(String partNumber, String qty, String additionFields) {
		restLibrary = new RestLibrary();
		response = APILibrary.addItem_PS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), FunLibrary.excelData.get("StoreId"),
				partNumber, qty, additionFields);
		restLibrary.getResponseBody(response);
	}
	
	public void validateMediumWeightSummaryResponse() {
		
		String orderId = "";
		String serviceType = "";
		String ccLocationId = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String overallMOV ="";
		
		orderId = funLibrary.getCurrentOrdersID(FunLibrary.excelData.get("UserID"));
	
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		
		if(serviceType.equalsIgnoreCase("HD")) {
			zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMLocationZoneId' and ORDERS_ID='" + orderId + "'");
			webStoreId = zoneId.substring(0, 4);
			funLibrary.validateJsonStructure(response,"mediumsummary_trolley_HD");
		}
		else if(serviceType.equalsIgnoreCase("CC")) {
		ccLocationId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMCollectionPointId' and ORDERS_ID='" + orderId + "'");
		webStoreId = ccLocationId.substring(0, 4);
		funLibrary.validateJSONPathValue_Equals(response, "ccLocationId", ccLocationId);
		funLibrary.validateJsonStructure(response,"mediumsummary_trolley_CC");
		}
		
		else if (serviceType.equalsIgnoreCase("RD")){
			zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMCollectionPointId' and ORDERS_ID='" + orderId + "'");
			webStoreId = zoneId.substring(0, 4);
			funLibrary.validateJsonStructure(response,"mediumsummary_trolley_HD");
		}
		
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		overallMOV =dbUtils.getValues("XORDERATTR", "VALUE", "orders_id="+orderId+" and NAME ='overallMOV'");	
		
		if(overallMOV.contains(".0")) {
			 overallMOV= overallMOV.split("[.]")[0];
		}
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", "2"+storeId.substring(1));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		//funLibrary.validateJSONPathValue_Equals(response, "overallMOV", overallMOV);
		
		String orderItemId ="";
		String partNum ="";
		String qty="";
		
		orderItemId = dbUtils.getValues("ORDERITEMS", "ORDERITEMS_ID", "orders_id="+orderId);
		partNum = dbUtils.getValues("ORDERITEMS", "PARTNUM", "orders_id="+orderId);
		qty = dbUtils.getValues("ORDERITEMS", "QUANTITY", "orders_id="+orderId);
		
	
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].orderItemId", orderItemId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", qty.substring(0, qty.indexOf(".0")));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", partNum);
		
		dbUtils.closeDBConnection();
		
	}
	

	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_ValidRequest(String testname) {
		OrderMediumtWeightSummary_API();

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		Assert.assertEquals(response.getBody().asString().equals(""), true);
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_InvalidRequest(String testname) {
		OrderMediumtWeightSummary_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description="validate schema of response json")
	public void validateSchema_MediumSummary_HD() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response,"mediumsummary_trolley_HD");
	}
	
	@Test(description="validate schema of response json")
	public void validateSchema_MediumSummary_CC() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response,"mediumsummary_trolley_CC");
	}

	
	@Test(description = "Validate Trolley for MultiSaveMultiSKU items")
	public void validateTrolleyMultiSaveMultiSKU() {
		OrderMediumtWeightSummary_API();
		
		String orderId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String overallMOV ="";
		
		orderId = funLibrary.getCurrentOrdersID(FunLibrary.excelData.get("UserID"));
	
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		
			zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMLocationZoneId' and ORDERS_ID='" + orderId + "'");
			webStoreId = zoneId.substring(0, 4);
			funLibrary.validateJsonStructure(response,"mediumsummary_trolley_HD");
		
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		overallMOV =dbUtils.getValues("XORDERATTR", "VALUE", "orders_id="+orderId+" and NAME ='overallMOV'");	
		
		if(overallMOV.contains(".0")) {
			 overallMOV= overallMOV.split("[.]")[0];
		}
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", "2"+storeId.substring(1));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "overallMOV", overallMOV);
		
		String orderItemId ="";
		String partNum ="";
		String qty="";
		
		orderItemId = dbUtils.getValues("ORDERITEMS", "ORDERITEMS_ID", "orders_id="+orderId);
		partNum = dbUtils.getValues("ORDERITEMS", "PARTNUM", "orders_id="+orderId);
		qty = dbUtils.getValues("ORDERITEMS", "QUANTITY", "orders_id="+orderId);
		
	
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[1].orderItemId", orderItemId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[1].qty", qty.substring(0, qty.indexOf(".0")));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[1].partNumber", partNum);
		
		dbUtils.closeDBConnection();
	}
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		OrderMediumtWeightSummary_API();
			restLibrary.getResponseBody(response);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			switch (FunLibrary.excelData.get("StatusCode")) {
			case "200":
				validateMediumWeightSummaryResponse();				
				break;
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
	
	@Test(description = "Validate Medium Summary reponse when user is localised by Suburb and Poscode")
	public void validateTrolleyLocalisedBySuburbPostcode() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
	
		String orderId = funLibrary.getCurrentOrdersID(FunLibrary.excelData.get("UserID"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId+ "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='postcodeOfSPOnlyLocalisation'");
		
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
		dbUtil.closeDBConnection();
	}
	
	@Test(description = "Validate Medium order summary reponse when user is localised by full address")
	public void validateTrolleyLocalisedByFulladdress() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
		
		String orderAddressId =funLibrary.getAddressID(FunLibrary.excelData.get("UserID"), "Home");
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserID"), "Home");
				
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",orderAddressId);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate medium order summary reponse when user is localised by RD Suburb and Poscode")
	public void validateTrolleyLocalisedByRDSuburbPostcode() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
		String orderId = funLibrary.getCurrentOrdersID(FunLibrary.excelData.get("UserID"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId+ "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='postcodeOfSPOnlyLocalisation'");
		
		
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
		dbUtil.closeDBConnection();
	}
	
	@Test(description = "Validate medium order summary reponse when user is localised by RD full address")
	public void validateTrolleyLocalisedByRDFulladdress() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();		
		
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		//String orderAddressId =funLibrary.getAddressID(FunLibrary.excelData.get("UserID"), "RD");
		String orderID_db = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserID") + "') and STATUS='P'");
		String dlAddressId_db =dbUtils.getValues("ORDERITEMS", "ADDRESS_ID", "ORDERS_ID="+orderID_db);
		//String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserID"), "RD");
				
		dbUtils.closeDBConnection();
		//funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",dlAddressId_db);
		funLibrary.Assert.assertAll();
	} 
	/*@Test(description = "Validate Medium Summary response when user has MultiSaveSingleSKU")
	public void validateTrolleyMultiSaveSingleSKU() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("queryParameterKey"), FunLibrary.excelData.get("queryParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		funLibrary.validateJSONPathValue_Equals(response, "overallMOV", FunLibrary.excelData.get("overallMOV"));
		
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].unitPrice", FunLibrary.excelData.get("unitPrice"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].itemTotal", FunLibrary.excelData.get("itemTotal"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].itemSaving", FunLibrary.excelData.get("itemSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].orderItemId", FunLibrary.excelData.get("orderItemId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", FunLibrary.excelData.get("qty"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", FunLibrary.excelData.get("partNumber"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoMinQty", FunLibrary.excelData.get("multibuyPromoMinQty"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoId", FunLibrary.excelData.get("multibuyPromoId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoType", FunLibrary.excelData.get("multibuyPromoType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoReward", FunLibrary.excelData.get("multibuyPromoReward"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyDesc", FunLibrary.excelData.get("multibuyDesc"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}
	@Test(description = "Validate Medium Summary response when user has MultiSaveMultiSKU")
	public void validateTrolleyMultiSaveMultiSKU() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("queryParameterKey"), FunLibrary.excelData.get("queryParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		funLibrary.validateJSONPathValue_Equals(response, "overallMOV", FunLibrary.excelData.get("overallMOV"));
		
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].unitPrice", FunLibrary.excelData.get("unitPrice"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].itemTotal", FunLibrary.excelData.get("itemTotal"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].itemSaving", FunLibrary.excelData.get("itemSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].orderItemId", FunLibrary.excelData.get("orderItemId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", FunLibrary.excelData.get("qty"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", FunLibrary.excelData.get("partNumber"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoMinQty", FunLibrary.excelData.get("multibuyPromoMinQty"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoId", FunLibrary.excelData.get("multibuyPromoId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoType", FunLibrary.excelData.get("multibuyPromoType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyPromoReward", FunLibrary.excelData.get("multibuyPromoReward"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].multibuyDesc", FunLibrary.excelData.get("multibuyDesc"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}*/
	
	@Override
	public String getTestName() {
		return testName.get();
	}

}
