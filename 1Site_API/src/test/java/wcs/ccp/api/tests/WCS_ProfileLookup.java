package wcs.ccp.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import io.restassured.response.Response;

public class WCS_ProfileLookup extends Base_Class_API implements ITest {

	RestLibrary restLibrary;
	Response response;

	public void execAPI() {
		restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey"), FunLibrary.excelData.get("QueryParamValue"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
	}

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		execAPI();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Contains(response, "description", FunLibrary.excelData.get("Description"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateGetUserDetailsByEmail(String testname) {
		execAPI();
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "email1", FunLibrary.excelData.get("Email1"));
		funLibrary.validateJSONPathValue_Equals(response, "dateOfBirth", FunLibrary.excelData.get("DateOfBirth"));
		funLibrary.validateJSONPathValue_Equals(response, "contextAttribute", FunLibrary.excelData.get("ContextAttribute"));
		funLibrary.validateJSONPathValue_Equals(response, "phone1Type", FunLibrary.excelData.get("Phone1Type"));
		funLibrary.validateJSONPathValue_Equals(response, "phone1", FunLibrary.excelData.get("Phone1"));
		break;
		default:
			break;
		}
	}
	
	@Test(description="validate that query param ver=1.1 is giving user address details")
	public void validateGetUserAddressDetails() {
		execAPI();
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "email1", FunLibrary.excelData.get("Email1"));
		funLibrary.validateJSONPathValue_Equals(response, "dateOfBirth", FunLibrary.excelData.get("DateOfBirth"));
//		funLibrary.validateJSONPathValue_Equals(response, "contextAttribute", FunLibrary.excelData.get("ContextAttribute"));
		funLibrary.validateJSONPathValue_Equals(response, "phone1Type", FunLibrary.excelData.get("Phone1Type"));
		funLibrary.validateJSONPathValue_Equals(response, "phone1", FunLibrary.excelData.get("Phone1"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].serviceType", FunLibrary.excelData.get("ServiceType"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].firstName", FunLibrary.excelData.get("FirstName"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].lastName", FunLibrary.excelData.get("LastName"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].addressLine[0]", FunLibrary.excelData.get("AddressLine"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].nickName", FunLibrary.excelData.get("NickName"));
		funLibrary.validateJSONPathValue_Equals(response, "addresses[0].suburb", FunLibrary.excelData.get("Suburb"));
		funLibrary.Assert.assertAll();
	}
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName.get();
	}
}
