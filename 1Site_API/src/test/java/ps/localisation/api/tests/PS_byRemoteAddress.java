package ps.localisation.api.tests;
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

public class PS_byRemoteAddress extends Base_Class_API implements ITest{
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpaddressid = dbUtil.getValues("xaddress", "CCPADDRESSID", "address_id in (select address_id from address where STATUS='P' and nickname='MyCompanyHoliday' and member_id=(select users_id from users where field1= 'deepaktest@mailinator.com'))");
		String coladdressid = dbUtil.getValues("xaddress", "ADDRESS_ID", "address_id in (select address_id from address where STATUS='P' and nickname='MyCompanyHoliday' and member_id=(select users_id from users where field1= 'deepaktest@mailinator.com'))");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(Payload.rdAddressPaylod(FunLibrary.excelData.get("reqstoreid"), FunLibrary.excelData.get("reqsuburb"), FunLibrary.excelData.get("reqpostcode"), FunLibrary.excelData.get("reqcountry"), FunLibrary.excelData.get("reqverificationid"), ccpaddressid));
		String sub= FunLibrary.excelData.get("suburb");
		System.out.println("Value of suburb="+sub);
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
		case "400":
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
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
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpaddressid);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "validate  changes in database when CCP AddressId for an RD Address corresponds to ColAddressId")
	public void RDAddress_CCPAddressIdCorrespondsToColAddressId() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		String ccpaddressid_db = dbUtil.getValues("xaddress", "CCPADDRESSID", "address_id in (select address_id from address where STATUS='P' and nickname='MyCompanyHoliday' and member_id=(select users_id from users where field1= 'deepaktest@mailinator.com'))");
		String coladdressid_db = dbUtil.getValues("xaddress", "ADDRESS_ID", "address_id in (select address_id from address where STATUS='P' and nickname='MyCompanyHoliday' and member_id=(select users_id from users where field1= 'deepaktest@mailinator.com'))");
		String validationid_db = dbUtil.getValues("xaddress", "VALIDATIONID", "address_id in (select address_id from address where STATUS='P' and nickname='MyCompanyHoliday' and member_id=(select users_id from users where field1= 'deepaktest@mailinator.com'))");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(Payload.rdAddressPaylod(FunLibrary.excelData.get("reqstoreid"), FunLibrary.excelData.get("reqsuburb"), FunLibrary.excelData.get("reqpostcode"), FunLibrary.excelData.get("reqcountry"), validationid_db, ccpaddressid_db));
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
			funLibrary.validateJSONPathValue_Equals(response, "colAddressId", coladdressid_db);
			funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", ccpaddressid_db);
			funLibrary.validateJSONPathValue_Equals(response, "latitude", FunLibrary.excelData.get("latitude"));
			funLibrary.validateJSONPathValue_Equals(response, "longitude", FunLibrary.excelData.get("longitude"));
		}
		funLibrary.Assert.assertAll();
		String rdAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id=" + order_id + " and NAME='rdAddressId'");
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='ccpAddressId'");
		testLog.info("Expected - rdAddressId : "+coladdressid_db+"");
		testLog.info("Actual - rdAddressId : " + rdAddressId);
		Assert.assertEquals(rdAddressId, coladdressid_db, "OrderAddressId is not matching");
		testLog.info("Expected - ccpAddressId : "+ccpaddressid_db+"");
		testLog.info("Actual - ccpAddressId : " + ccpAddressId);
		Assert.assertEquals(ccpAddressId, ccpaddressid_db, "ccpAddressId is not matching");
		funLibrary.Assert.assertAll();	
	}
	
	@Test(description = "validate  changes in database when CCP AddressId for an RD Address does not corresponds to ColAddressId")
	public void RDAddres_HD_CCPAddressIdDoesNotCorrespondsToColAddressId() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(Payload.rdAddressPaylod(FunLibrary.excelData.get("reqstoreid"), FunLibrary.excelData.get("reqsuburb"), FunLibrary.excelData.get("reqpostcode"), FunLibrary.excelData.get("reqcountry"), FunLibrary.excelData.get("reqverificationid"), FunLibrary.excelData.get("reqccpaddressId")));
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
		String ccpAddressId = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + order_id + "' and NAME='ccpAddressId'");
		System.out.println(rdAddressId);
		testLog.info("Expected - rdAddressId : "+FunLibrary.excelData.get("colAddressId"));
		testLog.info("Actual - rdAddressId : " + rdAddressId);
		Assert.assertEquals(rdAddressId,FunLibrary.excelData.get("colAddressId"));
		testLog.info("Expected - ccpAddressId : "+FunLibrary.excelData.get("ccpAddressId")+"");
		testLog.info("Actual - ccpAddressId : " + ccpAddressId);
		Assert.assertEquals(ccpAddressId, FunLibrary.excelData.get("ccpAddressId"), "ccpAddressId is not matching");
		funLibrary.Assert.assertAll();	
	}


	@Override
	public String getTestName() {
		return testName.get();
	}
	
	

}
