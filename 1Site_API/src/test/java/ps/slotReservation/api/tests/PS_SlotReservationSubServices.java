package ps.slotReservation.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.body.SlotReservationPayload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PS_SlotReservationSubServices extends Base_Class_API implements ITest {


	
	@Test(description = "MC Subservice")
	public void subserviceMC() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")+ "' and minimum_order_value >3");
		
					
		dbUtil_DM.closeDBConnection();
		/*Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));*/


		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
	
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "CC", "123",
				FunLibrary.excelData.get("collectionPointId1")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
		//funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);


	}
	
/*	@Test(description = "SLSubservice")
	public void subserviceSL() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "'");
		
					
		dbUtil_DM.closeDBConnection();
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));

		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "CC", "123",
				FunLibrary.excelData.get("collectionPointId1")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
	//	funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);


	}*/
	
	/*@Test(description = "CE Subservice")
	public void subserviceCE() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >2");
		
					
		dbUtil_DM.closeDBConnection();
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));

		
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "CC", "123",
				FunLibrary.excelData.get("collectionPointId1")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
		//funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);


	}*/
	
	
	@Test(description = "SD Subservice")
	public void subserviceSD() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >4");
		
					
		dbUtil_DM.closeDBConnection();
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));

		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "CC", "123",
				FunLibrary.excelData.get("collectionPointId")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
	//	funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);


	}
	
	@Test(description = "CC Subservice")
	public void subserviceCC() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' ");
		
					
		dbUtil_DM.closeDBConnection();
		/*Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));
*/

		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "CC", "123",
				FunLibrary.excelData.get("collectionPointId1")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
		funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);

	}
	
	

	@Test(description = "RD Subservice")
	public void subserviceRD() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD' and minimum_order_value is null");
		dbUtil_DM.closeDBConnection();
		/*Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));
*/

		
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId1")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
		//funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);


	}
	
	@Test(description = "HD Subservice")
	public void subserviceHD() {
		
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD' ");
		dbUtil_DM.closeDBConnection();
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID",
				"member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and NICKNAME='" + FunLibrary.excelData.get("NickName")
						+ "' and STATUS='P' and ADDRESSTYPE='SB'");
		String ccpAddressId = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and ADDRESS_ID='" + db_AddressId + "'");
		
		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), FunLibrary.excelData.get("storeId"),
				FunLibrary.excelData.get("PostCode"), FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"),
				FunLibrary.excelData.get("JWT-Token"), "20510", FunLibrary.excelData.get("colAddressId"));*/


		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		
		restLibrary.addBody(SlotReservationPayload.psReservationPayload( FunLibrary.excelData.get("storeId"),slotId, "HD", ccpAddressId,
				FunLibrary.excelData.get("collectionPointId")));
	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsesubservice = jsonPath.getString("subServiceType");
		System.out.println("responsesubservice" + " " + responsesubservice);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		String DBresponsesubservice = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='OrderSubServiceType'");
		System.out.println(" DBresponsesubservice" + "" + DBresponsesubservice);
		//funLibrary.validate_Contains("Subservice",DBresponsesubservice, responsesubservice);
		funLibrary.validate_Equals("Subservice", DBresponsesubservice, responsesubservice);

		dbUtil.closeDBConnection();


	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}