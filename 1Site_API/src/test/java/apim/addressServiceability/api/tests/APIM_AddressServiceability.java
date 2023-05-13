package apim.addressServiceability.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class APIM_AddressServiceability extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "statusCode", FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("ErrorMessage"));
			break;			
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.ParameterName", FunLibrary.excelData.get("ParameterName"));
			funLibrary.Assert.assertAll();
			break;
		case "200":
		case "206":
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].serviceType", FunLibrary.excelData.get("serviceType"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].country", FunLibrary.excelData.get("country"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].latitude", FunLibrary.excelData.get("latitude"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].postcode", FunLibrary.excelData.get("postcode"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].suburb", FunLibrary.excelData.get("suburb"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].zoneId", FunLibrary.excelData.get("zoneId"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].state", FunLibrary.excelData.get("state"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].id", FunLibrary.excelData.get("id"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].longitude", FunLibrary.excelData.get("longitude"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].serviceable", FunLibrary.excelData.get("serviceable"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.records[0].referenceId", FunLibrary.excelData.get("refrenceId"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.totalNum", FunLibrary.excelData.get("totalNum"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.numOfServiceable", FunLibrary.excelData.get("numOfServiceable"));
		funLibrary.validateJSONPathValue_Equals(response, "serviceabilityData.numOfNonServiceable", FunLibrary.excelData.get("numOfNonServiceable"));
		funLibrary.Assert.assertAll();
		break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}
}
	
	
