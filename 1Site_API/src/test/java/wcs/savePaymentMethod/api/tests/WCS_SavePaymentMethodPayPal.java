package wcs.savePaymentMethod.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class WCS_SavePaymentMethodPayPal extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;
	
	public void savePayment_PayPal_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_WCS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_access-token"),
                FunLibrary.excelData.get("Header_user-jwt-token"),             
                FunLibrary.excelData.get("PathParam_storeId"),
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_name_PayPal"),
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	public void savePayment_SavedPayPal_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_WCS(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_access-token"),
                FunLibrary.excelData.get("Header_user-jwt-token"),             
                FunLibrary.excelData.get("PathParam_storeId"),
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_name_SavedPayPal"),
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	//Test cases for PayPal
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodesPayPal(String testname) {
		savePayment_PayPal_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("DescriptionPayPal"));
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
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
			case "404":
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("DescriptionPayPal"));
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
	public void validateFunctionalErrorCodesPayPal(String testname) {
		savePayment_PayPal_API();
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
	
	//Test cases for SavedPayPal
	
		@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
		public void validateValidErrorCodesSavedPayPal(String testname) {
			savePayment_SavedPayPal_API();
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			switch (FunLibrary.excelData.get("StatusCode")) {
			case "404":
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("DescriptionSavedPayPal"));
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
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
				case "404":
					funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("DescriptionSavedPayPal"));
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
		public void validateFunctionalErrorCodesSavedPayPal(String testname) {
			savePayment_SavedPayPal_API();
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
		public void PayPaldIdentifierMissing() {
			savePayment_PayPal_API();		
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		}
		
		@Test()
		public void CustomerEmailMissing() {
			savePayment_PayPal_API();		
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		}
		
		@Test()
		public void CustomerfNameMissing() {
			savePayment_PayPal_API();		
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		}
		
		@Test()
		public void CustomerlNameMissing() {
			savePayment_PayPal_API();		
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		}
		
		
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
