package wcs.promoCode.api.tests;

import java.util.List;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;
import orderFullSummary.pojo.AutoAppliedPromosWithNoPromoCodes;
import orderFullSummary.pojo.Root;

public class WCS_AutoAppliedPromos extends Base_Class_API implements ITest {

	Response response = null;
	RestLibrary restLibrary = null;
	String storeId;
	Root root;
	List<AutoAppliedPromosWithNoPromoCodes> autoAppliedPromosWithNoPromoCodes;

	public void OrderFullSummaryAPI() {

		response = APILibrary.OrderFullWeightSummary_WCS(
				Base_Class_API.BaseURI, 
				"", 
				"",
				FunLibrary.excelData.get("Header_access-token"), 
				FunLibrary.excelData.get("Header_user-jwt-token"),
				FunLibrary.excelData.get("PathParam_storeId"),
				FunLibrary.excelData.get("QueryParam_ver"));
		

		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		if(response.getStatusCode() == 200) {
			root = (Root) restLibrary.getResponseBody(response, Root.class);
		}

	}
	
	public void orderProcess_API() {
        restLibrary = new RestLibrary();
        response = APILibrary.orderProcess_WCS(
        		Base_Class_API.BaseURI,
                "",
                "",
                FunLibrary.excelData.get("Header_access-token"),
                FunLibrary.excelData.get("Header_user-jwt-token"),             
                FunLibrary.excelData.get("PathParam_storeId"),
                FunLibrary.excelData.get("QueryParam_ver"));
        // get the response body and print on console
        
    }
	
	
	
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		
		Boolean freeDelivery = false;
		Double orderDiscount =0.00;
		orderProcess_API();
		OrderFullSummaryAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
				String orderId="";		
			
			DatabaseUtilities dbUtil = new DatabaseUtilities();
			
			orderId = dbUtil.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("User") + "') and STATUS='P'");
			dbUtil.closeDBConnection();
			
			if(response.jsonPath().getJsonObject("autoAppliedPromosWithNoPromoCodes") != null) {
			autoAppliedPromosWithNoPromoCodes = root.getAutoAppliedPromosWithNoPromoCodes();
			
			for(AutoAppliedPromosWithNoPromoCodes aap : autoAppliedPromosWithNoPromoCodes) {
				
				if( aap.getPromoType().toString().contains("shipping"))
					freeDelivery = true;
				else {
					orderDiscount=orderDiscount+aap.getSavingAmount();
				}
			//String promoId=	aap.getPromoId();
			
			}
					
			funLibrary.validateJSONPathValue_Equals(response, "orderId",orderId);
			
			if(root.getOrderShipping()>1.00)
				funLibrary.validate_Equals("freeDelivery", freeDelivery, root.freeDelivery);
			
			funLibrary.validate_Equals("orderDiscount", orderDiscount, root.orderDiscount);
			}
			else
				testLog.info("No AutoAppliedPromos are present in the order ");
			
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

	
	
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return testName.get();
	}
	
	
	
}
