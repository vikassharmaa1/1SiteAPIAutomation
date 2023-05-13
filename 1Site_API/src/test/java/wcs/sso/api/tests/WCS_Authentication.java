package wcs.sso.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class WCS_Authentication extends Base_Class_API implements ITest {

	Response response = null;
	RestLibrary restLibrary = null;

	public void SSO_API() {
		restLibrary = new RestLibrary();

		response = APILibrary.SSOAuthentication_WCS(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"),
				FunLibrary.excelData.get("RequestType"),
				FunLibrary.excelData.get("Header_access-token"),
				FunLibrary.excelData.get("Header_user-jwt-token"),
				FunLibrary.excelData.get("Header_channel"),
				FunLibrary.excelData.get("PathParam_sotreId"),
				"1");

		// get the response body and print on console
		restLibrary.getResponseBody(response);
	}

	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateValidErrorCodes(String testname) {
		// executing SSO API
		SSO_API();

		// Validating the response
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
		// executing SSO API
		SSO_API();

		// Validating the response
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
		// executing SSO API
		SSO_API();

		// Validating the response
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
	public void ValidateUserJWTAuthentication() {
		// executing SSO API
		SSO_API();

		// Validating the response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "col-token", FunLibrary.excelData.get("colToken"));

	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
