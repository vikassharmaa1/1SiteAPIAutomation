package ps.sso.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class PS_Authentication extends Base_Class_API implements ITest {
	
	RestLibrary restLibrary;
	Response response;
	
	public void authentication_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.authentication_PS(
				Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"),
				FunLibrary.excelData.get("RequestType"),
				FunLibrary.excelData.get("Header_authorization"),
				FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Header_Channel"));
		// get the response body and print on console
		restLibrary.getResponseBody(response);
	}


	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		authentication_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		RestAssured.registerParser("text/plain", Parser.JSON);
		switch (FunLibrary.excelData.get("StatusCode")) {
		
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.ParameterName", FunLibrary.excelData.get("ParameterName"));
			break;
			
		case "404":
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			break;

		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	
	@Test(description="validate schema of response json")
	public void validateSchema_SSO() {
		authentication_API();
		funLibrary.validateJsonStructure(response,"sso");
	}


	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		authentication_API();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		RestAssured.registerParser("text/plain", Parser.JSON);
		switch (FunLibrary.excelData.get("StatusCode")) {

		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "col-token", FunLibrary.excelData.get("colToken"));
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

	@Override
	public String getTestName() {

		return testName.get();
	}
}
