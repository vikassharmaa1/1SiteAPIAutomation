package ps.integration.tests;

import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class E2E_HD extends Base_Class_API{

	@Test
	public void HD_SignedDelivery() {

		String authorization = FunLibrary.excelData.get("Header_Authorization");
		String userAuthorization = FunLibrary.excelData.get("Header_UserAuthorization");
		String storeId = FunLibrary.excelData.get("StoreId");
		String userId = FunLibrary.excelData.get("UserId");
		String addressId = FunLibrary.getAddressID(userId, "Home");
		String qty = FunLibrary.excelData.get("Quantity");
		String partNumber = FunLibrary.excelData.get("PartNumber");
		String orderId = FunLibrary.getCurrentOrdersID(userId);
		String promocode = "CUSPTESTPERCCODE";
		Response response = null;
		
		
		PS.removePromoCode("", "", "", authorization, userAuthorization, storeId, promocode, "1.1");
		
		response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, storeId, addressId);

		funLibrary.validateStatusCode(response, "200");
	

		response = PS.addUpdateTrolley("", "", "", authorization, userAuthorization, partNumber, "1");
		PS.EmptyTrolley(userId, authorization, userAuthorization, storeId);
		response = PS.addUpdateTrolley("", "", "", authorization, userAuthorization, partNumber, "5");
		
		// get orderItemID from above response		
		String orderItemId = FunLibrary.getOrderItemID(userId, partNumber);
		
		response = PS.bagEstimator("", "", "", authorization, userAuthorization, storeId,"1.1");
		response = PS.syncValidate("", "", "", authorization, userAuthorization,storeId, "1.1");
		response = PS.updateItem("", "", "", authorization, userAuthorization, storeId, partNumber, orderItemId, "7", "false");
		//------------------
		
		response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, storeId, "1", "2", "Bayview", "0820", "NT", "ALL");
		
		//get slot id from above response
		String slotId=funLibrary.getJsonPathValue(response, "slots.HD[0].id");
		response = PS.reserveSlot("", "", "", authorization, userAuthorization, storeId, slotId, "HD", "DM-Shift", "", "");
		response = PS.syncValidate("", "", "", authorization, userAuthorization,storeId, "1.1"); //validate 206
		response = PS.cartAttribute("","","", authorization, userAuthorization, storeId, "2", "", "instruction", "");// check bagging option according to the selected slot. indentify the type of slot first
		response = PS.applyPromoCode("", "", "", authorization, userAuthorization, promocode, storeId);// check promo code condition/edit promocode
		String identifier = PS.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		response= PS.savePaymentMethod_Card("", "", "", authorization, userAuthorization, storeId, "card", "false", identifier);
	    response=PS.deleteSavedCard("", "", "", storeId, authorization, userAuthorization, "1", "SavedCard");
	    String identifier1 = PS.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		response= PS.savePaymentMethod_Card("", "", "", authorization, userAuthorization, storeId, "card", "false", identifier1);
		response=PS.orderProcess("", "", "",storeId, authorization, userAuthorization, "1.1");
		response=PS.orderSubmit("", "", "", storeId, authorization, userAuthorization, "", "", "1.1", "sia");
	}

}
