package ps.slots.api.tests;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.rest.api.PS;
import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.Reporting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slotbyaddress.api.response.pojo.HD;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class PS_GetSlotsByLocationPrivate extends Base_Class_API implements ITest{
	
	static String jsonId = "";

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
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
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "AC01- Verify day section")
	public void validateDaySection() {
		
		String statusCode = FunLibrary.excelData.get("StatusCode");
		String date = null;
		List <String> Id = new ArrayList<String>() ;
		int a=0;
		Boolean slotAval=false;
		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		for (a=0;a<=6;++a) {
			date = funLibrary.getDateAddDays(a);
			Id=dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + 
		     		   "window_start_time like '%"+date+"%' and " + 
		     		   "window_end_time like '%"+date+"%'and COLLECTIONPOINT_NAME = '0404CC0404'");
			
			if(!Id.isEmpty()){
				slotAval=true;
				break;
			}
		}
		
		if(!slotAval) {
			statusCode = "204";
			
		}
		
		RestLibrary restLibrary = new RestLibrary();
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		Response response = null;
		//String daysFwd = Integer.toString(a);
		response = PS.getSlotByLocationDate("", "", "", authorization, userAuthorization, "0404", "0404CC0404", date, date, "ALL");
		testLog.info("Date "+date);
			funLibrary.validateStatusCode(response, statusCode);
			if(statusCode.equals("200")) {
			String daySection = "";
			testLog.info("Value of Id.size: "+Id.size());
			Reporting.test.get().log(Status.INFO, String.format("Number of available slots: "+Id.size()));
			for(int i=0; i<Id.size(); i++) {
			
			String id = Id.get(i);
			testLog.info("Value of id: "+id);
			Reporting.test.get().log(Status.INFO, String.format("Verifying for the slot: "+id));
			String dateDb = dbUtil.getValues("delwindow", "WINDOW_START_TIME", "ID='" + id + "'");
			
			String startTime = dateDb;
			
			startTime= funLibrary.storeTimeAfterWhitespace(startTime);
					
			String[] time = startTime.split ( ":" );
			int windowStartTimeHour = Integer.parseInt ( time[0].trim() );
			Reporting.test.get().log(Status.INFO, String.format("Value of windowStartTimeHour: "+windowStartTimeHour));
			testLog.info("Value of windowStartTimeHour: "+windowStartTimeHour);
			
			if(windowStartTimeHour >=0 && windowStartTimeHour <= 5)
			{
				daySection="Overnight";
			}
			else if(windowStartTimeHour >=6 && windowStartTimeHour <= 11)
			{
				daySection="Morning";
			}
			else if(windowStartTimeHour >=12 && windowStartTimeHour <= 15)
			{
				daySection="Afternoon";
			}
			else if(windowStartTimeHour >=16 )
			{
				daySection="Evening";
			}
			
			
			Root root = (Root) restLibrary.getResponseBody(response, Root.class);		
			
			CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
			
			funLibrary.validate_Equals("daySection", daySection, ccSlot.getDaySection());
			funLibrary.Assert.assertAll();
			}
		}
			
			else {
				testLog.info("No slots found till next 7 days hence returning 204");
			}
	}
		
	@Test(description = "AC02- Verify movFreeShipCharge if user is Coles Plus Member")
	public void validateMovFreeShipChargeColesPlusMember() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		//DatabaseUtilities dbUtil = new DatabaseUtilities();
		JsonPath jsonPath = response.jsonPath() ;
		
		String isColesPlusMember = jsonPath.getString("isColesPlusMember") ;//user is coles plus member
		
		Assert.assertEquals(isColesPlusMember, "true");
		
		String movFreeShipChargeJson = jsonPath.getString("slots.CC[0].movForFreeShipCharge");
		
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String movFreeShipChargeDbColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.CC.qualifiedamount'");
		String movFreeShipChargeDbNonColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.CC.qualifiedamount'");
		
		if(movFreeShipChargeJson != movFreeShipChargeDbNonColesPlus) {
			
			Assert.assertTrue(true);
			
		}
		
		funLibrary.validate_Equals("movFreeShipCharge", movFreeShipChargeJson, movFreeShipChargeDbColesPlus);
		funLibrary.Assert.assertAll();
		
	}
	
		
	
	@Test(description = "AC03- Verify movFreeShipCharge if user is Non Coles Plus Member")
	public void validateMovFreeShipChargeNonColesPlusMember() {
		
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		//DatabaseUtilities dbUtil = new DatabaseUtilities();
		JsonPath jsonPath = response.jsonPath() ;
		
		String isColesPlusMember = jsonPath.getString("isColesPlusMember") ;//user is coles plus member
		
		Assert.assertEquals(isColesPlusMember, "false");
		
		String movFreeShipChargeJson = jsonPath.getString("slots.CC[0].movForFreeShipCharge") ;
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String movFreeShipChargeDbColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.CC.qualifiedamount'");
		String movFreeShipChargeDbNonColesPlus = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.CC.qualifiedamount'");
		
		if(movFreeShipChargeJson != movFreeShipChargeDbColesPlus) {
			
			Assert.assertTrue(true);
			
		}
		
		funLibrary.validate_Equals("movFreeShipCharge", movFreeShipChargeJson, movFreeShipChargeDbNonColesPlus);
		funLibrary.Assert.assertAll();
		
	}
	
	

	@Test(description = "AC04- Verify maximum items")
	public void validateMaximumItems() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String date = funLibrary.getCurrentDate();
		
		List <String> Id=dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + 
	     		   "window_start_time like '%"+date+"%' and " + 
	     		   "window_end_time like '%"+date+"%'and COLLECTIONPOINT_NAME = '0404CC0404'"); //List <String> getMultipleValuesList; and DELZONE_NAME='0404HD'
		
	
		for(int i=0; i<Id.size(); i++) {

		String id = Id.get(i);
			
		String maxitem = dbUtil.getValues("delwindow", "MAX_ITEMS", "ID='" + id + "'");
				
		Root root = (Root) restLibrary.getResponseBody(response, Root.class);		
		
		CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

		funLibrary.validate_Equals("maxitem", maxitem, ccSlot.getMaxItems());
		
		funLibrary.Assert.assertAll();
		
		}
		
	}
	
	
	@Test(description = "AC05- Verify ship charge saving")
	public void validateShipChargeSaving() {
				
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		JsonPath jsonPath = response.jsonPath() ;
		String shipChargeSavingjson = jsonPath.getString("slots.CC[0].shipChargeSaving");
		double ConvertshipChargeSavingjson=Double.parseDouble(shipChargeSavingjson);
		
		String movForshipChargejson = jsonPath.getString("slots.CC[0].movForFreeShipCharge");
		double ConvertmovForshipChargejson=Double.parseDouble(movForshipChargejson);
		String productValueData = FunLibrary.excelData.get("Productvalue");
		double productValue=Double.parseDouble(productValueData);
		
		if (productValue > ConvertmovForshipChargejson) {
			
			Assert.assertTrue(true);
			
		}
		
		String shipChargejson = jsonPath.getString("slots.CC[0].shipCharge");
		double ConvertshipChargejson=Double.parseDouble(shipChargejson);
		Assert.assertEquals(ConvertshipChargeSavingjson, ConvertshipChargejson);
		funLibrary.Assert.assertAll();
		
		
	}
	
	
	
	
	
	
	@Test(description = "Get slot Id")
	public void slotIdGet(){
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("SampleBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPathId = response.jsonPath();
		jsonId = jsonPathId.getString("slots.CC[0].id");
		
	}
	
	
	@Test(dependsOnMethods = { "slotIdGet" }, description = "AC06- Verify slot Id is reserved")
	public void validateReservedSlotId() {
				
		//Payload.localizationByCollection(FunLibrary.excelData.get("AccessToken"), FunLibrary.excelData.get("UserJWT"), FunLibrary.excelData.get("PathParamValue1"), FunLibrary.excelData.get("PathParamValue2"));
		 
		Payload.slotreservation_PS(FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("Header1_Value"), jsonId);
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("SampleBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String valueDB = dbUtil.getValues("xorderattr", "VALUE", "value='" + jsonId + "'");
		funLibrary.validate_Equals("Id", jsonId, valueDB);
		
		JsonPath jsonPathslotId = response.jsonPath();
		String jsonslotId = jsonPathslotId.getString("slots.CC[0].slotIdSelected") ;
		
		funLibrary.validate_Equals("slotIdSelected", jsonslotId, "true");
		
		funLibrary.Assert.assertAll();
		
		
		
	}
	
	
	
	@Test(description = "AC07- Verify error code COLRS-ERR-BAD-REQ-004")
	public void validateIncorrectWindowtype() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		//restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		
	}
	
	
	@Test(description = "ST-1628 AC02- Verify display end time")
	public void validateDisplayEndtime() throws ParseException {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
		FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		// restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
		// FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		long threshold = Long.parseLong(dbUtil.getValues("x_coles_config", "VALUE", "NAME='delivery.slots.displayEndTime.threshold'"));
		long thresholdTime = 0;
		Root root = (Root) restLibrary.getResponseBody(response, Root.class);	
		long slotCount = root.getSlots().getcC().stream().count();

		for(int i=0; i<slotCount;++i) {
			CC slot = root.getSlots().getcC().get(i);
			String winType = slot.getWindowType();
			
			if(winType.equalsIgnoreCase("CCRAPID")|| (winType.equals("") && slot.getDaySection().equals("ASAP")) ) {
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

	// Modification against ST-4794
	@Test(description = "ST-5782 Verify bagging option")
	public void validateBaggingOption() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
		FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
		String baggingOption = dbUtil.getValues("delcollection_point", "BAGGING_OPTIONS","COLLECTIONPOINT_NAME='0404CC0404'");

		JsonPath jsonPath = response.jsonPath();
		String baggingOptionJson = jsonPath.getString("slotBagging.slotBaggingBitFlag");

		funLibrary.validate_Equals("movFreeShipCharge", baggingOptionJson, baggingOption);
		funLibrary.Assert.assertAll();

	}
	
	
	
	
	/*
	@Test(description = "ST-4794 AC01- Verify bagging options")
    public void validateBaggingOption() {
           
           RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
           restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
           restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
           restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
           Response response = restLibrary.executeAPI();
           restLibrary.getResponseBody(response);
           funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
           DatabaseUtilities dbUtil = new DatabaseUtilities("DM");
           String date = funLibrary.getCurrentDate();
           
           List <String> Id=dbUtil.getMultipleValuesList("delwindow", "ID", "status = 'OPEN' and " + 
                    "window_start_time like '%"+date+"%' and " + 
                    "window_end_time like '%"+date+"%'and COLLECTIONPOINT_NAME = '0404CC0404'"); //List <String> getMultipleValuesList; and DELZONE_NAME='0404HD'
           
    
           for(int i=0; i<Id.size(); i++) {

           String id = Id.get(i);
                  
           String baggingOption = dbUtil.getValues("delcollection_point", "BAGGING_OPTIONS", "COLLECTIONPOINT_NAME='0404CC0404'");
           
           List <String> baggingOptionList = new ArrayList<>();
           baggingOptionList.add("0");
           baggingOptionList.add("1");
           baggingOptionList.add("2");
           baggingOptionList.add("3");
           
           if(baggingOptionList.contains(baggingOption)) {
           
           Root root = (Root) restLibrary.getResponseBody(response, Root.class);          
           
           CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

           funLibrary.validate_Equals("baggingOption", baggingOption, Integer.toString(ccSlot.getBaggingOption()));
           
           funLibrary.Assert.assertAll();
           
           }
           
           else {
        	   
        	   baggingOption = "2";
        	   Root root = (Root) restLibrary.getResponseBody(response, Root.class);
               CC ccSlot = root.getSlots().getcC().stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
        	   funLibrary.validate_Equals("baggingOption", baggingOption, Integer.toString(ccSlot.getBaggingOption()));
        	   funLibrary.Assert.assertAll();
        	   
           }
           
           
          }
           
    }*/
	
	


	@Override
	public String getTestName() {
		return testName.get();

	}

}
