package ps.savePaymentMethod.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PS_SavePaymentMethodPayPal extends Base_Class_API implements ITest {
	RestLibrary restLibrary;
	Response response;
	
	public void savePayment_PayPal_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_PS(
        		Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userAuthorization"),
                FunLibrary.excelData.get("RequestBodyPayPal"),
                FunLibrary.excelData.get("QueryParam_ver"));        	
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	public void savePayment_SavedPayPal_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_PS(
        		Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userAuthorization"),
                FunLibrary.excelData.get("RequestBodySavedPayPal"),
                FunLibrary.excelData.get("QueryParam_ver"));        	
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	
	// Test Cases for PayPal
	
		@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
		public void validateValidErrorCodesPayPal(String testname) {
			savePayment_PayPal_API();
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			switch (FunLibrary.excelData.get("StatusCode")) {
			
			case "404":
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				break;
				
			default:
				testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
				break;
			}
			funLibrary.Assert.assertAll();
			
		}
			
			@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
			public void validateInvalidErrorCodesPayPal(String testname) {
				savePayment_PayPal_API();
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				switch (FunLibrary.excelData.get("StatusCode")) {
				
				case "400":
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
					break;
					
				case "500":
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
		public void validateFunctionalErrorCodesPayPal(String testname) {
			savePayment_PayPal_API();
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

			switch (FunLibrary.excelData.get("StatusCode")) {
			
			case "401":
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
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
		
		// Test Cases for SavedPayPal
		
			@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
			public void validateValidErrorCodesSavedPayPal(String testname) {
				savePayment_SavedPayPal_API();
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				switch (FunLibrary.excelData.get("StatusCode")) {
				
				case "404":
					funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
					break;
					
				default:
					testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
					break;
				}
				funLibrary.Assert.assertAll();
				
			}
				
				@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
				public void validateInvalidErrorCodesSavedPayPal(String testname) {
					savePayment_SavedPayPal_API();
					funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
					switch (FunLibrary.excelData.get("StatusCode")) {
					
					case "400":
						funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
						funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
						funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
						break;
						
					case "500":
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
			public void validateFunctionalErrorCodesSavedPayPal(String testname) {
				savePayment_SavedPayPal_API();
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

				switch (FunLibrary.excelData.get("StatusCode")) {
				
				case "401":
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
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
			
			@Test()
			public void PayPalpaymentNameMissing() {
				savePayment_PayPal_API();	
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			}
			
			@Test()
			public void SavedPayPalpaymentNameMissing() {
				savePayment_SavedPayPal_API();
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			}
			
	
	
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
