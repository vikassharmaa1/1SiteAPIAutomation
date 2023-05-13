package ps.blockedAddress.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;

public class PS_BlockedAddress extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header_Key"), FunLibrary.excelData.get("Header_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.ParameterName", FunLibrary.excelData.get("ParameterName"));
			break;
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	@Test(description="validate schema of response json")
	public void validateSchema_BlockedAddress() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header_Key"), FunLibrary.excelData.get("Header_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response,"blockedaddress");
	} 
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header_Key"), FunLibrary.excelData.get("Header_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
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
		funLibrary.validateJSONPathValue_Equals(response, "totalNum", FunLibrary.excelData.get("totalNum"));
		funLibrary.validateJSONPathValue_Equals(response, "numOfBlockedAddresses", FunLibrary.excelData.get("numOfBlockedAddresses"));
		funLibrary.validateJSONPathValue_Equals(response, "numOfNonBlockedAddresses", FunLibrary.excelData.get("numOfNonBlockedAddresses"));
		funLibrary.validateJSONPathValue_Equals(response, "validationResult[0].qasAddressId", FunLibrary.excelData.get("qasAddressId1"));
		funLibrary.validateJSONPathValue_Equals(response, "validationResult[0].isBlockedAddress", FunLibrary.excelData.get("isBlockedAddress1"));
		funLibrary.validateJSONPathValue_Equals(response, "validationResult[1].qasAddressId", FunLibrary.excelData.get("qasAddressId2"));
		funLibrary.validateJSONPathValue_Equals(response, "validationResult[1].isBlockedAddress", FunLibrary.excelData.get("isBlockedAddress2"));
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
		return testName.get();
	}
}
