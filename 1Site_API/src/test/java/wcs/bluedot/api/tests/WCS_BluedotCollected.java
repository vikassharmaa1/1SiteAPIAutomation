package wcs.bluedot.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

//Author V.N Nagarajan

public class WCS_BluedotCollected extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("queryParameterKey"), FunLibrary.excelData.get("queryParameterValue"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
			
			Response response = restLibrary.executeAPI();
			restLibrary.getResponseBody(response);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			switch (FunLibrary.excelData.get("StatusCode")) {
			case "201":
				
				
				funLibrary.validateJSONPathValue_Equals(response, "orderId", FunLibrary.excelData.get("orderId"));
				funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", FunLibrary.excelData.get("colWebstoreId"));
		
				break;
			case "401":
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
				break;
			default:
				testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
				break;
			}

		funLibrary.Assert.assertAll();
	}

	@Test(description = "Validate the reponse for the order with collected status")
	public void validRequestSuccess() {
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
			restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		
			DatabaseUtilities dbUtilCol = new DatabaseUtilities("COL");
			
			dbUtilCol.updateValues("COLOWNER.DELIVERY_ETA", "DELIVERY_STATUS", "ORDER_ID="+FunLibrary.excelData.get("orderId"), "8");
		
			Response response = restLibrary.executeAPI();
			restLibrary.getResponseBody(response);
			funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
			switch (FunLibrary.excelData.get("StatusCode")) {
			case "201":
				DatabaseUtilities dbUtil = new DatabaseUtilities();
				String ccPointID = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMCollectionPointId'");
			
				if(dbUtilCol.getValues("COLOWNER.COLLECTION_POINT_ATTR", "VALUE","COLLECTION_POINT_ID='" + ccPointID + "' and NAME='APP_PUSHMSG_ENABLED'").equalsIgnoreCase("TRUE")) {
				if(dbUtilCol.getValues("COLOWNER.DELIVERY_ETA", "DELIVERY_STATUS", "order_id=" + FunLibrary.excelData.get("orderId")).equals("9")) {
					funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("message"));
				}else
				testLog.info("No CC orders with status not equal to 9 found in DB to process.");
				}else
				testLog.info("The Collection point ID for the given order is not having APP_PUSHMSG_ENABLED field");
				break;
			default:
				testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
				break;
			}	
			
			funLibrary.Assert.assertAll();
	
	}
	
	@Test(description = "Validate the reponse for the order which is not in collected status")
			public void validRequestUnsuccess() {
				RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
				restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
				
				DatabaseUtilities dbUtilCol1 = new DatabaseUtilities("COL");
				boolean flag=dbUtilCol1.getValues("COLOWNER.DELIVERY_ETA", "DELIVERY_STATUS", "order_id=" + FunLibrary.excelData.get("orderId")).equals("9");
				
				Response response = restLibrary.executeAPI();
				restLibrary.getResponseBody(response);
				funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
				
				if(flag) {
				switch (FunLibrary.excelData.get("StatusCode")) {
				case "201":
					DatabaseUtilities dbUtil = new DatabaseUtilities();
					String ccPointID = dbUtil.getValues("XORDERATTR", "VALUE", "orders_id='" + FunLibrary.excelData.get("orderId") + "' and NAME='DMCollectionPointId'");
				
					
					DatabaseUtilities dbUtilCol = new DatabaseUtilities("colservices");
					//dbUtilCol.updateValues("COLOWNER.DELIVERY_ETA", "DELIVERY_STATUS", "ORDER_ID=", "8");
					if(dbUtilCol.getValues("COLOWNER.COLLECTION_POINT_ATTR", "VALUE","COLLECTION_POINT_ID='" + ccPointID + "' and NAME='APP_PUSHMSG_ENABLED'").equalsIgnoreCase("TRUE")) {
					
						funLibrary.validateJSONPathValue_Equals(response, "message", FunLibrary.excelData.get("message"));
					
					
					}else
					testLog.info("The Collection point ID for the given order is not having APP_PUSHMSG_ENABLED field");
					break;
				default:
					testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
					break;
				}	}
				else
					testLog.info("Order status is not collected");
				
				funLibrary.Assert.assertAll();
						
					
			
			
			}
			
	@Override
	public String getTestName() {
		return testName.get();
	}

	
}
