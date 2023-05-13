package wcs.colesPlus.api.tests;



import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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

public class WCS_ColesPlusMOV extends Base_Class_API implements ITest{

	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	
	
	@Test(description = "AC01- Verify MOV values for different service type")
	public void validateMOV() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParamKey1"), FunLibrary.excelData.get("PathParamValue1"));
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
