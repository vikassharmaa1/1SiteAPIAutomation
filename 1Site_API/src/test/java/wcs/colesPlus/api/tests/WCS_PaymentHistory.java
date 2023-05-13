package wcs.colesPlus.api.tests;



import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import org.junit.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class WCS_PaymentHistory extends Base_Class_API implements ITest{

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
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
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	/*
	@Test(description = "AC01- Verify account details for colus plus subscription payments")
	public void validateAccountDetailsColesPlusSubscription() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		JsonPath jsonPath = response.jsonPath();
		
		String ChildOrderId_Db = dbUtil.getValues("orderrel", "CHILDORDER_ID", "parentorder_id='" + FunLibrary.excelData.get("Parentorder_Id") + "' order by CHILDORDER_ID DESC");
				
		String subscriptionId_Db = dbUtil.getValues("subscription", "SUBSCRIPTION_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String subscriptionId_Json = jsonPath.getString("subscriptions[0].subscriptionId");		
		funLibrary.validate_Equals("subscriptionId", subscriptionId_Db, subscriptionId_Json);
		
		
		String description_Db = dbUtil.getValues("subscription", "DESCRIPTION", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String description_Json = jsonPath.getString("subscriptions[0].description");		
		funLibrary.validate_Equals("description", description_Db, description_Json);
		
		
		double amount_Db = Double.parseDouble(dbUtil.getValues("subscription", "TOTALCOST", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'"));
		double amount_Json = Double.parseDouble(jsonPath.getString("subscriptions[0].childOrders[0].amount"));	
		Assert.assertEquals("Compare amount", amount_Db, amount_Json, 1);
		
		
		String recurringOrderId_Db = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("Member_Id") + "'");
		String recurringOrderId_Json = jsonPath.getString("subscriptions[0].recurringOrderId");		
		funLibrary.validate_Equals("recurringOrderId", recurringOrderId_Db, recurringOrderId_Json);
		
		
		String paymetnStatus_Db = dbUtil.getValues("orders", "STATUS", "orders_id='" + FunLibrary.excelData.get("Orders_Id") + "'");
		String paymetnStatus_Json = jsonPath.getString("subscriptions[0].childOrders[0].paymentStatus");
		
		
		//String ChildOrderId_Db = dbUtil.getValues("orderrel", "CHILDORDER_ID", "parentorder_id='" + FunLibrary.excelData.get("Parentorder_Id") + "'");
		String Id_Json = jsonPath.getString("subscriptions[0].childOrders[0].id");		
		funLibrary.validate_Equals("Id", ChildOrderId_Db, Id_Json);
		
		
		if(paymetnStatus_Db.equals("S") && paymetnStatus_Json.equalsIgnoreCase("Successful")) {
			
			funLibrary.Assert.assertTrue(true, "Status matched");
			funLibrary.testLog.info("Status matched");
			
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Status mismatched");
			funLibrary.testLog.info("Status mismatched");
		}
		
		
		
		
		String datePlaced_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("orders", "TIMEPLACED", "orders_id='" + FunLibrary.excelData.get("Orders_Id") + "'"));
		String datePlaced_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].childOrders[0].datePlaced"));		
		funLibrary.validate_Equals("datePlaced", datePlaced_Db, datePlaced_Json);
		
		
		String lastUpdated_Db = funLibrary.storeDateBeforeWhitespace(dbUtil.getValues("orders", "LASTUPDATE", "orders_id='" + FunLibrary.excelData.get("Orders_Id") + "'"));
		String lastUpdated_Json = funLibrary.storeDateBeforeSeperation_T(jsonPath.getString("subscriptions[0].childOrders[0].invoices[0].lastUpdate"));		
		funLibrary.validate_Equals("lastUpdated", lastUpdated_Db, lastUpdated_Json);
		
		dbUtil.closeDBConnection();
		
		//datePaymentProcessed
		
		DatabaseUtilities dbUtilCol = new DatabaseUtilities("COLMedia");
		String fileName_Db = dbUtilCol.getValues("colinvoices", "FILENAME", "ORDER_ID='" + FunLibrary.excelData.get("Orders_Id") + "'");
		String fileName_Json = jsonPath.getString("subscriptions[0].childOrders[0].invoices[0].fileName");
		funLibrary.validate_Equals("fileName", fileName_Db, fileName_Json);
		
		
		String format_Json = jsonPath.getString("subscriptions[0].childOrders[0].invoices[0].format");
		if(fileName_Db.contains(format_Json)) {
			
			funLibrary.Assert.assertTrue(true, "pdf matched");
			funLibrary.testLog.info("pdf matched");
			
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "pdf mismatched");
			funLibrary.testLog.info("pdf mismatched");
		}
		
		
		String type_Db = dbUtilCol.getValues("colinvoices", "TYPE", "ORDER_ID='" + FunLibrary.excelData.get("Orders_Id") + "'");
		String type_Json = jsonPath.getString("subscriptions[0].childOrders[0].invoices[0].type");
		
		if(type_Db.equalsIgnoreCase("a") && type_Json.equals("Invoice")) {
			
			funLibrary.Assert.assertTrue(true, "Invoice type matched");
			funLibrary.testLog.info("Invoice type matched");
			
		}
		
		else {
			funLibrary.Assert.assertTrue(false, "Invoice type mismatched");
			funLibrary.testLog.info("Invoice type mismatched");
		}
		
		
		funLibrary.Assert.assertAll();
		
	}

*/

	
	@Test(description = "AC03- Verify account details for non colus plus member")
	public void validateAccountDetailsNonColesPlusMember() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		Assert.assertEquals(response.getBody().asString().equals(FunLibrary.excelData.get("Response")), true);
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	
	
	
	

	

	

	@Override
	public String getTestName() {
		return testName.get();

	}

}
