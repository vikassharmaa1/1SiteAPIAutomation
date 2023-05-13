package wcs.orderSubmit.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class OrderSubmit extends Base_Class_API implements ITest {

	RestLibrary restLibrary ;
	Response response;

	public void OrderSubmit_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.orderSubmit_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"),
				FunLibrary.excelData.get("PathParam_storeId"), FunLibrary.excelData.get("Body"), FunLibrary.excelData.get("QueryParam_ver"));
		// get the response body and print on console
		restLibrary.getResponseBody(response);
	}

	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodes(String testname) {
		OrderSubmit_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateInvalidErrorCodes(String testname) {
		OrderSubmit_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functional", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		OrderSubmit_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;

		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "col-token", FunLibrary.excelData.get("colToken"));
			break;

		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate that order submit thorws error if called before process api")
	public void validateProcessAPIError() {
		restLibrary = new RestLibrary();
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("User"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"));

		restLibrary.getResponseBody(APILibrary.addItem_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"), "3227410", "5", "false"));

		// Localizing to HD
		restLibrary.getResponseBody(APILibrary.localizationBySuburbPostCode_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "20509", "1", "3121", "RICHMOND"));
		restLibrary.getResponseBody(APILibrary.localizationBySuburbPostCode_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "20501", "1", "0820", "Bayview"));

		// Get slot id and ccp address id
		String slotId = funLibrary.getSlotID("HD", "0404HD", "", 1);
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("User"), "Home");

		// reserve slot
		  restLibrary.getResponseBody(APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId));

		OrderSubmit_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorKey", FunLibrary.excelData.get("ErrorKey"));
		
	}

	@Test()
	public void validateInvalidPaymentError() {
		restLibrary = new RestLibrary();
		APILibrary.EmptyTrolley(FunLibrary.excelData.get("user"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"));

		APILibrary.addItem_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"), "3227410", "5", "false");

		// Localizing to HD
		APILibrary.localizationBySuburbPostCode_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "20509", "1", "3121", "RICHMOND");
		APILibrary.localizationBySuburbPostCode_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), "20501", "1", "0820", "Bayview");

		// Get slot id and ccp address id
		String slotId = funLibrary.getSlotID("HD", "0404HD", "", 1);
		String ccpAddressId = funLibrary.getCCPAddressID(FunLibrary.excelData.get("User"), "Home");

		// reserve slot
		APILibrary.reserveSlot("0404", "1", slotId, "HD", "DM-SHIFT", "0404CC0404", ccpAddressId);

		// call process API
		APILibrary.orderProcess_WCS("", "", "", FunLibrary.excelData.get("Header_access-token"), FunLibrary.excelData.get("Header_user-jwt-token"), FunLibrary.excelData.get("PathParam_storeId"), "1");

		OrderSubmit_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorKey", FunLibrary.excelData.get("ErrorKey"));
	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
