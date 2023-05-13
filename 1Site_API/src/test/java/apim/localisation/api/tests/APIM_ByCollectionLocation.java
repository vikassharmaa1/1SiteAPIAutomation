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

@Test
public class APIM_ByCollectionLocation extends Base_Class_API implements ITest {

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

	@Test(description = "validate whether the database is updated, when user changes collection Location")
	public void changeLocation() {
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
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "locationId", FunLibrary.excelData.get("locationid"));
		funLibrary.Assert.assertAll();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String DMCollectionPointId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMCollectionPointId'");
		String DMServiceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		String field1 = dbUtil.getValues("USERPROF", "FIELD1", "users_id='555103527'");
		String webStoreId = dbUtil.getValues("xstoreffmcatrel", "FFMEXTID", "catalog_id='" + FunLibrary.excelData.get("catalogId") + "'");
		testLog.info("Expected - DMCollectionPointId : 0748SD0748");
		testLog.info("Actual - DMCollectionPointId : " + DMCollectionPointId);
		Assert.assertEquals(DMCollectionPointId, "0748SD0748", "DMCollectionPointId is not matching");
		testLog.info("Expected - DMServiceType : CC");
		testLog.info("Actual - DMServiceType : " + DMServiceType);
		Assert.assertEquals(DMServiceType, "CC", "DMServiceType is not matching");
		testLog.info("Expected - field1 : 16103");
		testLog.info("Actual - field1 : " + field1);
		Assert.assertEquals(field1, "16103", "field1 is not matching");
		testLog.info("Expected - webStoreId : 0748");
		testLog.info("Actual - webStoreId : " + webStoreId);
		Assert.assertEquals(webStoreId, "0748", "webStoreId is not matching");
		funLibrary.Assert.assertAll();

	}

	@Test(description = "validate whether the database is updated, when user selects a collection Location")
	public void selectLocation() {
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
		funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "locationId", FunLibrary.excelData.get("locationid"));
		funLibrary.Assert.assertAll();
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String DMCollectionPointId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMCollectionPointId'");
		String DMServiceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMServiceType'");
		String field1 = dbUtil.getValues("USERPROF", "FIELD1", "users_id='555103527'");
		String webStoreId = dbUtil.getValues("xstoreffmcatrel", "FFMEXTID", "catalog_id='" + FunLibrary.excelData.get("catalogId") + "'");
		testLog.info("Expected - DMCollectionPointId : 0884CC0884");
		testLog.info("Actual - DMCollectionPointId : " + DMCollectionPointId);
		Assert.assertEquals(DMCollectionPointId, "0884CC0884", "DMCollectionPointId is not matching");
		testLog.info("Expected - DMServiceType : CC");
		testLog.info("Actual - DMServiceType : " + DMServiceType);
		Assert.assertEquals(DMServiceType, "CC", "DMServiceType is not matching");
		testLog.info("Expected - field1 : 9553");
		testLog.info("Actual - field1 : " + field1);
		Assert.assertEquals(field1, "9553", "field1 is not matching");
		testLog.info("Expected - webStoreId : 0884");
		testLog.info("Actual - webStoreId : " + webStoreId);
		Assert.assertEquals(webStoreId, "0884", "webStoreId is not matching");
		funLibrary.Assert.assertAll();

	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
