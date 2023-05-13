package ps.getslotbyaddress.api.tests;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slotbyaddress.api.response.pojo.HD;
import slotbyaddress.api.response.pojo.Root;

public class PS_byAddressPrivate extends Base_Class_API implements ITest {
	static String jsonId = "";

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01- Verify daysection")
	public void validateDaySection() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String date = funLibrary.getDateAddDays(1);

		List<String> Id = dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " + "window_end_time like '%" + date + "%' and DELZONE_NAME IN ('0450HD', '0450PD') and PARTNER_ID NOT IN ('BG01') and SERVICE_TYPE='HD'");

		String daySection = "";

		for (int i = 0; i < Id.size(); i++) {

			String id = Id.get(i);

			String dateDb = dbUtil.getValues("delwindow", "WINDOW_START_TIME", "ID='" + id + "'");

			String startTime = dateDb;

			startTime = funLibrary.storeTimeAfterWhitespace(startTime);

			String[] time = startTime.split(":");
			int windowStartTimeHour = Integer.parseInt(time[0].trim());

			if(windowStartTimeHour >= 0 && windowStartTimeHour <= 5) {
				daySection = "Overnight";
			} else if(windowStartTimeHour >= 6 && windowStartTimeHour <= 11) {
				daySection = "Morning";
			} else if(windowStartTimeHour >= 12 && windowStartTimeHour <= 15) {
				daySection = "Afternoon";
			} else if(windowStartTimeHour >= 16) {
				daySection = "Evening";
			}

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			HD hdSlot = root.getSlots().gethD().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("daySection", daySection, hdSlot.getDaySection());

			funLibrary.Assert.assertAll();

		}

	}

	@Test(description = "AC02- Verify maximum items")
	public void validateMaximumItems() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String date = funLibrary.getDateAddDays(1);

		List<String> Id = dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " + "window_end_time like '%" + date + "%' and DELZONE_NAME IN ('0450HD', '0450PD') and PARTNER_ID NOT IN ('BG01') and SERVICE_TYPE='HD'");

		for (int i = 0; i < Id.size(); i++) {

			String id = Id.get(i);

			String maxitem = dbUtil.getValues("delwindow", "MAX_ITEMS", "ID='" + id + "'");

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			HD hdSlot = root.getSlots().gethD().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("maxitem", maxitem, hdSlot.getMaxItems());

			funLibrary.Assert.assertAll();

		}

	}

	@Test(description = "Get slot Id")
	public void slotIdGet() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPathId = response.jsonPath();
		jsonId = jsonPathId.getString("slots.HD[0].id");

	}

	@Test(description = "AC03- Verify slot Id is reserved")
	public void validateReservedSlotId() {
		//Payload.localizationBySuburb(FunLibrary.excelData.get("AccessToken"), FunLibrary.excelData.get("UserJWT"), FunLibrary.excelData.get("StoreId"), FunLibrary.excelData.get("Postcode"), FunLibrary.excelData.get("Suburb"));
		Payload.slotreservationAddres_PS(FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("Header1_Value"), jsonId);
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String valueDB = dbUtil.getValues("xorderattr", "VALUE", "value='" + jsonId + "'");
		System.out.println(valueDB);
		funLibrary.validate_Equals("Id", jsonId, valueDB);

		JsonPath jsonPathslotId = response.jsonPath();
		String jsonslotId = jsonPathslotId.getString("slots.HD[0].slotIdSelected");

		funLibrary.validate_Equals("slotIdSelected", jsonslotId, "true");

		funLibrary.Assert.assertAll();

	}

	@Test(description = "AC04- Verify error code COLRS-ERR-BAD-REQ-004 when incorrect windowtype")
	public void validateIncorrectWindowType() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();

	}

	@Test(description = "AC05- Verify incorrect postcode")
	public void validateIncorrectPostcode() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();

	}

	@Test(description = "AC06- Verify incoorect length of postcode")
	public void validateInvalidPostcodeLength() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();

	}

	@Test(description = "AC07- Verify error code COLRS-ERR-BAD-REQ-004 when country code is not 'AU'")
	public void validateIncorrectCountryCode() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();

	}
	/*
	 * @Test(description = "AC08- Verify error code COLRS-ERR-BAD-REQ-004 when State value is incorrect") public void validateIncorrectStateValue() {
	 * RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
	 * FunLibrary.excelData.get("RequestType")); restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"),
	 * FunLibrary.excelData.get("Header1_Value")); restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"),
	 * FunLibrary.excelData.get("Header2_Value")); restLibrary.addBody(FunLibrary.excelData.get("RequestBody")); Response response =
	 * restLibrary.executeAPI(); restLibrary.getResponseBody(response); funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	 * funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
	 * funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
	 * funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
	 * funLibrary.Assert.assertAll();
	 * 
	 * }
	 */

	@Test(description = "(ST-1486) AC01- Verify movFreeShipCharge if user is Coles Plus Member")
	public void validateMovFreeShipChargeColesPlusMember() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		// DatabaseUtilities dbUtil = new DatabaseUtilities();
		JsonPath jsonPath = response.jsonPath();

		String isColesPlusMember = jsonPath.getString("isColesPlusMember");

		Assert.assertEquals(isColesPlusMember, "true");

		String movFreeShipChargeJson = jsonPath.getString("slots.HD[0].movForFreeShipCharge");

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String movFreeShipChargeDbColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.HD.qualifiedamount'");
		String movFreeShipChargeDbNonColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.HD.qualifiedamount'");

		if(movFreeShipChargeJson != movFreeShipChargeDbNonColesPlus) {

			Assert.assertTrue(true);

		}

		funLibrary.validate_Equals("movFreeShipCharge", movFreeShipChargeJson, movFreeShipChargeDbColesPlus);
		funLibrary.Assert.assertAll();

	}

	@Test(description = "(ST-1486)AC02- Verify movFreeShipCharge if user is Non Coles Plus Member")
	public void validateMovFreeShipChargeNonColesPlusMember() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();

		String isColesPlusMember = jsonPath.getString("isColesPlusMember");

		Assert.assertEquals(isColesPlusMember, "false");

		String movFreeShipChargeJson = jsonPath.getString("slots.HD[0].movForFreeShipCharge");

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String movFreeShipChargeDbColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.HD.qualifiedamount'");
		String movFreeShipChargeDbNonColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.HD.qualifiedamount'");

		if(movFreeShipChargeJson != movFreeShipChargeDbColesPlus) {

			Assert.assertTrue(true);

		}

		funLibrary.validate_Equals("movFreeShipCharge", movFreeShipChargeJson, movFreeShipChargeDbNonColesPlus);
		funLibrary.Assert.assertAll();

	}

	@Test(description = "(ST-1486) AC03- Verify ship charge saving")
	public void validateShipChargeSaving() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		JsonPath jsonPath = response.jsonPath();
		String shipChargeSavingjson = jsonPath.getString("slots.HD[0].shipChargeSaving");
		double ConvertshipChargeSavingjson = Double.parseDouble(shipChargeSavingjson);

		String movForshipChargejson = jsonPath.getString("slots.HD[0].movForFreeShipCharge");
		double ConvertmovForshipChargejson = Double.parseDouble(movForshipChargejson);
		String productValueData = FunLibrary.excelData.get("Productvalue");
		double productValue = Double.parseDouble(productValueData);

		if(productValue > ConvertmovForshipChargejson) {

			Assert.assertTrue(true);

		}

		String shipChargejson = jsonPath.getString("slots.HD[0].shipCharge");
		double ConvertshipChargejson = Double.parseDouble(shipChargejson);
		Assert.assertEquals(ConvertshipChargeSavingjson, ConvertshipChargejson);
		funLibrary.Assert.assertAll();

	}

	@Test(description = "ST-1628 AC04- Verify display end time")
	public void validateDisplayEndTime() throws ParseException {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		long threshold = Long.parseLong(dbUtil.getValues("x_coles_config", "VALUE", "NAME='delivery.slots.displayEndTime.threshold'"));
		long thresholdTime = 0;

		Root root = (Root) restLibrary.getResponseBody(response, Root.class);
		long slotCount = root.getSlots().gethD().stream().count();

		for (int i = 0; i < slotCount; i++) {

			HD slot = root.getSlots().gethD().get(i);
			String winType = slot.getWindowType();
			if(winType.equalsIgnoreCase("PARTNERRAPID") || winType.equalsIgnoreCase("RAPID")) {
				thresholdTime = 0;
			} else {
				thresholdTime = threshold;
			}

		String cutOffTimeUtc = slot.getCutoffTime().getUtc().toString().replace('T', ' ');
		String cutOffTimeLocal = slot.getCutoffTime().getLocal().toString().replace('T', ' ');
		String displayEndTimeUtc = slot.getDisplayEndTime().getUtc().toString().replace('T', ' ');
		String displayEndTimeLocal = slot.getDisplayEndTime().getLocal().toString().replace('T', ' ');
		Timestamp cutOffUtc = Timestamp.valueOf(cutOffTimeUtc);
		Timestamp displayEndUtc = Timestamp.valueOf(displayEndTimeUtc);

		long longCutOffTimeUtc = cutOffUtc.getTime();
		long longDisplayEndTimeTimeUtc = displayEndUtc.getTime();
		long resultantTimeUtc = (longCutOffTimeUtc - longDisplayEndTimeTimeUtc) / 60000;

		Assert.assertEquals(thresholdTime, resultantTimeUtc);

		Timestamp cutOffLocal = Timestamp.valueOf(cutOffTimeLocal);
		Timestamp displayEndLocal = Timestamp.valueOf(displayEndTimeLocal);

		long longCutOffTimeLocal = cutOffLocal.getTime();
		long longDisplayEndTimeTimeLocal = displayEndLocal.getTime();
		long resultantTimeLocal = (longCutOffTimeLocal - longDisplayEndTimeTimeLocal) / 60000;

		Assert.assertEquals(thresholdTime, resultantTimeLocal);

		}
		
		funLibrary.Assert.assertAll();

	}

	/*
	 * //@Test(description = "ST-4794 AC01- Verify bagging options") public void validateBaggingOption_Normal() {
	 * 
	 * RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
	 * FunLibrary.excelData.get("RequestType")); restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"),
	 * FunLibrary.excelData.get("Header1_Value")); restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"),
	 * FunLibrary.excelData.get("Header2_Value")); restLibrary.addBody(FunLibrary.excelData.get("RequestBody1")); Response response =
	 * restLibrary.executeAPI(); restLibrary.getResponseBody(response); funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	 * DatabaseUtilities dbUtil = new DatabaseUtilities("DM"); String date = funLibrary.getCurrentDate();
	 * 
	 * List <String> Id=dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " +
	 * "window_end_time like '%" + date + "%' and DELZONE_NAME = '0404HD' and PARTNER_ID NOT IN ('BG01') and SERVICE_TYPE='HD'");
	 * 
	 * 
	 * for(int i=0; i<Id.size(); i++) {
	 * 
	 * String id = Id.get(i);
	 * 
	 * String baggingOption = dbUtil.getValues("delsubzone", "HD_BAGGING_OPTIONS", "suburb_id='10803' and delzone_name='0404HD'");
	 * 
	 * List <String> baggingOptionList = new ArrayList<>(); baggingOptionList.add("0"); baggingOptionList.add("1"); baggingOptionList.add("2");
	 * baggingOptionList.add("3");
	 * 
	 * if(baggingOptionList.contains(baggingOption)) {
	 * 
	 * Root root = (Root) restLibrary.getResponseBody(response, Root.class);
	 * 
	 * HD hdSlot = root.getSlots().gethD().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	 * 
	 * funLibrary.validate_Equals("baggingOption", baggingOption, Integer.toString(hdSlot.getBaggingOption()));
	 * 
	 * funLibrary.Assert.assertAll();
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * baggingOption = "2"; Root root = (Root) restLibrary.getResponseBody(response, Root.class); HD hdSlot = root.getSlots().gethD().stream().filter(x ->
	 * x.getId().equalsIgnoreCase(id)).findFirst().orElse(null); funLibrary.validate_Equals("baggingOption", baggingOption,
	 * Integer.toString(hdSlot.getBaggingOption())); funLibrary.Assert.assertAll();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * //@Test(description = "ST-4794 AC01- Verify bagging options") public void validateBaggingOption_PD() {
	 * 
	 * RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
	 * FunLibrary.excelData.get("RequestType")); restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"),
	 * FunLibrary.excelData.get("Header1_Value")); restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"),
	 * FunLibrary.excelData.get("Header2_Value")); restLibrary.addBody(FunLibrary.excelData.get("RequestBody1")); Response response =
	 * restLibrary.executeAPI(); restLibrary.getResponseBody(response); funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	 * DatabaseUtilities dbUtil = new DatabaseUtilities("DM"); String date = funLibrary.getCurrentDate();
	 * 
	 * List<String> Id = dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " +
	 * "window_end_time like '%" + date + "%' and DELZONE_NAME = '0404PD' and PARTNER_ID NOT IN ('BG01') and SERVICE_TYPE='HD'");
	 * 
	 * 
	 * for(int i=0; i<Id.size(); i++) {
	 * 
	 * String id = Id.get(i);
	 * 
	 * String baggingOption = dbUtil.getValues("delsubzone", "HD_BAGGING_OPTIONS", "suburb_id='10803' and delzone_name='0404PD'");
	 * 
	 * List <String> baggingOptionList = new ArrayList<>(); baggingOptionList.add("0"); baggingOptionList.add("1"); baggingOptionList.add("2");
	 * baggingOptionList.add("3");
	 * 
	 * if(baggingOptionList.contains(baggingOption)) {
	 * 
	 * Root root = (Root) restLibrary.getResponseBody(response, Root.class);
	 * 
	 * HD hdSlot = root.getSlots().gethD().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	 * 
	 * funLibrary.validate_Equals("baggingOption", baggingOption, Integer.toString(hdSlot.getBaggingOption()));
	 * 
	 * funLibrary.Assert.assertAll();
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * baggingOption = "2"; Root root = (Root) restLibrary.getResponseBody(response, Root.class); HD hdSlot = root.getSlots().gethD().stream().filter(x ->
	 * x.getId().equalsIgnoreCase(id)).findFirst().orElse(null); funLibrary.validate_Equals("baggingOption", baggingOption,
	 * Integer.toString(hdSlot.getBaggingOption())); funLibrary.Assert.assertAll();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public String getTestName() {
		return testName.get();
	}
}
