package apim.cancelorder.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
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

public class APIM_CancelOrder extends Base_Class_API implements ITest {
	Response response;
	static String hdorder;
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class,priority = 8)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		restLibrary.addHeader("Ocp-Apim-Subscription-Key",FunLibrary.excelData.get("Header_subscriptionKey"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		restLibrary.addQueryParameter("storeId", FunLibrary.excelData.get("storeIds"));
		restLibrary.addQueryParameter("ver", FunLibrary.excelData.get("ver"));
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
			// funLibrary.validateStatusCode(response,
			// FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "message", "Resource not found");
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(priority = 0, description = "AC01- Verify order get cancelled for HD Orders")
	public void validateOrderisCancelled_HD() throws InterruptedException {
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String subscription_key = FunLibrary.excelData.get("Header_subscriptionKey");
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
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),FunLibrary.excelData.get("JWT-Token"), "0404","0820", "BAYVIEW");
		APILibrary.addUpdateTrolley_APIM_1("", "", "", authorization, userAuthorization, subscription_key, "5833943",	"5");
		APILibrary.reserveSlot_APIM_3("", "", "", authorization, userAuthorization, subscription_key, "0404", "1", slotId,"HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		String identifier = APILibrary.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		System.out.println(identifier);
		APILibrary.savePaymentMethod_Card_APIM("", "", "", authorization, userAuthorization, subscription_key, "0404","card", "true", identifier, "1");
		response = PS.cartAttribute("", "", "", authorization, userAuthorization, "20501", "2", "", "instruction", "");																										
		APILibrary.orderProcessAPIM_1("", "", "", authorization, userAuthorization, subscription_key, "1.1");
		Thread.sleep(20000);
		Response ordersubmit = APILibrary.orderSubmit_APIM("", "", "", "0404", authorization, userAuthorization,subscription_key, "", "", "1.1");
		JsonPath jsonPath = ordersubmit.jsonPath();
		String orderId = jsonPath.getString("orderId");
		System.out.println(orderId);
		Thread.sleep(20000);
		response = APILibrary.cancelOrder_APIM("", "", "", userAuthorization,authorization,orderId);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		String db_order = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + orderId + "'");
		funLibrary.validate_Equals("db_status", db_order, "X");

		if (response.statusCode() == 200) {
			response = APILibrary.cancelOrder_PS("", "", "", userAuthorization, authorization, orderId);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
		}

	}
	
	/*@Test(priority = 2, description = "AC01- Verify order get cancelled for HD Orders",dependsOnMethods = { "validateOrderisCancelled_HD" })
	public void validateOrderisCancelled_HD1() throws InterruptedException {
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String ver = FunLibrary.excelData.get("ver");
		String UserName = FunLibrary.excelData.get("UserName");
		System.out.println("usernamae" + " " + UserName);

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='M'  order by timeplaced " );
		System.out.println("final"+""+db_order);
		response = APILibrary.cancelOrder_APIM("", "", "", userAuthorization,authorization, db_order);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", db_order);
		String db_order1 = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + db_order + "'");
		funLibrary.validate_Equals("db_status",  db_order1, "X");
		  if(response.statusCode()==200)
			{
				response = APILibrary.cancelOrder_APIM("", "", "", userAuthorization,authorization, db_order);
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
						FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
						FunLibrary.excelData.get("ErrorLevel/Priority"));
			}
	}
	*/
	

	@Test(priority = 1, description = "AC01- Verify order get cancelled for CC Orders")
	public void validateOrderisCancelled_CC() throws InterruptedException {
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String storeId = FunLibrary.excelData.get("StoreIds");
		String subscription_key = FunLibrary.excelData.get("Header_subscriptionKey");
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
		APILibrary.addUpdateTrolley_APIM_1("", "", "", authorization, userAuthorization, subscription_key, "5833943","5");
		APILibrary.reserveSlot_APIM_3("", "", "", authorization, userAuthorization, subscription_key, "0404", "1", slotId,"CC", "DM-SHIFT", "0404CC0404", ccpAddressId);
		String identifier = APILibrary.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		System.out.println(identifier);
		APILibrary.savePaymentMethod_Card_APIM("", "", "", authorization, userAuthorization, subscription_key, "0404","card", "true", identifier, "1");
		response = PS.cartAttribute("", "", "", authorization, userAuthorization, "20501", "2", "", "instruction", "");																										
		APILibrary.orderProcessAPIM_1("", "", "", authorization, userAuthorization, subscription_key, "1.1");
		Response ordersubmit = APILibrary.orderSubmit_APIM("", "", "", "0404", authorization, userAuthorization,subscription_key, "", "", "1.1");
		JsonPath jsonPath = ordersubmit.jsonPath();
		String orderId = jsonPath.getString("orderId");
		System.out.println(orderId);
		Thread.sleep(20000);
		response = APILibrary.cancelOrder_APIM("", "", "", userAuthorization,authorization,orderId);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		String db_order = dbUtil.getValues("orders", "STATUS", "ORDERS_ID='" + orderId + "'");
		funLibrary.validate_Equals("db_status", db_order, "X");
		  if(response.statusCode()==200)
			{
				response = APILibrary.cancelOrder_APIM("", "", "", userAuthorization,authorization, orderId);
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
						FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
						FunLibrary.excelData.get("ErrorLevel/Priority"));
			}

	}
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class,priority = 7)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		restLibrary.addQueryParameter("storeId", FunLibrary.excelData.get("storeIds"));
		restLibrary.addQueryParameter("ver", FunLibrary.excelData.get("ver"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(priority = 3,description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrderNotAssociatedwithMyAccount() {
		response = APILibrary.cancelOrder_APIM("", "", "", FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(priority = 4,description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrdernotPresentinDB() {
		response = APILibrary.cancelOrder_APIM("", "", "", FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(priority = 5,description = "AC03- Verify that order cannot be cancelled cut off for cancelling the order is passed ")
	public void validateOrderCutOff() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='M'  order by LASTUPDATE desc" );

		response = APILibrary.cancelOrder_APIM("", "", "", FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Header_Authorization"),db_order);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(priority = 6,description = "Verify invalid order status")
	public void validateInvalidOrderStatus() {
		response = APILibrary.cancelOrder_APIM("", "", "", FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
