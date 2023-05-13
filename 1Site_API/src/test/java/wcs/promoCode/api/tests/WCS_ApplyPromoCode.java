package wcs.promoCode.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_ApplyPromoCode extends Base_Class_API implements ITest {

	
	Response response = null;
	RestLibrary restLibrary = null;

	public void ApplyPromoCode_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.applyPromoCode_WCS(
				Base_Class_API.BaseURI, 
				FunLibrary.excelData.get("BasePath"), 
				FunLibrary.excelData.get("RequestType"),
				FunLibrary.excelData.get("Header_Authorization"), 
				FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"),
				FunLibrary.excelData.get("Body"),
				FunLibrary.excelData.get("QueryParam_ver"));
		//restLibrary.getResponseBody(response);
	}
	public void RemovePromoCode_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.removePromoCode_WCS(
				Base_Class_API.BaseURI, 
				"", 
				"",
				FunLibrary.excelData.get("Header_Authorization"), 
				FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"),
				FunLibrary.excelData.get("QueryParam_promoCode"),
				FunLibrary.excelData.get("QueryParam_ver"));
				int statusCode = response.getStatusCode();
				if(statusCode==200) {
					testLog.info("The Promocode --"+FunLibrary.excelData.get("QueryParam_promoCode")+" -- is removed from the order ");
					
				}
		
		//restLibrary.getResponseBody(response);
	}
	
	
	
	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_ValidRequest(String testname) {
		ApplyPromoCode_API();

		String reasonCode = FunLibrary.excelData.get("ReasonCode");
		String description = FunLibrary.excelData.get("Description");
		String code = "ERROR";

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "code", code);
		funLibrary.validateJSONPathValue_Equals(response, "description", description);
		funLibrary.validateJSONPathValue_Equals(response, "reasonCode", reasonCode);
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErrorCodes_InvalidRequest(String testname) {
		ApplyPromoCode_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
		funLibrary.Assert.assertAll();
	}
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		ApplyPromoCode_API();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
				String orderId="";		
			
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			
			orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
			dbUtil.closeDBConnection();
			
			
			funLibrary.validateJSONPathValue_Equals(response, "orderId",orderId);
			funLibrary.validateJSONPathValue_Equals(response, "promoCode",FunLibrary.excelData.get("promoCode"));
			RemovePromoCode_API();
			break;
		case "401":
		case "400":
			String errorCode = FunLibrary.excelData.get("ErrorCode");
			String errorMessage = FunLibrary.excelData.get("ErrorMessage");
			String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
			break;
		default:
			testLog.info(
					"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		
		funLibrary.Assert.assertAll();
		
	}

	@Test(description = "Validate Apply Promo code reponse when the promo code is already applied to the order")
	public void AlreadyAppliedPromo ()	{
		
		ApplyPromoCode_API();

		ApplyPromoCode_API();
		restLibrary.getResponseBody(response);
		
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", errorLevel);
		funLibrary.Assert.assertAll();
		RemovePromoCode_API();
		
	}
	

	

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName.get();
	}

}
