package apim.slotReservation.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.body.SlotReservationPayload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIM_SlotReservation extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
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
			funLibrary.validateJSONPathValue_Equals(response, "message", "Resource not found");

			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));

		// 400 nd 404 need to change in sheet
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

	@Test(description = "RD AC01- Verify XORDERATTR table updated")
	public void validSlotDetailsUpateinDBRD() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "' and service_type = 'RD' and DELIVERY_RESTRICTION_ID1 NOT IN('')");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"RD", "123", FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseisColesPlusMember = jsonPath.getString("isColesPlusMember");
		String responseslotMaxItems = jsonPath.getString("slotMaxItems");

		Double responseslotMOVForFreeShipCharges = jsonPath.getDouble("slotMOVForFreeShipCharge");
		String responseslotMOVForFreeShipCharge = Double.toString(responseslotMOVForFreeShipCharges);

		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		Double responseslotShipCharges = jsonPath.getDouble("slotShipCharge");
		String responseslotShipCharge = Double.toString(responseslotShipCharges);

		String responeslotRestriction = jsonPath.getString("slotRestriction[0]");
		System.out.println("responseslotMaxItems" + " " + responseslotMaxItems);
		System.out.println("responseisColesPlusMember" + " " + responseisColesPlusMember);
		System.out.println("responseslotMOVForFreeShipCharge" + " " + responseslotMOVForFreeShipCharge);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);
		System.out.println("responseslotShipCharge" + " " + responseslotShipCharge);
		System.out.println("responeslotRestriction" + " " + responeslotRestriction);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		String DBisColesPlusMember = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='isColesPlusMember'");
		String DBslotMaxItems = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMaxItems'");
		String DBslotMOVForFreeShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMOVForFreeShipCharge'");
		String DBslotRestriction = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='DMDeliverySlotRestrictions'");
		String DBslotShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotShipCharge'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		System.out.println(" DBisColesPlusMember" + "" + DBisColesPlusMember);
		System.out.println(" DBslotMaxItems" + "" + DBslotMaxItems);
		System.out.println("DBslotMOVForFreeShipCharge" + "" + DBslotMOVForFreeShipCharge);
		System.out.println(" DBslotRestriction" + "" + DBslotRestriction);
		System.out.println("DBslotShipCharge" + "" + DBslotShipCharge);

		funLibrary.validate_Equals(" MOV", DBoverallMOV, responseoverallMOV);
		funLibrary.validate_Equals("Coles Plus Member", DBisColesPlusMember, responseisColesPlusMember);
		funLibrary.validate_Contains("DMRestriction", DBslotRestriction, responeslotRestriction);
		funLibrary.validate_Contains("slotMaxItems", DBslotMaxItems, responseslotMaxItems);
		funLibrary.validate_Equals("slotMOVForFreeShipCharge", DBslotMOVForFreeShipCharge,
				responseslotMOVForFreeShipCharge);
		funLibrary.validate_Equals("slotShipCharge", DBslotShipCharge, responseslotShipCharge);

	}

	@Test(description = "RD Customermov")
	public void customermovRD() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID", "WINDOW_START_TIME like '%"
				+ funLibrary.getAnyDate(1, "yyyy-MM-dd") + "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
				+ FunLibrary.excelData.get("collectionPointId") + "'and service_type = 'RD' and COLLECTIONPOINT_NAME='"
				+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >0");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"RD", "123", FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		funLibrary.validate_Equals(" Customer MOV RD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "RD winodmov")
	public void windowmovRD() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'RD' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >0");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"RD", "123", FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Wndow MOV RD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "RD site")
	public void sitemovRD() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'RD' and minimum_order_value is null");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"RD", "123", FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Site MOV RD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "CC AC01- Verify XORDERATTR table updated")
	public void validSlotDetailsUpateinDBCC() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "' and service_type = 'CC' and DELIVERY_RESTRICTION_ID1 NOT IN('')");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"CC", "123", FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseisColesPlusMember = jsonPath.getString("isColesPlusMember");
		String responseslotMaxItems = jsonPath.getString("slotMaxItems");

		Double responseslotMOVForFreeShipCharges = jsonPath.getDouble("slotMOVForFreeShipCharge");
		String responseslotMOVForFreeShipCharge = Double.toString(responseslotMOVForFreeShipCharges);

		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		Double responseslotShipCharges = jsonPath.getDouble("slotShipCharge");
		String responseslotShipCharge = Double.toString(responseslotShipCharges);

		String responeslotRestriction = jsonPath.getString("slotRestriction[0]");
		System.out.println("responseslotMaxItems" + " " + responseslotMaxItems);
		System.out.println("responseisColesPlusMember" + " " + responseisColesPlusMember);
		System.out.println("responseslotMOVForFreeShipCharge" + " " + responseslotMOVForFreeShipCharge);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);
		System.out.println("responseslotShipCharge" + " " + responseslotShipCharge);
		System.out.println("responeslotRestriction" + " " + responeslotRestriction);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		String DBisColesPlusMember = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='isColesPlusMember'");
		String DBslotMaxItems = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMaxItems'");
		String DBslotMOVForFreeShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMOVForFreeShipCharge'");
		String DBslotRestriction = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='DMDeliverySlotRestrictions'");
		String DBslotShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotShipCharge'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		System.out.println(" DBisColesPlusMember" + "" + DBisColesPlusMember);
		System.out.println(" DBslotMaxItems" + "" + DBslotMaxItems);
		System.out.println("DBslotMOVForFreeShipCharge" + "" + DBslotMOVForFreeShipCharge);
		System.out.println(" DBslotRestriction" + "" + DBslotRestriction);
		System.out.println("DBslotShipCharge" + "" + DBslotShipCharge);

		funLibrary.validate_Equals(" MOV", DBoverallMOV, responseoverallMOV);
		funLibrary.validate_Equals("Coles Plus Member", DBisColesPlusMember, responseisColesPlusMember);
		funLibrary.validate_Contains("DMRestriction", DBslotRestriction, responeslotRestriction);
		funLibrary.validate_Contains("slotMaxItems", DBslotMaxItems, responseslotMaxItems);
		funLibrary.validate_Equals("slotMOVForFreeShipCharge", DBslotMOVForFreeShipCharge,
				responseslotMOVForFreeShipCharge);
		funLibrary.validate_Equals("slotShipCharge", DBslotShipCharge, responseslotShipCharge);

	}

	@Test(description = "CC Customermov")
	public void customermovCC() {
/*
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID", "WINDOW_START_TIME like '%"
				+ funLibrary.getAnyDate(1, "yyyy-MM-dd") + "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
				+ FunLibrary.excelData.get("collectionPointId") + "'and service_type = 'CC' and COLLECTIONPOINT_NAME='"
				+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >1");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"CC", "123", FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Customer MOV CC", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "CC winodmov")
	public void windowmovCC() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and service_type = 'CC' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and minimum_order_value >2");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"CC", "123", FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Window MOV CC", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "CC site")
	public void sitemovCC() {

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId")
						+ "'and service_type = 'CC' and minimum_order_value is null");
		dbUtil_DM.closeDBConnection();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"CC", "123", FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Site MOV CC", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "HD AC01- Verify XORDERATTR table updated")
	public void validSlotDetailsUpateinDBHD() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD' and DELIVERY_RESTRICTION_ID1 NOT IN('')");
		dbUtil_DM.closeDBConnection();
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID",
				"member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and NICKNAME='" + FunLibrary.excelData.get("NickName")
						+ "' and STATUS='P' and ADDRESSTYPE='SB'");
		String ccpAddressId = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and ADDRESS_ID='" + db_AddressId + "'");
		// dbUtil_1.closeDBConnection();
/*
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));*/

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"HD", ccpAddressId, FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseisColesPlusMember = jsonPath.getString("isColesPlusMember");
		String responseslotMaxItems = jsonPath.getString("slotMaxItems");

		Double responseslotMOVForFreeShipCharges = jsonPath.getDouble("slotMOVForFreeShipCharge");
		String responseslotMOVForFreeShipCharge = Double.toString(responseslotMOVForFreeShipCharges);

		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		Double responseslotShipCharges = jsonPath.getDouble("slotShipCharge");
		String responseslotShipCharge = Double.toString(responseslotShipCharges);

		String responeslotRestriction = jsonPath.getString("slotRestriction[0]");
		System.out.println("responseslotMaxItems" + " " + responseslotMaxItems);
		System.out.println("responseisColesPlusMember" + " " + responseisColesPlusMember);
		System.out.println("responseslotMOVForFreeShipCharge" + " " + responseslotMOVForFreeShipCharge);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);
		System.out.println("responseslotShipCharge" + " " + responseslotShipCharge);
		System.out.println("responeslotRestriction" + " " + responeslotRestriction);
		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);
		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		String DBisColesPlusMember = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='isColesPlusMember'");
		String DBslotMaxItems = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMaxItems'");
		String DBslotMOVForFreeShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotMOVForFreeShipCharge'");
		String DBslotRestriction = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='DMDeliverySlotRestrictions'");
		String DBslotShipCharge = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='slotShipCharge'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		System.out.println(" DBisColesPlusMember" + "" + DBisColesPlusMember);
		System.out.println(" DBslotMaxItems" + "" + DBslotMaxItems);
		System.out.println("DBslotMOVForFreeShipCharge" + "" + DBslotMOVForFreeShipCharge);
		System.out.println(" DBslotRestriction" + "" + DBslotRestriction);
		System.out.println("DBslotShipCharge" + "" + DBslotShipCharge);
		funLibrary.validate_Equals(" MOV", DBoverallMOV, responseoverallMOV);
		funLibrary.validate_Equals("Coles Plus Member", DBisColesPlusMember, responseisColesPlusMember);
		funLibrary.validate_Contains("DMRestriction", DBslotRestriction, responeslotRestriction);
		if(DBslotMaxItems=="No Record Found")
		{
			funLibrary.validate_Equals("maxitems", null,  responseslotMaxItems);

		}
		else
		{
			funLibrary.validate_Contains("slotMaxItems", DBslotMaxItems,responseslotMaxItems);
		}
		
		funLibrary.validate_Equals("slotMOVForFreeShipCharge", DBslotMOVForFreeShipCharge,
				responseslotMOVForFreeShipCharge);
		funLibrary.validate_Equals("slotShipCharge", DBslotShipCharge, responseslotShipCharge);

	}

	@Test(description = "HD Customermov")
	public void customermovHD() {
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

		/*Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));*/
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"HD", ccpAddressId, FunLibrary.excelData.get("collectionPointId")));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);

		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		funLibrary.validate_Equals(" Customer MOV HD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "HD winodmov")
	public void windowmovHD() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		dbUtil_DM.closeDBConnection();
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID",
				"member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and NICKNAME='" + FunLibrary.excelData.get("NickName")
						+ "' and STATUS='P' and ADDRESSTYPE='SB'");
		String ccpAddressId = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and ADDRESS_ID='" + db_AddressId + "'");
	/*	Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));*/

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"HD", ccpAddressId, FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);

		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Window MOV HD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "HD site")
	public void sitemovHD() {
		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD' and minimum_order_value is null");
		dbUtil_DM.closeDBConnection();
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_AddressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID",
				"member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and NICKNAME='" + FunLibrary.excelData.get("NickName")
						+ "' and STATUS='P' and ADDRESSTYPE='SB'");
		String ccpAddressId = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and ADDRESS_ID='" + db_AddressId + "'");
	/*	Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));*/

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"HD", ccpAddressId, FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		Double responseoverallMOVs = jsonPath.getDouble("overallMOV");
		String responseoverallMOV = Double.toString(responseoverallMOVs);
		System.out.println("responseoverallMOV" + " " + responseoverallMOV);

		int order = jsonPath.getInt("orderId");
		System.out.println("orderID" + " " + order);

		String DBoverallMOV = dbUtil.getValues("XORDERATTR", "VALUE",
				"orders_id='" + order + "' and NAME='overallMOV'");
		System.out.println(" DBoverallMOV" + "" + DBoverallMOV);
		// Assert.assertEquals(responseoverallMOV, DBoverallMOV);
		funLibrary.validate_Equals(" Site MOV HD", DBoverallMOV, responseoverallMOV);

	}

	@Test(description = "AC03- Verify error message if no service is selected")
	public void ValidatesErrorcodeforNoServiceType() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));

		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();

		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));

	}
	//***  Test case is not required as change in functionality************************

