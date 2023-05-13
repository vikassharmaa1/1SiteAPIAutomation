package apim.nativeInvoice.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class APIM_nativeInvoicePDF extends Base_Class_API implements ITest {

	RestLibrary restLibrary;
	Response response;
	public void nativeInvoicepdf() {
        restLibrary = new RestLibrary();
        response = APILibrary.invoice_APIM(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_userauthorization"),
                FunLibrary.excelData.get("Header_authorization"),    
                FunLibrary.excelData.get("Header_subscriptionKey"),
                FunLibrary.excelData.get("fileName"),
                FunLibrary.excelData.get("format"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "message", "Resource not found");
			break;
			
		case "400":
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
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
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
	
	@Test()
	public void validateMissingFiletype() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test()
	public void validateIncorrectOrderId() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test()
	public void validNativeInvoicepdf() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test()
	public void validateIncorrectfilename() {
		restLibrary = new RestLibrary();
		nativeInvoicepdf();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}
}
