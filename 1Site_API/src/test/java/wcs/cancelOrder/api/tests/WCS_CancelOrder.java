package wcs.cancelOrder.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.api.WCS;
import com.rest.body.Payload;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_CancelOrder extends Base_Class_API implements ITest {
	Response response;
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class,priority = 8)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("Access-Token", FunLibrary.excelData.get("Header_access-token"));
		restLibrary.addHeader("user-jwt-token", FunLibrary.excelData.get("Header_user-jwt-token"));
		restLibrary.addPathParameter("storeId", FunLibrary.excelData.get("storeIds"));
		restLibrary.addQueryParameter("orderId", FunLibrary.excelData.get("orderId"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'",
					FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class,priority = 7)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("Access-Token", FunLibrary.excelData.get("Header_access-token"));
		restLibrary.addHeader("user-jwt-token", FunLibrary.excelData.get("Header_user-jwt-token"));
		restLibrary.addPathParameter("storeId", FunLibrary.excelData.get("storeIds"));
		restLibrary.addQueryParameter("orderId", FunLibrary.excelData.get("orderId"));
     	Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(priority = 0, description = "AC01- Verify order get cancelled for HD Orders")
	public void validateOrderisCancelled_HD() throws InterruptedException {
		String AccessToken = FunLibrary.excelData.get("Header_access-token");
		String userjwttoken = FunLibrary.excelData.get("Header_user-jwt-token");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String ver = FunLibrary.excelData.get("ver");
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		System.out.println("slotid" + " " + FunLibrary.excelData.get("collectionPointId"));
		System.out.println("slotid" + " " + slotId);
		dbUtil_DM.closeDBConnection();
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserName"),FunLibrary.excelData.get("NickName"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		Payload.localizationBySuburb(AccessToken,userjwttoken, "0404","0820", "BAYVIEW");
		APILibrary.addUpdateTrolley_WCS_1("", "", "", AccessToken,userjwttoken, "0404", "5", "5833943");
		APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		//String identifier = APILibrary.getMPGSCardToken("5123450000000008", "01", "25", "100");
		String identifier = PS.getMPGSCardToken("4111111111111111", "12", "25", "123");

		RestAssured.reset();
		APILibrary.savePaymentMethod_WCS_1("", "", "",AccessToken,userjwttoken, "0404", "card", "1.1", "true", identifier);
		APILibrary.orderProcess_WCS("", "", "",AccessToken ,userjwttoken	, "0404", "1");
		Response ordersubmit = APILibrary.orderSubmit_WCS_1("", "", "", AccessToken,userjwttoken, "0404", "1.1", "", "");
		JsonPath jsonPath = ordersubmit.jsonPath();
		String orderId = jsonPath.getString("orderId");
		System.out.println(orderId);
		Thread.sleep(20000);
		response = APILibrary.cancelOrder_WCS("", "", "",AccessToken,userjwttoken,"0404",orderId);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId",orderId);
		String db_order = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + orderId + "'");
		funLibrary.validate_Equals("db_status", db_order, "X");

	}

	/*@Test(priority = 2, description = "AC01- Verify order get cancelled for HD Orders",dependsOnMethods = { "validateOrderisCancelled_HD" })
	public void validateOrderisCancelled_HD1() throws InterruptedException {
		String AccessToken = FunLibrary.excelData.get("Header_access-token");
		String userjwttoken = FunLibrary.excelData.get("Header_user-jwt-token");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String ver = FunLibrary.excelData.get("ver");
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order1 = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='M'  order by timeplaced " );
		System.out.println("final"+""+db_order1);
		response = APILibrary.cancelOrder_WCS("", "", "",AccessToken,userjwttoken,"0404",db_order1);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId",db_order1);
		String db_order = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + db_order1 + "'");
		funLibrary.validate_Equals("db_status", db_order, "X");
		  if(response.statusCode()==200)
			{
			  response =  APILibrary.cancelOrder_WCS("", "", "",AccessToken,userjwttoken,"0404",db_order1);
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
						FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
						FunLibrary.excelData.get("ErrorLevel/Priority"));
			}

	}*/
	
	@Test(priority = 1, description = "AC01- Verify order get cancelled for CC Orders")
	public void validateOrderisCancelled_CC() throws InterruptedException {
		String AccessToken = FunLibrary.excelData.get("Header_access-token");
		String userjwttoken = FunLibrary.excelData.get("Header_user-jwt-token");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String ver = FunLibrary.excelData.get("ver");
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >0");
		System.out.println("slotid" + " " + FunLibrary.excelData.get("collectionPointId"));
		System.out.println("slotid" + " " + slotId);
		dbUtil_DM.closeDBConnection();
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserName"),FunLibrary.excelData.get("NickName"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		APILibrary.addUpdateTrolley_WCS_1("", "", "", AccessToken,userjwttoken, "0404", "5", "5833943");
		APILibrary.reserveSlot("0404", "1", slotId, "CC", "DM-SHIFT", "0404CC0404", ccpAddressId);
		//String identifier = APILibrary.getMPGSCardToken("5123450000000008", "01", "25", "100");
		String identifier = PS.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		APILibrary.savePaymentMethod_WCS_1("", "", "",AccessToken,userjwttoken, "0404", "card", "1.1", "true", identifier);
		response = PS.cartAttribute("", "", "", authorization, userAuthorization, "20501", "2", "", "instruction", "");
		APILibrary.orderProcess_WCS("", "", "",AccessToken ,userjwttoken	, "0404", "1");
		Response ordersubmit = APILibrary.orderSubmit_WCS_1("", "", "", AccessToken,userjwttoken, "0404", "1.1", "", "");
		JsonPath jsonPath = ordersubmit.jsonPath();
		String orderId = jsonPath.getString("orderId");
		System.out.println(orderId);
		Thread.sleep(30000);
		response = APILibrary.cancelOrder_WCS("", "", "",AccessToken,userjwttoken,"0404",orderId);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId",orderId);
		String db_order = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + orderId + "'");
		funLibrary.validate_Equals("db_status", db_order, "X");
		if(response.statusCode()==200)
		{
		  response =  APILibrary.cancelOrder_WCS("", "", "",AccessToken,userjwttoken,"0404",orderId);
			
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
		}

		
	}
	
	
	@Test(priority = 3,description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrderNotAssociatedwithMyAccount() {
		response = APILibrary.cancelOrder_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("storeIds"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(priority = 4,description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrdernotPresentinDB() {
		response = APILibrary.cancelOrder_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("storeIds"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	@Test(priority = 5,description = "AC03- Verify that order cannot be cancelled cut off for cancelling the order is passed ")
	public void validateOrderCutOff() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and STATUS='M'  order by LASTUPDATE desc");
		response = APILibrary.cancelOrder_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("storeIds"), db_order);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(priority = 6,description = "Verify invalid order status")
	public void validateInvalidOrderStatus() {
		response = APILibrary.cancelOrder_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("storeIds"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	@Override
	public String getTestName() {
		return testName.get();
	}
}