/*	@Test(description = "AC04- Verify error message if no ccpAddressId is passed")
	public void ValidatesErrorCodefornoccpAddressId() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));

		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));

	}*/

	@Test(description = "AC05- Verify error message if no slot is unavailable")
	public void ValidatesErrorcodeforNoSlotavailabel() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));

		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();

		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));

	}

	@Test(description = "InvalidccpAddressID")
	public void validatesforIncorrectccpAddressId() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
						+ "' and service_type = 'HD'");
		dbUtil_DM.closeDBConnection();

/*		Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));*/

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"HD", "rt600afc64-6f70-4c13-bae2-4c6a9ba42076", FunLibrary.excelData.get("collectionPointId")));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));

	}

	@Test(description = "InvalidccollectionPointId")
	public void validatesforIncorrectcollectionPointId() {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
						+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME='"
						+ FunLibrary.excelData.get("collectionPointId") + "' and service_type = 'CC'");
		dbUtil_DM.closeDBConnection();
		/*Payload.localizationByAddressId(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				"20510", FunLibrary.excelData.get("colAddressId"));
		Payload.localizationBySuburb(FunLibrary.excelData.get("Access-Token"), FunLibrary.excelData.get("JWT-Token"),
				FunLibrary.excelData.get("storeId"), FunLibrary.excelData.get("PostCode"),
				FunLibrary.excelData.get("Suburb"));
*/
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header5_Key"), FunLibrary.excelData.get("Header5_Value"));

		restLibrary.addBody(SlotReservationPayload.psReservationPayload(FunLibrary.excelData.get("storeId"), slotId,
				"CC", "123", "0404KT0404"));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
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
