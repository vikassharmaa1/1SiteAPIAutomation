package wcs.orderdetails.api.tests;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;

public class WCS_orderDetails extends Base_Class_API implements ITest{
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
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
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
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
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate the available count in response matches with db value")
	public void validateOrderAttribute() throws SQLException {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		String order_id= response.jsonPath().getString("orderId");
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.dmDeliveryWindowType", getDbValue(order_id, "DMDeliveryWindowType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.orderFlybuyBarcode", getDbValue(order_id, "OrderFlyBuysNumber"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.orderBaggingPreference", getDbValue(order_id, "OrderBaggingPreference"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.timeZone", getDbValue(order_id, "DMTimezone"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.dmWindowType", getDbValue(order_id, "DMWindowType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.dmServiceType", getDbValue(order_id, "DMServiceType"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.dmDeliverySlotBaggingOptions", getDbValue(order_id, "DMDeliverySlotBaggingOptions"));
		funLibrary.validateJSONPathValue_Equals(response, "orderAttributesMap.orderStaffDiscountNumber", getDbValue(order_id, "OrderStaffDiscountNumber"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "Validate TrollyTotals")
	public void validateTrolly() throws SQLException {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		String order_id= response.jsonPath().getString("orderId");
		System.out.println("Order id="+order_id);
		System.out.println(response.jsonPath().getFloat("trolleyTotals.shippingCharge"));
		Double itemtotal= response.jsonPath().getDouble("trolleyTotals.bagItemsTotal")+response.jsonPath().getDouble("trolleyTotals.trolleySubtotal");
		Double finaltotal= response.jsonPath().getDouble("trolleyTotals.shippingCharge")+itemtotal;
		System.out.println("Final Total=" +finaltotal);
		/*funLibrary.validateJSONPathValue_Equals(response, String.valueOf(itemtotal), dbUtil.getValues("orders", "TOTALPRODUCT", "orders_id='"+order_id+"'"));
		funLibrary.validateJSONPathValue_Equals(response, "trolleyTotals.shippingCharge", dbUtil.getValues("orders", "TOTALSHIPPING", "orders_id='"+order_id+"'"));*/
		funLibrary.validateJSONPathValue_Equals(response, "trolleyTotals.grandTotal", String.valueOf(finaltotal));
		funLibrary.Assert.assertAll();
	}
	@Test(description = "Validate the order address details are matching to DB")
	public void validateOrderAddress() throws SQLException {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			String order_id= response.jsonPath().getString("orderId");
			String address_id= getDbValue(order_id, "OrderAddressId");
			funLibrary.validateJSONPathValue_Equals(response, "address.lastName", getAddressValue(address_id, "LASTNAME"));
			funLibrary.validateJSONPathValue_Equals(response, "address.country", getAddressValue(address_id, "COUNTRY"));
			funLibrary.validateJSONPathValue_Equals(response, "address.nickName.", getAddressValue(address_id, "NICKNAME"));
			funLibrary.validateJSONPathValue_Equals(response, "address.addressType", getAddressValue(address_id, "ADDRESSTYPE").trim());
			funLibrary.validateJSONPathValue_Equals(response, "address.postcode", getAddressValue(address_id, "ZIPCODE"));
			funLibrary.validateJSONPathValue_Equals(response, "address.addressId", getAddressValue(address_id, "ADDRESS_ID"));
			funLibrary.validateJSONPathValue_Equals(response, "address.phone1", getAddressValue(address_id, "PHONE1"));
			funLibrary.validateJSONPathValue_Equals(response, "address.phone1Type", getAddressValue(address_id, "PHONE1TYPE"));
			funLibrary.validateJSONPathValue_Equals(response, "address.firstName", getAddressValue(address_id, "FIRSTNAME"));
			funLibrary.validateJSONPathValue_Equals(response, "address.email1", getAddressValue(address_id, "EMAIL1"));
			funLibrary.validateJSONPathValue_Equals(response, "address.state", getAddressValue(address_id, "STATE"));
			funLibrary.validateJSONPathValue_Equals(response, "address.suburb", getAddressValue(address_id, "CITY"));
	        funLibrary.Assert.assertAll();
	}
	
	public  String getDbValue(String orderid, String name)
	{
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String value = dbUtil.getValues("xorderattr", "value", "orders_id='"+orderid+"' and name='"+name+"'");
		System.out.println("My Value= " +value);
		if(value.equalsIgnoreCase("No Record Found"))
		{
			value="";
		}
		return value;
		
	}
	
	public  String getAddressValue(String address_id, String field)
	{
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String addressvalue = dbUtil.getValues("address", ""+field+"", "address_id='"+address_id+"'");
		return addressvalue;
		
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
	
	

}
