package wcs.nativeInvoice.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class WCS_nativeInvoicePDF extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;
	
	public void nativeInvoicepdf() {
        restLibrary = new RestLibrary();
        response = APILibrary.invoice_WCS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_access-token"),
                FunLibrary.excelData.get("Header_user-jwt-token"),             
                FunLibrary.excelData.get("storeId"),
                FunLibrary.excelData.get("orderId"),
                FunLibrary.excelData.get("format"),
                FunLibrary.excelData.get("fileName"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodes(String testname) {
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("ErrorCode"));
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
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			break;
			
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
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
	
	@Test()
	public void validNativeInvoicepdf() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test()
	public void validateIncorrectOrderId() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	
	@Test()
	public void validateIncorrectfilename() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}

}