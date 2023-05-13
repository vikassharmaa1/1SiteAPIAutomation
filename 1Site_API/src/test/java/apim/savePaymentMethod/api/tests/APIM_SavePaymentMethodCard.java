package apim.savePaymentMethod.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIM_SavePaymentMethodCard extends Base_Class_API implements ITest{
	RestLibrary restLibrary;
	Response response;
	String identifier="";
	
	public void savePayment_Card_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_Card_APIM(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userAuthorization"), 
                FunLibrary.excelData.get("Header_subscriptionKey"),
        		FunLibrary.excelData.get("StoreId"),
                FunLibrary.excelData.get("QueryParam_name_Card"),           
                FunLibrary.excelData.get("SaveToProfile"),
                identifier,
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	public void savePayment_SavedCard_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_Card_APIM(
                Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_userAuthorization"), 
                FunLibrary.excelData.get("Header_subscriptionKey"),
        		FunLibrary.excelData.get("StoreId"),
                FunLibrary.excelData.get("QueryParam_name_SavedCard"),           
                FunLibrary.excelData.get("SaveToProfile"),
                 identifier,
                 FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
    }
	
	// Test Cases for Card
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodesCard(String testname) {
		savePayment_Card_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "statusCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("ErrorMessage"));
			break;
			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
		
	}
		
		@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
		public void validateInvalidErrorCodesCard(String testname) {
			savePayment_Card_API();
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
	public void validateFunctionalErrorCodesCard(String testname) {
		savePayment_Card_API();
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
	
	
	// Test Cases for SavedCard
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodesSavedCard(String testname) {
		savePayment_SavedCard_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "statusCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("ErrorMessage"));
			break;
			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
		
	}
		
		@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
		public void validateInvalidErrorCodesSavedCard(String testname) {
			savePayment_SavedCard_API();
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
	public void validateFunctionalErrorCodesSavedCard(String testname) {
		savePayment_SavedCard_API();
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
	public void validatePaymentSave() {
		identifier = APILibrary.getMPGSCardToken("4111111111111111", "12", "25", "123");
		RestAssured.reset();
		savePayment_Card_API();		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test()
	public void paymentNameMissing() {
		restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_APIM(
        		Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_subscriptionKey"),
                FunLibrary.excelData.get("Header_userAuthorization"), 
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        restLibrary.getResponseBody(response);
        
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	
	@Test()
	public void IdentifierMissing() {
		
		restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_APIM(
        		Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_subscriptionKey"),
                FunLibrary.excelData.get("Header_userAuthorization"), 
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_ver"));
        restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	
	@Test()
	public void cardSavetoProfileMissing() {
		restLibrary = new RestLibrary();
        response = APILibrary.savePaymentMethod_APIM(
        		Base_Class_API.BaseURI,
                FunLibrary.excelData.get("BasePath"),
                FunLibrary.excelData.get("RequestType"),
                FunLibrary.excelData.get("Header_authorization"),
                FunLibrary.excelData.get("Header_subscriptionKey"),
                FunLibrary.excelData.get("Header_userAuthorization"), 
                FunLibrary.excelData.get("RequestBody"),
                FunLibrary.excelData.get("QueryParam_ver"));
        restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
	}
	
	@Override
	public String getTestName() {
		return testName.get();
	}
}
