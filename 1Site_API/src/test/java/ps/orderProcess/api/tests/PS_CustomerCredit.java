package ps.orderProcess.api.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.internal.assertion.Assertion;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import orderprocess.api.response.pojo.CustomerCredit;
import orderprocess.api.response.pojo.CustomerCredits;
import orderprocess.api.response.pojo.Root;

import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import orderprocess.api.response.pojo.CustomerCredit;
import orderprocess.api.response.pojo.Root;

public class PS_CustomerCredit extends Base_Class_API implements ITest {
	
	@Test(description = "AC01- validatenocustomerCredits")
	public void validateNoCustomerCredits() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		//Verify 200 response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPathId = response.jsonPath();
		String jsonId = jsonPathId.getString("customerCredits");
		
		// Verify customer credits is not present in response 
		if (response.jsonPath().getJsonObject("customerCredits") == null) {
			funLibrary.validate_Equals("customerCredits", jsonId, null);
		}
	
	funLibrary.Assert.assertAll();

		
		
		
	}

	@Test(description = "AC02- validateCreditIsNotPresent")
	public void validateCreditIsNotPresent() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 200 response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		//Connect with database
		DatabaseUtilities dbUtil = new DatabaseUtilities();
        //Get coupan id from database
		List<String> Id = dbUtil.getMultipleValuesList("px_coupon", "PX_COUPON_ID",
				"status IN ('0','2') and " + " users_id='604549558'");
		//Store coupan id in list
		for (int i = 0; i < Id.size(); i++) {

			String id = Id.get(i);
			
            // Get coupan status from database
			String coupanid = dbUtil.getValues("px_coupon", "status", "PX_COUPON_ID='" + id + "'");

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			CustomerCredit Credit = root.getCustomerCredits().getCustomerCredit().stream().filter(x -> x.getCreditId().equalsIgnoreCase(id)).findFirst()
					.orElse(null);
			
			// Verify status of coupan status from response

			funLibrary.validate_Equals("creditStatus", coupanid, Credit.creditStatus);

			funLibrary.Assert.assertAll();

		}
		
		
	}
	
	@Test(description = "AC03- validateCreditIsPresent")
	public void validateCreditIsPresent() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 200 response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		//Connect with database
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		//Get coupan id from database
		List<String> Id = dbUtil.getMultipleValuesList("px_coupon", "PX_COUPON_ID",
				"status = '2' and " + " users_id='604549558'");
		//Store coupan id in list
		for (int i = 0; i < Id.size(); i++) {

			String id = Id.get(i);
           
			// Get coupan status from database
			String coupanid = dbUtil.getValues("px_coupon", "status", "PX_COUPON_ID='" + id + "'");

			Root root = (Root) restLibrary.getResponseBody(response, Root.class);

			CustomerCredit Credit = root.getCustomerCredits().getCustomerCredit().stream().filter(x -> x.getCreditId().equalsIgnoreCase(id)).findFirst()
					.orElse(null);

			// Verify status of coupan status from response
			funLibrary.validate_Equals("creditStatus", coupanid, Credit.creditStatus);

			funLibrary.Assert.assertAll();

		}
		
		
	}
	
	@Test(description = "Apply customer credit")
	public void CustomerCredit() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		//Verify 200 response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

	}
	
	@Test(dependsOnMethods = { "CustomerCredit" }, description = "AC05- validateCreditWhenTrollyModified")
	public void validateCreditWhenTrollyModified() {
		
		//Hit update trolly APi and modify trolly amount
		APILibrary.updateItem_PS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath1"), 
		FunLibrary.excelData.get("RequestType1"), FunLibrary.excelData.get("Header1_Value"), 
		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("PathParamValue1"), 
		FunLibrary.excelData.get("partNumber"), FunLibrary.excelData.get("orderItemId"), 
		FunLibrary.excelData.get("qty"),FunLibrary.excelData.get("additionalFields"));
		
		//Hit order process API and verify customer credits is not present in response
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
		FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 200 response
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPathId = response.jsonPath();
		String jsonId = jsonPathId.getString("customerCredits");
		
		// Verify customer credits is not present in response 
		if (response.jsonPath().getJsonObject("customerCredits") == null) {
			funLibrary.validate_Equals("customerCredits", jsonId, null);
		}
	
	      funLibrary.Assert.assertAll();
	      
	      // Update trolly items again in order to use same user again
	        APILibrary.updateItem_WCS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath1"), 
			FunLibrary.excelData.get("RequestType1"), FunLibrary.excelData.get("Header1_Value"), 
			FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("PathParamValue1"), 
			FunLibrary.excelData.get("partNumber"), FunLibrary.excelData.get("orderItemId"), 
			FunLibrary.excelData.get("qty1"),FunLibrary.excelData.get("additionalFields"));
	        Response response1 = restLibrary.executeAPI();
	        restLibrary.getResponseBody(response1);
	        funLibrary.validateStatusCode(response1, FunLibrary.excelData.get("StatusCode"));
		
		
	}
	
	
	
		
		@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
