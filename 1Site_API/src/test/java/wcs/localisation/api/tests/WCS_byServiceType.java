package wcs.localisation.api.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class WCS_byServiceType extends Base_Class_API implements ITest {

//	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
//	public void validateGenericErrorCodes(String testname) {
//		Map<String, String> header = new HashMap<String, String>();
//		header.put(FunLibrary.header1_Key, FunLibrary.header1_Value);
//		header.put(FunLibrary.header2_Key, FunLibrary.header2_Value);
//
//		Map<String, String> pathParameter = new HashMap<String, String>();
//		pathParameter.put(FunLibrary.pathParameterKey1, FunLibrary.pathParameterValue1);
//		pathParameter.put(FunLibrary.pathParameterKey2, FunLibrary.pathParameterValue2);
//
//		// Setting API end point and header
//		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.basePath, FunLibrary.requestType, header);
//		// Executing API with body and getting the response
//		Response response = restLibrary.executeAPIWithPathParams(pathParameter);
//		// Validating the response, status code
//		restLibrary.getResponseBody(response);
//		funLibrary.validateStatusCode(response, FunLibrary.statusCode);
//		// ValidatableResponse
//		switch (FunLibrary.statusCode) {
//		case "400":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].description", FunLibrary.description);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].reasonCode", FunLibrary.reasonCode);
//			break;
//
//		case "504":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].description", FunLibrary.description);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].reasonCode", FunLibrary.reasonCode);
//			break;
//
//		case "403":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].description", FunLibrary.description);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].reasonCode", FunLibrary.reasonCode);
//			break;
//
//		case "500":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].description", FunLibrary.description);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].reasonCode", FunLibrary.reasonCode);
//			break;
//
//		case "404":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//
//			break;
//		default:
//			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.statusCode);
//			break;
//		}
//
//	}
//
//	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
//	public void validateFunctionalErrorCodes(String testname) {
//
//		Map<String, String> header = new HashMap<String, String>();
//		header.put(FunLibrary.header1_Key, FunLibrary.header1_Value);
//		header.put(FunLibrary.header2_Key, FunLibrary.header2_Value);
//
//		Map<String, String> pathParameter = new HashMap<String, String>();
//		pathParameter.put(FunLibrary.pathParameterKey1, FunLibrary.pathParameterValue1);
//		pathParameter.put(FunLibrary.pathParameterKey2, FunLibrary.pathParameterValue2);
//
//		// Setting API end point and header
//		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.basePath, FunLibrary.requestType, header);
//		// Executing API with body and getting the response
//		Response response = restLibrary.executeAPIWithPathParams(pathParameter);
//		// Validating the response, status code
//		restLibrary.getResponseBody(response);
//		funLibrary.validateStatusCode(response, FunLibrary.statusCode);
//		switch (FunLibrary.statusCode) {
//		case "200":
//			funLibrary.validateJSONPathValue_Equals(response, "Country", FunLibrary.Country);
//			funLibrary.validateJSONPathValue_Equals(response, "PostCode", FunLibrary.PostCode);
//			funLibrary.validateJSONPathValue_Equals(response, "Suburb", FunLibrary.Suburb);
//			funLibrary.validateJSONPathValue_Equals(response, "State", FunLibrary.State);
//			break;
//		case "204":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//
//			break;
//		case "400":
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.errorCode);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].description", FunLibrary.description);
//			funLibrary.validateJSONPathValue_Equals(response, "errors[0].reasonCode", FunLibrary.reasonCode);
//			break;
//
//		default:
//			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.statusCode);
//			break;
//		}
//
//	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName.get();

	}

}
