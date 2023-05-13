package wcs.trolley.api.tests;

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

public class WCS_MediumSummary extends Base_Class_API implements ITest {

	
	Response response = null;
	RestLibrary restLibrary = null;

	public void OrderMediumtWeightSummary_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.OrderMediumWeightSummary_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"),FunLibrary.excelData.get("QueryParam_ver"));
		//restLibrary.getResponseBody(response);
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
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = FunLibrary.excelData.get("orderId") ;
		
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
		

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", "2"+storeId.substring(1));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		
		String orderItemId ="";
		String partNum ="";
		String qty="";
		
		if (response.jsonPath().getJsonObject("orderItems[0].orderItemId") != null) {
		orderItemId = dbUtils.getValues("ORDERITEMS", "ORDERITEMS_ID", "orders_id="+orderId);
		partNum = dbUtils.getValues("ORDERITEMS", "PARTNUM", "orders_id="+orderId);
		qty = dbUtils.getValues("ORDERITEMS", "QUANTITY", "orders_id="+orderId);

		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].orderItemId", orderItemId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].qty", qty.substring(0, qty.indexOf(".0")));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", partNum);
		}
		dbUtils.closeDBConnection();
		
	}
	
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_ValidRequest(String testname) {
		OrderMediumtWeightSummary_API();

		String reasonCode = FunLibrary.excelData.get("ReasonCode");
		String description = FunLibrary.excelData.get("Description");
		String code = "ERROR";

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "code", code);
		funLibrary.validateJSONPathValue_Equals(response, "description", description);
		funLibrary.validateJSONPathValue_Equals(response, "reasonCode", reasonCode);
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_InvalidRequest(String testname) {
		OrderMediumtWeightSummary_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
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

	
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			validateMediumWeightSummaryResponse();
			
			
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
			String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
			dbUtil.closeDBConnection();
			
			
			funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
			funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
			
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
	
	@Test(description = "Validate that reponse do not haveservice type when trolley is empty")
	public void validateEmptyTrolley() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		dbUtil.closeDBConnection();
		
		
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "totalQty","0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving","0");
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal","0");
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping","0.0");
		
		
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate Medium Summary reponse when user is localised by Suburb and Poscode")
	public void validateTrolleyLocalisedBySuburbPostcode() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
	
			
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		
		
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
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='ccpAddressId'");
		String orderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='OrderAddressId'");
		
		
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",orderAddressId);
		funLibrary.Assert.assertAll();
		dbUtil.closeDBConnection();
	}
	
	@Test(description = "Validate medium order summary reponse when user is localised by RD Suburb and Poscode")
	public void validateTrolleyLocalisedByRDSuburbPostcode() {
		OrderMediumtWeightSummary_API();
		restLibrary.getResponseBody(response);
		validateMediumWeightSummaryResponse();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		
		
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
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='ccpAddressId'");
		String orderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='rdAddressId'");
		
		
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",orderAddressId);
		funLibrary.Assert.assertAll();
		dbUtil.closeDBConnection();
	} 

	@Override
	public String getTestName() {
		return testName.get();
	}
}
