package wcs.orderProcess.api.test;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_BaggingDetails extends Base_Class_API implements ITest {
	
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
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		APILibrary.addUpdateTrolley_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "5", "5833943");
		APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		Response orderprocess = APILibrary.orderProcess_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1");
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
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("UserName"), FunLibrary.excelData.get("Header_user-jwt-token"),
				"0404");
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
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		APILibrary.addUpdateTrolley_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "5", "5833943");
		APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);

		Response orderprocess = APILibrary.orderProcess_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1");
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
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("UserName"), FunLibrary.excelData.get("Header_user-jwt-token"),
				"0404");
	}

@Override
public String getTestName() {
	return testName.get();
}
}
