package wcs.trolley.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.api.WCS;
import com.rest.body.SlotReservationPayload;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;



import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import orderFullSummary.pojo.OrderAttributes;
import orderFullSummary.pojo.Root;

public class WCS_FullOrderSummary_RD extends Base_Class_API implements ITest{
	
	Root root;

	@Test(description = "AC01 - Verify Order Full Weight summary response with RD changes")
	public void validateRDResponse() {
	
	String authorization = FunLibrary.excelData.get("Header2_Value");
	String userAuthorization = FunLibrary.excelData.get("Header1_Value");
	String partNumber = FunLibrary.excelData.get("PartNumber");
	String storeId = FunLibrary.excelData.get("PathParamValue1");
	
	String qty = FunLibrary.excelData.get("qty");
	String ver = FunLibrary.excelData.get("ver");
	String userId = FunLibrary.excelData.get("userId");
	String bodyCartAttributes = FunLibrary.excelData.get("RequestBody");
	Response response = null;

	RestLibrary restLibrary = new RestLibrary();
	
	DatabaseUtilities dbUtils = new DatabaseUtilities();
	//String orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + userId + "') and STATUS='P'");
	String orderItemId = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
String additionalFields= "true";
	
	//response = WCS.addUpdateTrolley("", "", "", authorization, userAuthorization, partNumber, "5");
	response = APILibrary.updateItem_WCS("", "", "", authorization, userAuthorization, storeId,partNumber,orderItemId,qty,additionalFields);
	
	DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
	String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
			"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(3, "yyyy-MM-dd")
					+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME like'"
					+ FunLibrary.excelData.get("collectionPointId")
					+ "'and service_type = 'RD'");
	System.out.println(slotId);
	String collectionPointId = dbUtil_DM.getValues("DELWINDOW", "COLLECTIONPOINT_NAME",
			"ID="+slotId);
	System.out.println(collectionPointId);
	dbUtil_DM.closeDBConnection();
	
	String bodyReserveSlot=SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
			collectionPointId);
	if (!slotId.equals("No Record Found")) {
	response = APILibrary.reserveSlot_WCS("", "", "", authorization, userAuthorization,storeId,bodyReserveSlot,ver);
	}
	else
		testLog.info("No RD Slot available for the given Date");
		
	
	response = WCS.updateRDCustomerDetailsWithBody("", "", "", authorization, userAuthorization, storeId, ver, bodyCartAttributes);
	
	response = APILibrary.OrderFullWeightSummary_WCS("", "", "", authorization, userAuthorization, storeId, ver);
	
	
	
	funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	root = (Root) restLibrary.getResponseBody(response, Root.class);
	OrderAttributes orderAttr = root.getOrderAttributes();
	
	
	String serviceType = orderAttr.getServiceType();
	System.out.println(serviceType);
	funLibrary.validate_Equals("Service Type", "RD", serviceType);
	
	
	String orderID_db = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + userId + "') and STATUS='P'");
	//String orderID_db =dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and STATUS='P'");
	String addressId_db =dbUtils.getValues("ORDERITEMS", "ADDRESS_ID", "ORDERS_ID="+orderID_db);
	String addressId = orderAttr.getRdAddressId(); 
	//funLibrary.validate_Equals("Address ID", addressId_db,addressId);
	
	
	dbUtils.closeDBConnection();
	dbUtil_DM.closeDBConnection();
	funLibrary.Assert.assertAll();
	
	
}
	
	
	
	
	
	
	
	
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	

}
