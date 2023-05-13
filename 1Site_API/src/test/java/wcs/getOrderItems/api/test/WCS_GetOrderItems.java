package wcs.getOrderItems.api.test;

import java.text.DecimalFormat;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_GetOrderItems extends Base_Class_API implements ITest {
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
	
	@Test( description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrderNotAssociatedwithMyAccount() {
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1", FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test( description = "AC02- Verify order number sent is not associated with my account")
	public void validateOrdernotPresentinDB() {
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1", FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
	}

	@Test(description = "validate schema of response json")
	public void validateSchema() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='X' " );
		System.out.println("final"+""+db_order);
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1", db_order);

		funLibrary.validateJsonStructure(response, "orderitems");
	}
	@Test( description = "AC0-01 Verify Order items fileds ")
	public void validateOrderItems() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='M' " );
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1", db_order);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		System.out.println("final"+"" +db_order);
		DecimalFormat decimalFormat = new DecimalFormat("0.#####");
		JsonPath jsonPath = response.jsonPath();
		Double responseUnitPrice1 =jsonPath.getDouble("orderItems[0].unitPrice");
		String responseUnitPrice = decimalFormat.format(Double.valueOf(responseUnitPrice1));
		Double responseitemTotal1 =jsonPath.getDouble("orderItems[0].itemTotal");
		String responseitemTotal = decimalFormat.format(Double.valueOf(responseitemTotal1));
		String orderItemId =jsonPath.getString("orderItems[0].orderItemId");
		System.out.println("final3"+"" +orderItemId);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		String dbunitPrice1=dbUtil.getValues("orderitems", "price","ORDERITEMS_ID='" + orderItemId + "'order by orderitems_id desc limit 1" );
		String dbunitPrice = decimalFormat.format(Double.valueOf(dbunitPrice1));
		String dbTotalPrice1=dbUtil.getValues("orderitems", "totalproduct","ORDERITEMS_ID='" + orderItemId + "'order by orderitems_id desc limit 1");
		String dbTotalPrice = decimalFormat.format(Double.valueOf(dbTotalPrice1));
		String dborderItemId=dbUtil.getValues("orderitems", "orderitems_id","ORDERITEMS_ID='" + orderItemId + "'order by orderitems_id desc limit 1");
		String dbpartNumber=dbUtil.getValues("orderitems", "partnum","ORDERITEMS_ID='" + orderItemId + "'order by orderitems_id desc limit 1");	
		funLibrary.validate_Equals("unitPrice", responseUnitPrice, dbunitPrice);
		funLibrary.validate_Equals("itemTotal", responseitemTotal, dbTotalPrice);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].orderItemId", dborderItemId);
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[0].partNumber", dbpartNumber);
	}
	
	@Test( description = "AC01- Verify Liquor/Tobbaco items in Order")
	public void validateOrderItemsLiquorTobbaco() {
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1", FunLibrary.excelData.get("orderId"));
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[1].itemAttributes.liquorAgeRestrictionFlag", "true");
		funLibrary.validateJSONPathValue_Equals(response, "orderItems[2].itemAttributes.tobaccoAgeRestrictionFlag", "true");
	}
	@Test( description = "Verify response for P order status")
	public void validateOrderStatusforP() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='P' " );
		System.out.println("final"+""+db_order);
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1",db_order);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}

	@Test( description = "Verify response for J order status")
	public void validateOrderStatusforJ() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_order = dbUtil.getValues("orders", "ORDERS_ID","member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")+ "') and STATUS='J' " );
		System.out.println("final"+""+db_order);
		response=APILibrary.getOrderItems_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1",db_order);

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	@Override
	public String getTestName() {
		return testName.get();
	}
}
