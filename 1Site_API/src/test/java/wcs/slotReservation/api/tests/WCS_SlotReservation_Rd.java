package wcs.slotReservation.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.SlotReservationPayload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_SlotReservation_Rd  extends Base_Class_API implements ITest {
	
	
	
	@Test(description = "AC01 Checking Reserve slot response")
	public void ReserveSlotResponseforRd() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD'");
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		
		String serviceType = jsonPath.getString("serviceType");
		funLibrary.validate_Equals("Service Type", "RD", serviceType);
		
		String subServiceType = jsonPath.getString("subServiceType");
		funLibrary.validate_Equals("Service Type", "RD", subServiceType);
	
		String baggingOption = jsonPath.getString("baggingOption");
		funLibrary.validate_Equals("Bagging Option", "0", baggingOption);
	
		String unattendedType = jsonPath.getString("unattendedType");
		funLibrary.validate_Equals("Unattended Type", "0", unattendedType);

		String slotMaxItems = jsonPath.getString("slotMaxItems");
		String slotMaxItems_db = dbUtil_DM.getValues("DELWINDOW", "MAX_ITEMS","ID ="+slotId);
		funLibrary.validate_Equals("slotMaxItems", slotMaxItems_db,slotMaxItems);
	
		String shift = jsonPath.getString("shift");
		String shift_db = dbUtil_DM.getValues("DELWINDOW", "shift","ID ="+slotId);
		funLibrary.validate_Equals("Shift", shift_db,shift);
		dbUtil_DM.closeDBConnection();
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		
		String orderId_db = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and STATUS='P'");
		String orderId = jsonPath.getString("orderId"); 
		funLibrary.validate_Equals("Order ID", orderId_db,orderId);
		 
		
		String rdPartnerId = jsonPath.getString("rdPartnerId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		funLibrary.validate_Equals("RD Partner ID", collectionPointId,rdPartnerId);

		
		String rdPartnerName = jsonPath.getString("rdPartnerName");
		String stlocds_id=dbUtil.getValues("stloc", "STLOC_ID","IDENTIFIER ='"+collectionPointId+ "'");
		String rdPartnerName_DB = dbUtil.getValues("STLOCDS", "NAME","STLOCDS_ID ="+stlocds_id);
		funLibrary.validate_Equals("rdPartnerName", rdPartnerName_DB.replaceAll("via", "").trim(),rdPartnerName);

		
		String rdPartnerPhone = jsonPath.getString("rdPartnerPhone");
		String rdPartnerPhone_db = dbUtil.getValues("stloc", "PHONE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerPhone", rdPartnerPhone_db,rdPartnerPhone);
		
		
		String rdPartnerPostcode = jsonPath.getString("rdPartnerPostcode");
		String rdPartnerPostcode_db = dbUtil.getValues("stloc", "ZIPCODE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerPostcode", rdPartnerPostcode_db,rdPartnerPostcode);

		
		String rdPartnerAddressLine = jsonPath.getString("rdPartnerAddressLine");
		String rdPartnerAddressLine_db =dbUtil.getValues("stloc", "ADDRESS1","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerAddressLine", rdPartnerAddressLine_db,rdPartnerAddressLine);

		String rdPartnerSuburb = jsonPath.getString("rdPartnerSuburb");
		String rdPartnerSuburb_db = dbUtil.getValues("stloc", "CITY","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerSuburb", rdPartnerSuburb_db,rdPartnerSuburb);

		String rdPartnerState = jsonPath.getString("rdPartnerState");
		String rdPartnerState_db = dbUtil.getValues("stloc", "STATE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerState", rdPartnerState_db,rdPartnerState);
		
		String rdPartnerCountry = jsonPath.getString("rdPartnerCountry");
		String rdPartnerCountry_db = dbUtil.getValues("stloc", "COUNTRY","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerCountry", rdPartnerCountry_db,rdPartnerCountry);
		dbUtil.closeDBConnection();
		funLibrary.Assert.assertAll();

	}
	
	
	@Test(description = "AC02 Checking Reserve slot response for RD delivery address details")
	public void ReserveSlotResponseDeliveryDetails() {
	
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD'");
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
	
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		
		String rdPartnerId = jsonPath.getString("rdPartnerId");
		String collectionPointId = FunLibrary.excelData.get("collectionPointId");
		funLibrary.validate_Equals("RD Partner ID", collectionPointId,rdPartnerId);

		
		String rdPartnerName = jsonPath.getString("rdPartnerName");
		String stlocds_id=dbUtil.getValues("stloc", "STLOC_ID","IDENTIFIER ='"+collectionPointId+ "'");
		String rdPartnerName_DB = dbUtil.getValues("STLOCDS", "NAME","STLOCDS_ID ="+stlocds_id);
		funLibrary.validate_Equals("rdPartnerName", rdPartnerName_DB.replaceAll("via", "").trim(),rdPartnerName);

		
		String rdPartnerPhone = jsonPath.getString("rdPartnerPhone");
		String rdPartnerPhone_db = dbUtil.getValues("stloc", "PHONE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerPhone", rdPartnerPhone_db,rdPartnerPhone);
		
		
		String rdPartnerPostcode = jsonPath.getString("rdPartnerPostcode");
		String rdPartnerPostcode_db = dbUtil.getValues("stloc", "ZIPCODE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerPostcode", rdPartnerPostcode_db,rdPartnerPostcode);

		
		String rdPartnerAddressLine = jsonPath.getString("rdPartnerAddressLine");
		String rdPartnerAddressLine_db =dbUtil.getValues("stloc", "ADDRESS1","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerAddressLine", rdPartnerAddressLine_db,rdPartnerAddressLine);

		String rdPartnerSuburb = jsonPath.getString("rdPartnerSuburb");
		String rdPartnerSuburb_db = dbUtil.getValues("stloc", "CITY","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerSuburb", rdPartnerSuburb_db,rdPartnerSuburb);

		String rdPartnerState = jsonPath.getString("rdPartnerState");
		String rdPartnerState_db = dbUtil.getValues("stloc", "STATE","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerState", rdPartnerState_db,rdPartnerState);
		
		String rdPartnerCountry = jsonPath.getString("rdPartnerCountry");
		String rdPartnerCountry_db = dbUtil.getValues("stloc", "COUNTRY","IDENTIFIER ='"+collectionPointId+"'");
		funLibrary.validate_Equals("rdPartnerCountry", rdPartnerCountry_db,rdPartnerCountry);
		dbUtil.closeDBConnection();
		
		funLibrary.Assert.assertAll();
	}

	
	@Test(description = "AC03 Checking Reserve slot response for Service Type and SubService Type")
	public void ReserveSlotResponseforServiceType() {

	
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD'");
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		
		String serviceType = jsonPath.getString("serviceType");
		funLibrary.validate_Equals("Service Type", "RD", serviceType);
		
		String subServiceType = jsonPath.getString("subServiceType");
		funLibrary.validate_Equals("Service Type", "RD", subServiceType);
		dbUtil_DM.closeDBConnection();
		funLibrary.Assert.assertAll();
	
	}
	
	@Test(description = "AC04 Checking Reserve slot response for Bagging Option and Unattended Type")
	public void ReserveSlotResponseforBaggingUnattended() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD'");
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
	
	
		String baggingOption = jsonPath.getString("baggingOption");
		funLibrary.validate_Equals("Bagging Option", "0", baggingOption);
	
		String unattendedType = jsonPath.getString("unattendedType");
		funLibrary.validate_Equals("Unattended Type", "0", unattendedType);
		dbUtil_DM.closeDBConnection();
		funLibrary.Assert.assertAll();

	}
	

	@Test(description = "AC05 Checking Reserve slot response for address ID")
	public void ReserveSlotResponseforAddressID() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD'");
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(SlotReservationPayload.wcsReservationPayload(slotId, "RD", "123",
				FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
	
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String orderID_db =dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName") + "') and STATUS='P'");
		String addressId_db =dbUtil.getValues("ORDERITEMS", "ADDRESS_ID", "ORDERS_ID="+orderID_db);
		String addressId = jsonPath.getString("addressId"); 
		funLibrary.validate_Equals("Address ID", addressId_db,addressId);
		
		dbUtil.closeDBConnection();
		dbUtil_DM.closeDBConnection();
		funLibrary.Assert.assertAll();

	}

	@Override
	public String getTestName() {
		return testName.get();
	}
	
	
	

}
