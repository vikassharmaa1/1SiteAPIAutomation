package ps.localisation.api.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class PS_ByServiceType extends Base_Class_API implements ITest {

//	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
//	public void validateGenericErrorCodes(String testname) {
//		// RestAssured.proxy("proxy.cmltd.net.au",8080);
//		Map<String, String> header = new HashMap<String, String>();
//		header.put(FunLibrary.header1_Key, FunLibrary.header1_Value);
//		header.put(FunLibrary.header2_Key, FunLibrary.header2_Value);
//
//		// Setting API end point and header
//		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
//				FunLibrary.excelData.get("RequestType"), header);
//		// Executing API with body and getting the response
//		Response response = restLibrary.executeAPIWithBody(FunLibrary.requestBody);
//
//		// Validating the response, status code
//		restLibrary.getResponseBody(response);
//		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
//
//		switch (FunLibrary.excelData.get("StatusCode")) {
//		case "404":
//			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
//
//			break;
//		case "400":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.errorMessage);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.errorLevel);
//			break;
//		case "504":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.errorMessage);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.errorLevel);
//			break;
//		case "204":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.errorMessage);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.errorLevel);
//			break;
//		case "500":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.errorMessage);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.errorLevel);
//			break;
//		default:
//			testLog.info(
//					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
//			break;
//		}
//
//	}
//
//	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
//	public void validateFunctionalErrorCodes(String testname) {
//		// RestAssured.proxy("proxy.cmltd.net.au",8080);
//		Map<String, String> header = new HashMap<String, String>();
//		header.put(FunLibrary.header1_Key, FunLibrary.header1_Value);
//		header.put(FunLibrary.header2_Key, FunLibrary.header2_Value);
//
//		// Setting API end point and header
//		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
//				FunLibrary.excelData.get("RequestType"), header);
//		// Executing API with body and getting the response
//		Response response = restLibrary.executeAPIWithBody(FunLibrary.requestBody);
//
//		// Validating the response, status code
//		restLibrary.getResponseBody(response);
//		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
//		switch (FunLibrary.excelData.get("StatusCode")) {
//		case "200":
////			funLibrary.validateJSONPathValue_Equals(response, "", FunLibrary.);
////			funLibrary.validateJSONPathValue_Equals(response, "", FunLibrary.);
////			funLibrary.validateJSONPathValue_Equals(response, "", FunLibrary.);
////			funLibrary.validateJSONPathValue_Equals(response, "", FunLibrary.;
//			break;
//		case "400":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.errorMessage);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.errorLevel);
//			break;
//		default:
//			testLog.info(
//					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
//			break;
//		}
//
//	}

	@Override
	public String getTestName() {

		return testName.get();
	}
}
