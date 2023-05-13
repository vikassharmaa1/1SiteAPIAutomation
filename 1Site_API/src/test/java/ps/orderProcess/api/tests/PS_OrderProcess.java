package ps.orderProcess.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.SlotReservationPayload;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_OrderProcess extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;
	public void orderProcess_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.orderProcessPS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userauthorization"),                    
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		orderProcess_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
			
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
			
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		orderProcess_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
			
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "col-token", FunLibrary.excelData.get("colToken"));
			break;
			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test()
	public void validOrderProcess() {
		restLibrary = new RestLibrary();
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("User"), FunLibrary.excelData.get("Header_userauthorization"),"0404");
		
		APILibrary.addItem_PS("", "", "", FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"),"0404","3227410", "5", "false");

		// Localizing to HD
		APILibrary.localizationBySuburbPostCode_PS("", "", "", FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"), "20509", "3121", "RICHMOND");
		APILibrary.localizationBySuburbPostCode_PS("", "", "", FunLibrary.excelData.get("Header_authorization"), FunLibrary.excelData.get("Header_userauthorization"), "20501", "0820", "Bayview");
//		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
//		String slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
//				"WINDOW_START_TIME like '%" + funLibrary.getAnyDate(1, "yyyy-MM-dd")
//						+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + FunLibrary.excelData.get("DELZONE_NAME")
//						+ "' and service_type = 'HD' and MAXIMUM_SLOTS > 50");
//		dbUtil_DM.closeDBConnection();
		
		// Get slot id and ccp address id
				String slotId = funLibrary.getSlotID("HD", "0450HD", "", 1);
				String ccpAddressId = FunLibrary.getCCPAddressID(FunLibrary.excelData.get("User"), "Home");

		// reserve slot
		APILibrary.reserveSlot_PS("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);
		// reserve slot
		//restLibrary.getResponseBody(APILibrary.reserveSlot_PS("0404", "1", slotId, "HD", "DM-SHIFT", FunLibrary.excelData.get("collectionPointId"), ccpAddressId));
		
		// call process API
		orderProcess_API();
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}

}
