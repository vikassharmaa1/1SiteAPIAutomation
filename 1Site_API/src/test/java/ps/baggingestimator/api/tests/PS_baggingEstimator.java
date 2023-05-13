package ps.baggingestimator.api.tests;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_baggingEstimator extends Base_Class_API implements ITest{
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
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
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "401":
		case "400":
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
		break;
		}
		funLibrary.Assert.assertAll();
	}
	@Test(description = "Validate the orderid and totalprice available in response are matching in DB")
	public void validateOrderSubTotal() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		dbUtil.wcsSchemaRun();
		String order_id= dbUtil.getValues("Orders", "orders_id", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		String ordervalue = dbUtil.getValues("orders", "totalproduct", "member_id=(select users_id from users where field1= 'deepaktest@mailinator.com') and status='P'");
		StringBuilder s = new StringBuilder(ordervalue);
		for(int k=1; k<=3; k++)
		{
		s.deleteCharAt(ordervalue.length()-1);
		ordervalue = s.toString();
		}
		String finaordervalue= s.toString();
		System.out.println(finaordervalue);
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		System.out.println("Status code is =" +FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "200":
			funLibrary.validateJSONPathValue_Equals(response, "orderId", order_id);
			funLibrary.validateJSONPathValue_Equals(response, "orderSubTotal", finaordervalue);
		}
		funLibrary.Assert.assertAll();	
	}
	
	@Test(description = "validate  changes in database when CCP AddressId for an RD Address does not corresponds to ColAddressId")
	public void validateBaggingTypes() {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		dbUtil.wcsSchemaRun();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
	   int arrlen = response.jsonPath().getInt("bagDataOfOrder.size()");
	   List a = new ArrayList();
	   for(int i=0; i<arrlen; i++)
	   {
		   if(response.jsonPath().getString("bagDataOfOrder["+i+"].bagType").equals("Reusable plastic bags")||response.jsonPath().getString("bagDataOfOrder["+i+"].bagType").equals("Reusable paper bags") && !a.contains(response.jsonPath().getString("bagDataOfOrder["+i+"].bagType")))
		   {
			   a.add("bagDataOfOrder["+i+"].bagType");
			   if(response.jsonPath().getString("bagDataOfOrder["+i+"].bagType").equals("Reusable plastic bags"))
			   {
			   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].bagBitFlag", dbUtil.getValues("xbags", "bitvalue", "bagname='Reusable plastic bags'"));
			   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].bagSKU", dbUtil.getValues("xbags", "partnumber", "bagname='Reusable plastic bags'"));
			   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].unitPrice", "0.15");
			   }
			   else
			   {
				   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].bagBitFlag", dbUtil.getValues("xbags", "bitvalue", "bagname='Reusable paper bags'"));
				   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].bagSKU", dbUtil.getValues("xbags", "partnumber", "bagname='Reusable paper bags'"));
				   funLibrary.validateJSONPathValue_Equals(response, "bagDataOfOrder["+i+"].unitPrice", "0.25");
			   }
	   }
	 }
		funLibrary.Assert.assertAll();	
	}


	@Override
	public String getTestName() {
		return testName.get();
	}
	
	

}
