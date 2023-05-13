package apim.localisation.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class APIM_ByAddress extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "statusCode", FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("ErrorMessage"));
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
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate whether the database is updated, when address is changed for user")
	public void changeHDAddress() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("Country"));
		funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("PostCode"));
		funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("Suburb"));
		funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("State"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("OrderId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "colAddressId", FunLibrary.excelData.get("coladdressid"));
		funLibrary.Assert.assertAll();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String DMLocationZoneId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("OrderId") + "' and NAME='DMLocationZoneId'");
		String DMServiceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("OrderId") + "' and NAME='DMServiceType'");
		String field1 = dbUtil.getValues("USERPROF", "FIELD1", "users_id='555103527'");
		String webStoreId = dbUtil.getValues("xstoreffmcatrel", "FFMEXTID", "catalog_id='" + FunLibrary.excelData.get("catalogId") + "'");
		testLog.info("Expected - DMLocationZoneId : 4835HD");
		testLog.info("Actual - DMLocationZoneId : " + DMLocationZoneId);
		Assert.assertEquals(DMLocationZoneId, "4835HD", "DMLocationZoneId is not matching");
		testLog.info("Expected - DMServiceType : HD");
		testLog.info("Actual - DMServiceType : " + DMServiceType);
		Assert.assertEquals(DMServiceType, "HD", "DMServiceType is not matching");
		testLog.info("Expected - field1 : 10501");
		testLog.info("Actual - field1 : " + field1);
		Assert.assertEquals(field1, "10501", "field1 is not matching");
		testLog.info("Expected - webStoreId : 4835");
		testLog.info("Actual - webStoreId : " + webStoreId);
		Assert.assertEquals(webStoreId, "4835", "webStoreId is not matching");
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate whether the database is updated, when new address is added for user")
	public void addNewHDAddress() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("Country"));
		funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("PostCode"));
		funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("Suburb"));
		funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("State"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("OrderId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "colAddressId", FunLibrary.excelData.get("coladdressid"));
		funLibrary.Assert.assertAll();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String DMLocationZoneId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("OrderId") + "' and NAME='DMLocationZoneId'");
		String DMServiceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("OrderId") + "' and NAME='DMServiceType'");
		String field1 = dbUtil.getValues("USERPROF", "FIELD1", "users_id='555103527'");
		String webStoreId = dbUtil.getValues("xstoreffmcatrel", "FFMEXTID", "catalog_id='" + FunLibrary.excelData.get("catalogId") + "'");
		testLog.info("Expected - DMLocationZoneId : 4390HD");
		testLog.info("Actual - DMLocationZoneId : " + DMLocationZoneId);
		Assert.assertEquals(DMLocationZoneId, "4390HD", "DMLocationZoneId is not matching");
		testLog.info("Expected - DMServiceType : HD");
		testLog.info("Actual - DMServiceType : " + DMServiceType);
		Assert.assertEquals(DMServiceType, "HD", "DMServiceType is not matching");
		testLog.info("Expected - field1 : 12698");
		testLog.info("Actual - field1 : " + field1);
		Assert.assertEquals(field1, "12698", "field1 is not matching");
		testLog.info("Expected - webStoreId : 4390");
		testLog.info("Actual - webStoreId : " + webStoreId);
		Assert.assertEquals(webStoreId, "4390", "webStoreId is not matching");
		funLibrary.Assert.assertAll();

	}

	@Override
	public String getTestName() {

		return testName.get();
	}
}
