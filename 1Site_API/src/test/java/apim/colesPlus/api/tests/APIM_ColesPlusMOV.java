package apim.colesPlus.api.tests;



import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import org.junit.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import slots.api.response.pojo.CC;
import slots.api.response.pojo.Root;

public class APIM_ColesPlusMOV extends Base_Class_API implements ITest{

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
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
				funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
				funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
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
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(description = "AC01- Verify MOV values for different service type")
	public void validateMOV() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"), FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		
		String movDbColesPlusHD = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.HD.qualifiedamount'");
		String movDbNonColesPlusHD = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.HD.qualifiedamount'");
		String movDbColesPlusCC = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.colesplus.CC.qualifiedamount'");
		String movDbNonColesPlusCC = dbUtil.getValues("x_coles_config", "VALUE", "name='colrs.ecomm.non.colesplus.CC.qualifiedamount'");
		
		String movJsonColesPlusHD = funLibrary.getJsonPathValue(response, "cpMovAmtHD");
		String movJsonNonColesPlusHD = funLibrary.getJsonPathValue(response, "nonCpMovAmtHD");
		String movJsonColesPlusCC = funLibrary.getJsonPathValue(response, "cpMovAmtCC");
		String movJsonNonColesPlusCC = funLibrary.getJsonPathValue(response, "nonCpMovAmtCC");
		
		funLibrary.validate_Equals("movColesPlusHD", movDbColesPlusHD, movJsonColesPlusHD);
		funLibrary.validate_Equals("movNonColesPlusHD", movDbNonColesPlusHD, movJsonNonColesPlusHD);
		funLibrary.validate_Equals("movColesPlusCC", movDbColesPlusCC, movJsonColesPlusCC);
		funLibrary.validate_Equals("movNonColesPlusCC", movDbNonColesPlusCC, movJsonNonColesPlusCC);
		funLibrary.Assert.assertAll();
		
		 
		
	}
	

	@Override
	public String getTestName() {
		return testName.get();

	}

}
