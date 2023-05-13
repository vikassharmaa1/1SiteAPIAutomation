package wcs.localisation.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_ByRDAddress extends Base_Class_API implements ITest {
	
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
		String ccpaddressid = dbUtil.getValues("xaddress", "CCPADDRESSID", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String coladdressid = dbUtil.getValues("xaddress", "ADDRESS_ID", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
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
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), ccpaddressid);
		Response response = restLibrary.executeAPI();
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
			funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", coladdressid);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", FunLibrary.excelData.get("ccpAddressId"));
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
	}
	@Test(description = "validate  changes when rdAddressID mapped to colAddressId for RD location")
	public void RDAddress_RDAddressIdCorrespondsToColAddressId() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'b2c.customer@getnada.com') and status='P'");
		String ccpaddressid_db = dbUtil.getValues("xaddress", "CCPADDRESSID", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String coladdressid_db = dbUtil.getValues("xaddress", "ADDRESS_ID", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String validationid_db = dbUtil.getValues("xaddress", "VALIDATIONID", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String longitude_db = dbUtil.getValues("xaddress", "LONGITUDE", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String latitude_db = dbUtil.getValues("xaddress", "LATITUDE", "address_id in (select address_id from address where STATUS='P' and nickname='RD' and member_id=(select users_id from users where field1= 'b2c.customer@getnada.com'))");
		String postcode_db=dbUtil.getValues("address","ZIPCODE","member_id=(select users_id from users where field1= 'b2c.customer@getnada.com')and nickname='RD'");
		String country_db=dbUtil.getValues("address","COUNTRY","member_id=(select users_id from users where field1= 'b2c.customer@getnada.com')and nickname='RD'");
		String suburb_db=dbUtil.getValues("address","CITY","member_id=(select users_id from users where field1= 'b2c.customer@getnada.com')and nickname='RD'");
		String state_db=dbUtil.getValues("address","STATE","member_id=(select users_id from users where field1= 'b2c.customer@getnada.com')and nickname='RD'");
		String servicetype_db=dbUtil.getValues("xorderattr","VALUE","orders_id=(select orders_id from orders\r\n" + 
				"where member_id=(select users_id from users where field1= 'b2c.customer@getnada.com') \r\n" + 
				"and status='P') and name='DMServiceType'");
		
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
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey7"), ccpaddressid_db);
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "country", country_db);
			funLibrary.validateJSONPathValue_Equals(response, "postcode", postcode_db);
			funLibrary.validateJSONPathValue_Equals(response, "suburb", suburb_db);
			funLibrary.validateJSONPathValue_Equals(response, "state", state_db);
			funLibrary.validateJSONPathValue_Equals(response, "serviceType", servicetype_db);
			funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
			funLibrary.validateJSONPathValue_Equals(response, "catalogId", FunLibrary.excelData.get("catalogId"));
			funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("webstoreId"));
			funLibrary.validateJSONPathValue_Equals(response, "storeId", FunLibrary.excelData.get("storeId"));
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", coladdressid_db);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpaddressid_db);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
			}
		funLibrary.Assert.assertAll();
		String rdAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id=" + order_id + " and NAME='rdAddressId'");
		funLibrary.validate_Equals("rdAddress",coladdressid_db,rdAddressId);
		funLibrary.Assert.assertAll();	
	}
	
	
	@Test(description = "validate  changes in database when CCP AddressId for an RD Address does not corresponds to ColAddressId")
	public void RDAddress_RDAddressDoesNotCorrespondsToColAddressId() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'b2c.customer@getnada.com') and status='P'");
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
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", FunLibrary.excelData.get("ccpAddressId"));
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
		
		String rdAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id=" + order_id + " and NAME='rdAddressId'");
		testLog.info("Expected - rdAddressId : "+FunLibrary.excelData.get("colAddressId"));
		testLog.info("Actual - rdAddressId : " + rdAddressId);
		Assert.assertEquals(rdAddressId,FunLibrary.excelData.get("colAddressId"));
		funLibrary.Assert.assertAll();	
	}



	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
