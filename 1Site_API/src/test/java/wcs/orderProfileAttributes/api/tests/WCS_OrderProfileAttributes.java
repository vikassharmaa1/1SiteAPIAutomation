package wcs.orderProfileAttributes.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class WCS_OrderProfileAttributes extends Base_Class_API implements ITest {
	
	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"),
				FunLibrary.excelData.get("pathParameterValue"));
		
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
		// ValidatableResponse
				switch (FunLibrary.excelData.get("StatusCode")) {
				case "404":
					funLibrary.validateJSONPathValue_Equals(response, "code", FunLibrary.excelData.get("ErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
					funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
					break;
				case "500":
					funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
					break;
				default:
					testLog.info(
							"No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
					break;
				}
				funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"),
				FunLibrary.excelData.get("pathParameterValue"));
		
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		
		
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
				switch (FunLibrary.excelData.get("StatusCode")) {
				case "200":
					funLibrary.validateJSONPathValue_Equals(response, "orderID", FunLibrary.excelData.get("orderID"));
					
					
					//-----------AC01-------
					DatabaseUtilities dbUtil = new DatabaseUtilities();
					if(!FunLibrary.excelData.get("OrderBaggingOptions").isEmpty()) {
					String baggingOption = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderID")+"' and name ='OrderBaggingOptions'" );
					funLibrary.validate_Equals("OrderBaggingOptions", FunLibrary.excelData.get("OrderBaggingOptions"), baggingOption);
					}
					
					if(!FunLibrary.excelData.get("signedType").isEmpty()) {
					//-----------AC02-------
					String signed = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderID")+"' and name='OrderUnattendedAllowed'");
					funLibrary.validate_Equals("OrderUnattendedAllowed", "1", signed);
					
					String serviceType_actual = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderID")+"' and name = 'OrderSubServiceType'");
					funLibrary.validate_Equals("OrderSubServiceType", "HD", serviceType_actual);
					
					String dlvinstr = dbUtil.getValues("XADDRESS", "DLVINSTR", "member_id=" + FunLibrary.excelData.get("member_id"));
					funLibrary.validate_Equals("DeliveryInstruction", FunLibrary.excelData.get("DeliveryInstruction"), dlvinstr);
					}
					
					if(!FunLibrary.excelData.get("unattendedType").isEmpty()) {
					//-----------AC03-------
					String unattended =dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderID")+"' and name='OrderUnattendedAllowed'");
					funLibrary.validate_Equals("OrderUnattendedAllowed", "2", unattended);
					
					String serviceType_actual = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderID")+"' and name = 'OrderSubServiceType'");
					funLibrary.validate_Equals("OrderSubServiceType", "UD", serviceType_actual);
					
					String field1 = dbUtil.getValues("XADDRESS", "FIELD1", "member_id=" + FunLibrary.excelData.get("member_id"));
					funLibrary.validate_Equals("DeliveryInstruction", FunLibrary.excelData.get("DeliveryInstruction"), field1);
					
					}
					
					
					//-----------AC04-------
					if(!FunLibrary.excelData.get("orderSubServiceType").isEmpty()) {
						
						if(FunLibrary.excelData.get("orderSubServiceType").equalsIgnoreCase("HD"))
						{
							String dlvinstr = dbUtil.getValues("XADDRESS", "DLVINSTR", "member_id=" + FunLibrary.excelData.get("member_id") );
							funLibrary.validate_Equals("DeliveryInstruction", FunLibrary.excelData.get("DeliveryInstruction"), dlvinstr);
							
						}
						else if (FunLibrary.excelData.get("orderSubServiceType").equalsIgnoreCase("UD")){
							String field1 = dbUtil.getValues("XADDRESS", "FIELD1", "member_id=" + FunLibrary.excelData.get("member_id"));
							funLibrary.validate_Equals("DeliveryInstruction", FunLibrary.excelData.get("DeliveryInstruction"), field1);
						}
					}
					
					//-----------AC05-------
					if(!FunLibrary.excelData.get("profileBaggingPreference").isEmpty()) {
						String baggingPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE", "member_id=" + FunLibrary.excelData.get("member_id") + " and MBRATTR_ID =730");
						funLibrary.validate_Equals("profileBaggingPreference", FunLibrary.excelData.get("OrderBaggingOptions"), baggingPreference);
					}
			
					//-----------AC06-------
					if((!FunLibrary.excelData.get("profileUnattendedPreference").isEmpty()) && (!FunLibrary.excelData.get("unattendedType").isEmpty())) {
						String orderPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE", "member_id=" + FunLibrary.excelData.get("member_id") + " and MBRATTR_ID =731");
						funLibrary.validate_Equals("profileUnattendedPreference","1", orderPreference);
						
					}
					
					
					break;
				case "206":
					funLibrary.validateJSONPathValue_Equals(response, "orderID", FunLibrary.excelData.get("orderID"));
					funLibrary.validateJSONPathValue_Equals(response, "isBaggingOptionSaved", FunLibrary.excelData.get("isBaggingOptionSaved"));
					funLibrary.validateJSONPathValue_Equals(response, "baggingOptionErrorCode", FunLibrary.excelData.get("baggingOptionErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "isUnattendedTypeSaved", FunLibrary.excelData.get("isUnattendedTypeSaved"));
					funLibrary.validateJSONPathValue_Equals(response, "unattendedTypeErrorCode", FunLibrary.excelData.get("unattendedTypeErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "isDeliveryInstructionSaved", FunLibrary.excelData.get("isDeliveryInstructionSaved"));
					funLibrary.validateJSONPathValue_Equals(response, "deliveryInstructionCode", FunLibrary.excelData.get("deliveryInstructionCode"));
				
					break;
				case "401":
				case "400":
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
							FunLibrary.excelData.get("ErrorLevel/Priority"));
					break;
				case "500":
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
					funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
							FunLibrary.excelData.get("ErrorLevel/Priority"));
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
