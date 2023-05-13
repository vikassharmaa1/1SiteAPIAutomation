package ps.orderProcess.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PS_BaggingDetails extends Base_Class_API implements ITest {

	@Test(description = "Validate bagging cost calculation")
	public void validateOrderProcessforBetterbag() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		System.out.println("slotid" + " " + slotId);
		dbUtil_DM.closeDBConnection();
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserName"),
				FunLibrary.excelData.get("NickName"));
		System.out.println("ccp" + "" + ccpAddressId);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String JWT = APILibrary.RemoveBearer(FunLibrary.excelData.get("Header_userauthorization"));
		System.out.println(FunLibrary.excelData.get("Header_userauthorization"));
		System.out.println(JWT);
		APILibrary.addUpdateTrolley_PS_1("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"), "5833943", "1");
		APILibrary.reserveSlot_PS("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		String identifier = APILibrary.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		APILibrary.savePaymentMethod_Card_PS("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"), "0404", "card", "true",identifier, "1");
		PS.cartAttribute("", "", "",FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"), "20501", "2", "", "instruction", "");
		Response orderprocess = APILibrary.orderProcessPS_1("", "", "", "0404",
				FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"),
				"1.1");
		System.out.println(orderprocess.statusCode());
		JsonPath jsonPath = orderprocess.jsonPath();
		String responsebagging = jsonPath.getString("baggingItems");
		System.out.println(responsebagging);
		double responseqty = jsonPath.getDouble("baggingItems[0].qty");
		System.out.println(responseqty);
		double responseunitprice = jsonPath.getDouble("baggingItems[0].unitPrice");
		System.out.println(responseunitprice);
		double resposnetotalPrice = jsonPath.getDouble("baggingItems[0].totalPrice");
		System.out.println(resposnetotalPrice);
		double totalprice = responseqty * responseunitprice;
		System.out.println("test" + "" + totalprice);

		funLibrary.validateJSONPathValue_Equals(orderprocess, "baggingItems[0].unitPrice",
				FunLibrary.excelData.get("unitPrice"));
		funLibrary.validateJSONPathValue_Equals(orderprocess, "baggingItems[0].bagType",
				FunLibrary.excelData.get("bagType"));
		funLibrary.validate_Equals("OrderProcess", totalprice, resposnetotalPrice);
		//APILibrary.EmptyTrolley(FunLibrary.excelData.get("UserName"), JWT, "0404");
	}

	@Test(description = "Validate bagging cost calculation")
	public void validateOrderProcessforPaperBag() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		System.out.println("slotid" + " " + slotId);
		dbUtil_DM.closeDBConnection();
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserName"),
				FunLibrary.excelData.get("NickName"));
		System.out.println("ccp" + "" + ccpAddressId);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String JWT = APILibrary.RemoveBearer(FunLibrary.excelData.get("Header_userauthorization"));
		System.out.println(FunLibrary.excelData.get("Header_userauthorization"));
		System.out.println(JWT);

		APILibrary.addUpdateTrolley_PS_1("", "", "", FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_userauthorization"), "5833943", "1");

		APILibrary.reserveSlot_PS("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);

		Response orderprocess = APILibrary.orderProcessPS_1("", "", "", "0465",
				FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"),
				"1.1");
		JsonPath jsonPath = orderprocess.jsonPath();
		String responsebagging = jsonPath.getString("baggingItems");
		System.out.println(responsebagging);
		double responseqty = jsonPath.getDouble("baggingItems[0].qty");
		System.out.println(responseqty);
		double responseunitprice = jsonPath.getDouble("baggingItems[0].unitPrice");
		System.out.println(responseunitprice);
		double resposnetotalPrice = jsonPath.getDouble("baggingItems[0].totalPrice");
		System.out.println(resposnetotalPrice);
		double totalprice = responseqty * responseunitprice;
		System.out.println("test" + "" + totalprice);

		funLibrary.validateJSONPathValue_Equals(orderprocess, "baggingItems[0].unitPrice",
				FunLibrary.excelData.get("unitPrice"));
	/*	funLibrary.validateJSONPathValue_Equals(orderprocess, "baggingItems[0].bagType",
				FunLibrary.excelData.get("bagType"));*/
		funLibrary.validate_Equals("OrderProcess", totalprice, resposnetotalPrice);
	//	APILibrary.EmptyTrolley(FunLibrary.excelData.get("UserName"), JWT, "0404");
	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
