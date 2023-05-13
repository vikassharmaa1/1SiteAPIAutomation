package wcs.orderSubmit.api.tests;

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

public class WCS_EnhanceOrderSubmit extends Base_Class_API implements ITest {

	@Test(description = "Validate that reponse have flybuy details")
	public void validateFlybuyData() {
//getting slot from DM
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		System.out.println("slotid" + " " + slotId);
		dbUtil_DM.closeDBConnection();

		// Fetching ccp address ID
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("UserName"),
				FunLibrary.excelData.get("NickName"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");

		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileFlyBuysNumber'");
		String DB_FlyBuysNumber = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");
		System.out.println(DB_FlyBuysNumber);
//Add item to trolley
		APILibrary.addUpdateTrolley_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "5", "5833943");
	
//Reserving slot
		APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		//String identifier = APILibrary.getMPGSCardToken("5123450000000008", "01", "25", "100");
		String identifier = PS.getMPGSCardToken("4111111111111111", "12", "25", "123");

		RestAssured.reset();
		APILibrary.savePaymentMethod_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "card", "1.1", "true", identifier);
		APILibrary.orderProcess_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1");
		Response ordersubmit = APILibrary.orderSubmit_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1", "2795098945019", "2777150527609");
		JsonPath jsonPath = ordersubmit.jsonPath();
		String responseFlyBuyNumber = jsonPath.getString("orderAttributes.orderFlybuyBarcode");
		funLibrary.validate_Equals("FlyBuyNumber", DB_FlyBuysNumber, responseFlyBuyNumber);
		String maskednumber = jsonPath.getString("orderAttributes.orderFlybuyNumber");
		funLibrary.validate_Equals("MaskedNumber", "600xxxxxxxxx5012", maskednumber);
		if (ordersubmit.statusCode() == 400) {
			APILibrary.orderProcess_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"),
					FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1");
			Response ordersubmit1 = APILibrary.orderSubmit_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
					FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1", "2795098945019", "2777150527609");

			JsonPath jsonPath1 = ordersubmit1.jsonPath();
			String maskednumber1 = jsonPath1.getString("orderAttributes.orderFlybuyNumber");
			String responseFlyBuyNumber1 = jsonPath1.getString("orderAttributes.orderFlybuyBarcode");
			funLibrary.validate_Equals("FlyBuyNumber", DB_FlyBuysNumber, responseFlyBuyNumber1);
			funLibrary.validate_Equals("FlyBuyNumber", DB_FlyBuysNumber, responseFlyBuyNumber1);
			funLibrary.validate_Equals("MaskedNumber", "600xxxxxxxxx5012", maskednumber1);
			funLibrary.validateJSONPathValue_Equals(ordersubmit1, "paymentMethod.data.cardType",
					FunLibrary.excelData.get("cardType"));
			funLibrary.validateJSONPathValue_Equals(ordersubmit1, "paymentMethod.data.cardExpiry", "01/2025");

		}

	}

	@Test(description = "Validate that error reponse for invalid flybuy details")
	public void validateInvalidFlybuy() {

		Response st = APILibrary.orderSubmit_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1", "27920010952121", "2777150527609");
		System.out.println(st.statusCode());
		JsonPath jsonPath = st.jsonPath();
		funLibrary.validateJSONPathValue_Equals(st, "errors[0].errorCode", "COLRS-ERR-OSUB-FBN-INVLD-001");

	}

	@Test(description = "Validate that error reponse for invalid staffmember details")
	public void validateInvalidStaffMember() {

		Response st = APILibrary.orderSubmit_WCS_1("", "", "", FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"), "0404", "1.1", "2792001095212", "27771505276091");
		System.out.println(st.statusCode());
		JsonPath jsonPath = st.jsonPath();
		funLibrary.validateJSONPathValue_Equals(st, "errors[0].errorCode", "COLRS-ERR-OSUB-STFNO-INVLD-001");

	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
