package ps.colesPlus.api.tests;



import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import org.junit.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class PS_DeleteSubscription extends Base_Class_API implements ITest{

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
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
	
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	
	
	@Test(description = "ST-4987: AC01- Verify details for non associated subscription Id")
	public void validateNonAssociatedSubscriptionId() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}
	
	
	
	@Test(description = "ST-4987: AC02- Verify details for inactive subscription Id")
	public void validateInactiveSubscription() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
		
		
	}

	

	
	
	
	
	    


	
	

	

	@Override
	public String getTestName() {
		return testName.get();

	}

}
