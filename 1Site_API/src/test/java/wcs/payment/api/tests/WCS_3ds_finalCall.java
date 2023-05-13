package wcs.payment.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;

import io.restassured.response.Response;

public class WCS_3ds_finalCall extends Base_Class_API implements ITest {
	
	@Test(description = "finalCall3DsSuccess")
	public void finalCall3DsSuccess() {
		
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"), FunLibrary.excelData.get("pathParameterValue"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey2"), FunLibrary.excelData.get("pathParameterValue2"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		
	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
