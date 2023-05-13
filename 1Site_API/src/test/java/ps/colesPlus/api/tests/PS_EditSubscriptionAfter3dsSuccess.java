package ps.colesPlus.api.tests;

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

public class PS_EditSubscriptionAfter3dsSuccess extends Base_Class_API implements ITest {
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
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
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(description ="AC01- Verify 3ds success")
	public void validate3DsSuccess() {
		Response response= null;
		
		//Call MPGS session API and create session
		String identifier = APILibrary.getMPGSCardToken("4440000009900010", "12", "25", "100");
		RestAssured.reset();
		
		//Call Initiate Edit subscription API
			    response = APILibrary.editSubscription_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("subscriptionId"),
			    		"Card", identifier);
			    JsonPath jsonPath = response.jsonPath();
				
				String recurringOrderId = jsonPath.getString("recurringOrderId");
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));
			DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
			
			
			//Check the order status changes to J or not
			String ORDERS_ID = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID, recurringOrderId);
			
			
			String STATUS_DB = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
			if(STATUS_DB.equals("J")) {
				
				funLibrary.Assert.assertTrue(true, "Status matched");
				funLibrary.testLog.info("Status matched");
				
			}
			
			else {
				funLibrary.Assert.assertTrue(false, "Status mismatched");
				funLibrary.testLog.info("Status mismatched");
			}
	
			//Call Initiate 3ds API
			response = APILibrary.initiate3Ds_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("storeId"), recurringOrderId);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			
			
			//Call Proceed 3ds API
			response = APILibrary.proceed3Ds_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("storeId"), recurringOrderId);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			
			
			//Call Final 3ds API
			response = APILibrary.final3Ds_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("storeId"), recurringOrderId);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode1"));
			
			//Call Complete edit subscription API
			response = APILibrary.editSubscriptionComplete_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("ver"), 
			    		FunLibrary.excelData.get("subscriptionId"), recurringOrderId);
			
			//Verify 200 status
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			String ORDERS_ID1 = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID1, recurringOrderId);
			
			// Verify orderid is updated in database or not
			String STATUS_DB1 = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
			if(STATUS_DB1.equals("I")) {
				
				funLibrary.Assert.assertTrue(true, "Status matched");
				funLibrary.testLog.info("Status matched");
				
			}
			
			else {
				funLibrary.Assert.assertTrue(false, "Status mismatched");
				funLibrary.testLog.info("Status mismatched");
			}
			
           String ORDERS_ID2 = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("MemberId") + "'");
			
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID1, ORDERS_ID2);
			
			funLibrary.Assert.assertAll();
			
		
		
	}
	
	@Test(description ="AC01- Verify 3ds success")
	public void validate3DsNotEnabled() {
		Response response= null;
		
		//Call MPGS session API and create session
		String identifier = APILibrary.getMPGSCardToken("5123456789012346", "10", "26", "100");
		RestAssured.reset();
		
		//Call Initiate Edit subscription API
			    response = APILibrary.editSubscription_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("subscriptionId"),
			    		"Card", identifier);
			    JsonPath jsonPath = response.jsonPath();
				
				String recurringOrderId = jsonPath.getString("recurringOrderId");
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode2"));
			DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
			
			
			//Check the order status changes to J or not
			String ORDERS_ID = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID, recurringOrderId);
			
			
			String STATUS_DB = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
			if(STATUS_DB.equals("J")) {
				
				funLibrary.Assert.assertTrue(true, "Status matched");
				funLibrary.testLog.info("Status matched");
				
			}
			
			else {
				funLibrary.Assert.assertTrue(false, "Status mismatched");
				funLibrary.testLog.info("Status mismatched");
			}
	
			//Call Initiate 3ds API
			response = APILibrary.initiate3Ds_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("storeId"), recurringOrderId);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			
			//Call Final 3ds API
			response = APILibrary.final3Ds_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("storeId"), recurringOrderId);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode1"));
			
			//Call Complete edit subscription API
			response = APILibrary.editSubscriptionComplete_PS("", "", "", FunLibrary.excelData.get("Header1_Value"),
			    		FunLibrary.excelData.get("Header2_Value"), FunLibrary.excelData.get("ver"), 
			    		FunLibrary.excelData.get("subscriptionId"), recurringOrderId);
			
			//Verify 200 status
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			String ORDERS_ID1 = dbUtil.getValues("orders", "ORDERS_ID", "orders_id='" + recurringOrderId + "'");		
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID1, recurringOrderId);
			
			// Verify orderid is updated in database or not
			String STATUS_DB1 = dbUtil.getValues("orders", "STATUS", "orders_id='" + recurringOrderId + "'");
			if(STATUS_DB1.equals("I")) {
				
				funLibrary.Assert.assertTrue(true, "Status matched");
				funLibrary.testLog.info("Status matched");
				
			}
			
			else {
				funLibrary.Assert.assertTrue(false, "Status mismatched");
				funLibrary.testLog.info("Status mismatched");
			}
			
           String ORDERS_ID2 = dbUtil.getValues("subscription", "ORDERS_ID", "member_id='" + FunLibrary.excelData.get("MemberId") + "'");
			
			funLibrary.validate_Equals("recurringOrderId", ORDERS_ID1, ORDERS_ID2);
			
			funLibrary.Assert.assertAll();
			
		
		
	}
	
	/*@Test(description ="AC02- Verify 3ds fail")
	public void validate3dsAuthenticationFails() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 400 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}*/
	
	@Test(description ="AC03- Verify version")
	public void validateVersion() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 200 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	}
	
	@Test(description ="AC04- Verify mandatory parameter missing")
	public void validateMandatoryParameterMissing() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 400 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	/*@Test(description = "AC05- Unexpected data provided in mandatory parameter")
	public void validateUnexpectedData() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 400 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}*/
	
	@Test(description = "AC06- Verify Wrong Subscription id")
	public void validateWrongSubscription() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 400 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	
	@Test(description = "AC07- Verify details for inactive subscription Id")
	public void validateInactiveSubscription() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamValue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		
		//Verify 400 status
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
