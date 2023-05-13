package apim.promoCode.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class APIM_RemovePromoCode extends Base_Class_API implements ITest {	
	Response response = null;
	RestLibrary restLibrary = null;

	public void ApplyPromoCode_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.applyPromoCode_APIM(
				Base_Class_API.BaseURI, 
				"","",
				FunLibrary.excelData.get("Header_Authorization"), 
				FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("Body"),
				FunLibrary.excelData.get("QueryParam_storeId"),
				FunLibrary.excelData.get("QueryParam_ver"));
		//restLibrary.getResponseBody(response);
	}
	public void RemovePromoCode_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.removePromoCode_APIM(
				Base_Class_API.BaseURI, 
				FunLibrary.excelData.get("BasePath"), 
				FunLibrary.excelData.get("RequestType"),
				FunLibrary.excelData.get("Header_Authorization"), 
				FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"),
				FunLibrary.excelData.get("QueryParam_promoCode"),
				FunLibrary.excelData.get("QueryParam_ver"));
				int statusCode = response.getStatusCode();
				if(statusCode==200) 
					testLog.info("The Promocode --"+FunLibrary.excelData.get("QueryParam_promoCode")+" -- is removed from the order ");
					//restLibrary.getResponseBody(response);
	}
		
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_ValidRequest(String testname) {
		RemovePromoCode_API();

		String statusCode = FunLibrary.excelData.get("StatusCode");
		String errorMessage = FunLibrary.excelData.get("Description");
		

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "statusCode", statusCode);
		funLibrary.validateJSONPathValue_Equals(response, "message", errorMessage);
		
		
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_InvalidRequest(String testname) {
		RemovePromoCode_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
		funLibrary.Assert.assertAll();
	}
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		ApplyPromoCode_API();
		RemovePromoCode_API();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
				String orderId="";			
			DatabaseUtilities dbUtil = new DatabaseUtilities();		
			orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
			dbUtil.closeDBConnection();			
			funLibrary.validateJSONPathValue_Equals(response, "orderId",orderId);
			
			
			break;
		case "401":
		case "400":
			String errorCode = FunLibrary.excelData.get("ErrorCode");
			String errorMessage = FunLibrary.excelData.get("ErrorMessage");
			String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();	
	}

	

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName.get();
	}

}

