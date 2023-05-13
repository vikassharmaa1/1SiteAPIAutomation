package ps.slotReservation.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;
import io.restassured.response.Response;

public class PS_SlotReservationEnhancement extends Base_Class_API implements ITest{
	
	@Test(description = "Verify slot reservation when not using ccpAddressId")
    public void slotReserveWithoutccpId() {
        DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
        String OrderAddressId = dbUtil.getValues("xorderattr", "VALUE", "name= 'OrderAddressId' and "
                + "orders_id = '" + FunLibrary.excelData.get("orders_id") + "'");
        String authorization = FunLibrary.excelData.get("Header_Authorization");
        String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
        Response response = null;
        String slotId="";
        String addressId_Burwood= FunLibrary.excelData.get("addressId_Burwood");
        String addressId_Bayview= FunLibrary.excelData.get("addressId_Bayview");
        if (OrderAddressId.equals(addressId_Burwood)) {
            response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0404", "1", "1", "Bayview", "0820", "NT", "ALL");
            slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));               
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
            response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, "0404", FunLibrary.excelData.get("addressId_Bayview"));
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
        }
        else if(OrderAddressId.equals(addressId_Bayview)){
            response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0584", "1", "1", "BURWOOD", "3125", "VIC", "ALL");
            slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));               
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
            funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
            response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, "0465", FunLibrary.excelData.get("addressId_Burwood"));
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
            response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
            funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
        }
        else {
            testLog.info("No slots found");
        }


/*switch (OrderAddressId) {
	
		case "67247570":
				
				response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0404", "1", "1", "Bayview", "0820", "NT", "ALL");
				slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));				
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
				response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, "0404", FunLibrary.excelData.get("addressId_Bayview"));
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				break;
				
		case "67247047":
				
				response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0584", "1", "1", "BURWOOD", "3125", "VIC", "ALL");
				slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));				
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
				response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, "0465", FunLibrary.excelData.get("addressId_Burwood"));
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				break;
				
			default:
				break;
				
		}
*/		
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	@Test(description = "Verify slot reservation when using ccpAddressId")
	public void slotReserveWithccpId() {
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String OrderAddressId = dbUtil.getValues("xorderattr", "VALUE", "name= 'OrderAddressId' and "
				+ "orders_id = '" + FunLibrary.excelData.get("orders_id") + "'");
		
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		Response response = null;
		String slotId="";
		String addressId_Burwood= FunLibrary.excelData.get("addressId_Burwood");
        String addressId_Bayview= FunLibrary.excelData.get("addressId_Bayview");
		if (OrderAddressId.equals(addressId_Burwood)) {
			response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0404", "1", "1", "Bayview", "0820", "NT", "ALL");
			slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
			response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", FunLibrary.excelData.get("ccpId_Bayview"));
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		}
		
		else if(OrderAddressId.equals(addressId_Bayview)){
			response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0584", "1", "1", "Burwood", "3125", "VIC", "ALL");
			slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
			response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", FunLibrary.excelData.get("ccpId_Burwood"));
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		}
		
		/*switch (OrderAddressId) {
		
			case "67247570":
				
				response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0404", "1", "1", "Bayview", "0820", "NT", "ALL");
				slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", FunLibrary.excelData.get("ccpId_Bayview"));
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				
			case "67247047":
				
				response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0584", "1", "1", "Burwood", "3125", "VIC", "ALL");
				slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0584", slotId, "HD", "DM-Shift", "", FunLibrary.excelData.get("ccpId_Burwood"));
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				
			default:
				break;
				
		}
		*/
		funLibrary.Assert.assertAll();
		
	}
	
	
	
	
	@Test(description = "Verify slot reservation with address type as B")
	public void slotReserve_BType() {
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String AddressType = dbUtil.getValues("address", "ADDRESSTYPE", "MEMBER_ID = '" + FunLibrary.excelData.get("userId") + "'");
		
		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		Response response = null;
		String slotId="";
		
				if(AddressType.trim().equals("B")) {
				response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, "0404", "1", "1", "Bayview", "0820", "NT", "ALL");
				slotId = funLibrary.getJsonPathValue(response, "slots.HD[0].id");
				response = PS.reserveSlot("", "", "", authorization, userAuthorization, "0404", slotId, "HD", "DM-Shift", "", "");
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
				
				}
				
				else {
					funLibrary.Assert.assertTrue(false, "Status mismatched");
					funLibrary.testLog.info("Status mismatched");
				}
				
				funLibrary.Assert.assertAll();
		}
		
		
	
	

	@Override
	public String getTestName() {
		return testName.get();
	}

}
