package wcs.localisation.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_byFullAddress extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), FunLibrary.excelData.get("PathParameterValue"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey3"), FunLibrary.excelData.get("QueryParameterValue3"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey4"), FunLibrary.excelData.get("QueryParameterValue4"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey5"), FunLibrary.excelData.get("QueryParameterValue5"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey6"), FunLibrary.excelData.get("QueryParameterValue6"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), FunLibrary.excelData.get("QueryParameterValue7"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "'reasonCode'", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "'description'", FunLibrary.excelData.get("Description"));
			break;
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), FunLibrary.excelData.get("PathParameterValue"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey3"), FunLibrary.excelData.get("QueryParameterValue3"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey4"), FunLibrary.excelData.get("QueryParameterValue4"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey5"), FunLibrary.excelData.get("QueryParameterValue5"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey6"), FunLibrary.excelData.get("QueryParameterValue6"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), FunLibrary.excelData.get("QueryParameterValue7"));
		Response response = restLibrary.executeAPI();
		String OrderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='OrderAddressId'");
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='ccpAddressId'");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
		case "400":
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		break;
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("country"));
			funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("postcode"));
			funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("suburb"));
			funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("state"));
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
			funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", OrderAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
	}
	@Test(description = "validate Address changes in database when CCP AddressId for an HD Address corresponds to ColAddressId")
	public void HDAddress_CCPAddressIdCorrespondsToColAddressId() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), FunLibrary.excelData.get("PathParameterValue"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey3"), FunLibrary.excelData.get("QueryParameterValue3"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey4"), FunLibrary.excelData.get("QueryParameterValue4"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey5"), FunLibrary.excelData.get("QueryParameterValue5"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey6"), FunLibrary.excelData.get("QueryParameterValue6"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), FunLibrary.excelData.get("QueryParameterValue7"));
		Response response = restLibrary.executeAPI();
		String OrderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='OrderAddressId'");
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='ccpAddressId'");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("country"));
			funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("postcode"));
			funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("suburb"));
			funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("state"));
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
			funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", OrderAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
		testLog.info("Expected - OrderAddressId :" +funLibrary.getJsonPathValue(response, "colAddressId"));
		testLog.info("Actual - OrderAddressId : " + OrderAddressId);
		Assert.assertEquals(OrderAddressId, funLibrary.getJsonPathValue(response, "colAddressId"), "OrderAddressId is not matching");
		testLog.info("Expected - ccpAddressId : " +funLibrary.getJsonPathValue(response, "ccpAddressId"));
		testLog.info("Actual - ccpAddressId : " + ccpAddressId);
		Assert.assertEquals(ccpAddressId, funLibrary.getJsonPathValue(response, "ccpAddressId"), "ccpAddressId is not matching");
		funLibrary.Assert.assertAll();	
	}
	@Test(description = "R1 Updates : validate changes to Localisation by Collection Location for an HD Address")
	public void HDAddressThatCorrespondsToColAddressId() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), FunLibrary.excelData.get("PathParameterValue"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey3"), FunLibrary.excelData.get("QueryParameterValue3"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey4"), FunLibrary.excelData.get("QueryParameterValue4"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey5"), FunLibrary.excelData.get("QueryParameterValue5"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey6"), FunLibrary.excelData.get("QueryParameterValue6"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), FunLibrary.excelData.get("QueryParameterValue7"));
		Response response = restLibrary.executeAPI();
		String OrderAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='OrderAddressId'");
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='ccpAddressId'");
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("country"));
			funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("postcode"));
			funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("suburb"));
			funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("state"));
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", FunLibrary.excelData.get("serviceType"));
			funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", OrderAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpAddressId);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
		testLog.info("Expected - OrderAddressId :" +funLibrary.getJsonPathValue(response, "colAddressId"));
		testLog.info("Actual - OrderAddressId : " + OrderAddressId);
		Assert.assertEquals(OrderAddressId, funLibrary.getJsonPathValue(response, "colAddressId"), "OrderAddressId is not matching");
		testLog.info("Expected - ccpAddressId : " +funLibrary.getJsonPathValue(response, "ccpAddressId"));
		testLog.info("Actual - ccpAddressId : " + ccpAddressId);
		Assert.assertEquals(ccpAddressId, funLibrary.getJsonPathValue(response, "ccpAddressId"), "ccpAddressId is not matching");
		funLibrary.Assert.assertAll();	
	}
	@Test(description = "R1 Updates : validate changes to Localisation by Collection Location for an HD Address")
	public void changeLocation() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey"), FunLibrary.excelData.get("PathParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "country", FunLibrary.excelData.get("country"));
		funLibrary.validateJSONPathValue_Equals(response, "postcode", FunLibrary.excelData.get("postcode"));
		funLibrary.validateJSONPathValue_Equals(response, "suburb", FunLibrary.excelData.get("suburb"));
		funLibrary.validateJSONPathValue_Equals(response, "state", FunLibrary.excelData.get("state"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" +order_id + "' and NAME='DMServiceType'"));
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
		funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
		funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		funLibrary.validateJSONPathValue_Equals(response, "locationId", dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='DMCollectionPointId'"));
		funLibrary.Assert.assertAll();
		String DMCollectionPointId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='DMCollectionPointId'");
		String DMServiceType = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" +order_id + "' and NAME='DMServiceType'");
		testLog.info("Expected - DMCollectionPointId :"+funLibrary.getJsonPathValue(response, "locationId"));
		testLog.info("Actual - DMCollectionPointId : " + DMCollectionPointId);
		Assert.assertEquals(DMCollectionPointId, funLibrary.getJsonPathValue(response, "locationId"), "DMCollectionPointId is not matching");
		testLog.info("Expected - DMServiceType :" +funLibrary.getJsonPathValue(response, "serviceType"));
		testLog.info("Actual - DMServiceType : " + DMServiceType);
		Assert.assertEquals(DMServiceType, funLibrary.getJsonPathValue(response, "serviceType"), "DMServiceType is not matching");
		funLibrary.Assert.assertAll();
	}

	@Override
	public String getTestName() {
		return testName.get();

	}
}
