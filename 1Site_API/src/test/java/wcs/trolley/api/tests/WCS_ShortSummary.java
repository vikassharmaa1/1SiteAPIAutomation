package wcs.trolley.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_ShortSummary extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
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
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
			funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
			funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
			funLibrary.validateJSONPathValue_Equals(response, "serviceType",FunLibrary.excelData.get("serviceType"));
			funLibrary.validateJSONPathValue_Equals(response, "ccLocationId",FunLibrary.excelData.get("ccLocationId"));
			break;
		case "401":
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Validate that reponse when trolley is empty")
	public void validateEmptyTrolley() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
		funLibrary.validateJSONPathValue_Equals(response, "ccLocationId", FunLibrary.excelData.get("ccLocationId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate that reponse when user is localised by Suburb and Poscode")
	public void validateTrolleyLocalisedBySuburbPostcode() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate that reponse when user is localised by full address")
	public void validateTrolleyLocalisedByFulladdress() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='ccpAddressId'");
		String orderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='OrderAddressId'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",orderAddressId);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate that reponse when user is localised by RD Suburb and Poscode")
	public void validateTrolleyLocalisedByRDSuburbPostcode() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String suburbOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='suburbOfSPOnlyLocalisation'");
		String postcodeOfSPOnlyLocalisation = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='postcodeOfSPOnlyLocalisation'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation",suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation",postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate that reponse when user is localised by RD full address")
	public void validateTrolleyLocalisedByRDFulladdress() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderShipping", FunLibrary.excelData.get("orderShipping"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSaving", FunLibrary.excelData.get("orderSaving"));
		funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", FunLibrary.excelData.get("orderSubTotal"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='ccpAddressId'");
		String orderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='rdAddressId'");
		String serviceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		funLibrary.validateJSONPathValue_Equals(response, "serviceType",serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId",ccpAddressId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId",orderAddressId);
		funLibrary.Assert.assertAll();
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
}
