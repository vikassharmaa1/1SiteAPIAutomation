package wcs.blockedAddress.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_BlockedAddress extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header_Key"), FunLibrary.excelData.get("Header_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
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
			funLibrary.validateJSONPathValue_Equals(response, "'reasonCode'", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "'description'", FunLibrary.excelData.get("Description"));
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
		restLibrary.addHeader(FunLibrary.excelData.get("Header_Key"), FunLibrary.excelData.get("Header_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		break;
		case "200":
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String db_BlockedAddresses = dbUtil.getValues("x_coles_config", "clob_value",
				"name like 'colrs.blocked.address.validationids'");
		dbUtil.closeDBConnection();
		String[] block1DB  = db_BlockedAddresses.split(",");
		JsonPath jsonPathEvaluator = response.jsonPath();
		String api_BlockedAddresses = jsonPathEvaluator.getString("addressesBlocked");
		api_BlockedAddresses = api_BlockedAddresses.replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ","");
		String api_AddressesCount = jsonPathEvaluator.getString("totalNum");
		int apicount = Integer.parseInt(api_AddressesCount);
		funLibrary.validate_Equals("Blocked address count",block1DB.length,apicount);
		String[] block2API = api_BlockedAddresses.split(",");
		Boolean bool = false;
		for(int i=0; i<block1DB.length;i++) {
			for(int j=0;j<block2API.length;++j) 
			{
				if(block1DB[i].equals(block2API[j])){
					bool = true;
				}
				
			}
			if(bool== false) {
				System.out.println(block1DB[i] + "doesnot exists in DB");
			}
			else{
				bool = false;
			}
		}
		System.out.println(db_BlockedAddresses.equals(api_BlockedAddresses));
		funLibrary.validate_Equals("Blocked Addresses list", db_BlockedAddresses, api_BlockedAddresses);
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
