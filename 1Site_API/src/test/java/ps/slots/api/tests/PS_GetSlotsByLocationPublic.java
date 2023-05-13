package ps.slots.api.tests;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class PS_GetSlotsByLocationPublic extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
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
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC02- Verify day section")
	public void validateDaySection() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String date = funLibrary.getCurrentDate();

		List<String> Id = dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " + "window_end_time like '%" + date + "%'and COLLECTIONPOINT_NAME = '0404CC0404'"); // List <String> getMultipleValuesList; and DELZONE_NAME='0404HD'

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

			CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("daySection", daySection, ccSlot.getDaySection());

			funLibrary.Assert.assertAll();

		}

	}

	@Test(description = "AC03- Verify error code COLRS-ERR-BAD-REQ-004")
	public void validateIncorrectWindowtype() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));

	}

	@Test(description = "AC04- Verify maximum items")
	public void validateMaximumItems() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String date = funLibrary.getCurrentDate();

		List<String> Id = dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + "window_start_time like '%" + date + "%' and " + "window_end_time like '%" + date + "%'and COLLECTIONPOINT_NAME = '0404CC0404'"); // List <String> getMultipleValuesList; and DELZONE_NAME='0404HD'

		for (int i = 0; i < Id.size(); i++) {

			String id = Id.get(i);

			String maxitem = dbUtil.getValues("delwindow", "MAX_ITEMS", "ID='" + id + "'");

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

			funLibrary.validate_Equals("maxitem", maxitem, ccSlot.getMaxItems());

			funLibrary.Assert.assertAll();

		}

	}

	@Test(description = "ST-1628 AC01- Verify display end time")
	public void validateDisplayEndtime() throws ParseException {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		long threshold = Long.parseLong(dbUtil.getValues("x_coles_config", "VALUE", "NAME='delivery.slots.displayEndTime.threshold'"));
		long thresholdTime = 0;
		Root root = (Root) restLibrary.getResponseBody(response, Root.class);
		long slotCount = root.getSlots().getcC().stream().count();

		for (int i = 0; i < slotCount; ++i) {
			CC slot = root.getSlots().getcC().get(i);
			String winType = slot.getWindowType();

			if(winType.equalsIgnoreCase("CCRAPID") || (winType.equals("") && slot.getDaySection().equals("ASAP"))) {
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

	@Override
	public String getTestName() {
		return testName.get();

	}

}
