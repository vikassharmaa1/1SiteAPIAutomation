package apim.orderProcess.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIM_OrderProcessEnhancement extends Base_Class_API implements ITest {
	
	@Test( description = "AC01- validateAppliedPromoCodes")
	public void validateAppliedPromoCodes() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String jsonId = jsonPathId.getString("appliedPromoCodes");

				
				// Verify appliedPromoCodes is present in response 
				if (response.jsonPath().getJsonObject("appliedPromoCodes") != null) {
					funLibrary.validate_Equals("appliedPromoCodes", jsonId, "[[qualified:true, savingAmount:10.0, active:true, promoCode:CLICK10, promoCodeDescription:Thanks for shopping. Here's $10 off from promo code CLICK10!]]");

				}
			funLibrary.Assert.assertAll();
			
	}
	
	@Test( description = "AC02- validatePromoNotQualified")
	public void validatePromoNotQualified() {
		
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String jsonId = jsonPathId.getString("appliedPromoCodes");
			
				// Verify qualified status is false  
				if (response.jsonPath().getJsonObject("appliedPromoCodes") != null) {
					funLibrary.validate_Equals("appliedPromoCodes", jsonId, "[[qualified:false, savingAmount:0.0, active:true, promoCode:SHOPBACK10, promoCodeDescription:Thanks for shopping. Here's $10 off from promo code CLICK10!]]");
				}
			
			funLibrary.Assert.assertAll();
			
	}

	/*@Test( description = "AC03- validateExpiredPromoCode")
	public void validateExpiredPromoCode() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String jsonId = jsonPathId.getString("appliedPromoCodes");

				// Verify qualified and active status is false
				if (response.jsonPath().getJsonObject("appliedPromoCodes") != null) {
					funLibrary.validate_Equals("appliedPromoCodes", jsonId, "[[qualified:false, savingAmount:0, "
							+ "active:false, promoCode:CYBER]]");
				}
			
			funLibrary.Assert.assertAll();
			
	}*/
	
	/*@Test( description = "AC04- validateMultiplePromoCodes")
	public void validateMultiplePromoCodes() {
		
		//Set basepath, parameters and request body
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header3_Key"), FunLibrary.excelData.get("Header3_Value"));
				restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				
				//Get the response
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				JsonPath jsonPathId = response.jsonPath();
				
				//Get the required path of each field
				String jsonId = jsonPathId.getString("appliedPromoCodes");
	
				// Verify appliedPromoCodes is present in response 
				if (response.jsonPath().getJsonObject("appliedPromoCodes") != null) {
					funLibrary.validate_Equals("appliedPromoCodes", jsonId, "[[qualified:true, savingAmount:10.0, "
							+ "active:true, promoCode:CLICK10, promoCodeDescription:Thanks for shopping. "
							+ "Here's $10 off from promo code CLICK10!], [qualified:true, savingAmount:10.0, "
							+ "active:true, promoCode:CREWARDS10, promoCodeDescription:Thanks for shopping. "
							+ "Here's $10 off from promo code CLICK10!], [qualified:true, savingAmount:10.0, "
							+ "active:true, promoCode:SHOPBACK10, promoCodeDescription:Thanks for shopping. "
							+ "Here's $10 off from promo code CLICK10!]]");

				}

			funLibrary.Assert.assertAll();
			
	}*/
	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
