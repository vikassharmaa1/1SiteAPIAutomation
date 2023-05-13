package testdata;

import java.util.List;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.ColesOnlineAPIBody;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class AddAddress extends Base_Class_API implements ITest {

	@Test(dataProvider = "emailIdProvider", dataProviderClass = API_DataProvider.class)
	public void addAddress(String emailid) {
		String nickName = "HD NT123";
		testLog.info("Logging in...");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, "wcs/resources/store/20510/loginidentity?updateCookies=true", "POST");
		ColesOnlineAPIBody body = new ColesOnlineAPIBody();
		restLibrary.addBody(body.userLoginBody(emailid.toString()));
		Response response = restLibrary.executeAPI();
		funLibrary.validateStatusCode(response, "201");
		// Getting WCToken and WCTrustedToken values from response
		JsonPath jsonPathEvaluator = response.jsonPath();
		FunLibrary.WCToken = jsonPathEvaluator.getString("WCToken");
		FunLibrary.WCTrustedToken = jsonPathEvaluator.getString("WCTrustedToken");
		// Getting set cookie values from response
		Headers headers = response.getHeaders();
		List<String> cookies = headers.getValues("Set-Cookie");
		FunLibrary.allSetCookies = "";
		for (int i = 0; i < cookies.size(); i++) {
			String cookie = cookies.get(i);
			FunLibrary.allSetCookies = FunLibrary.allSetCookies + cookie + "; ";
		}
		// Setting API end point and header
		RestLibrary restLibrary1 = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), "POST");
		restLibrary1.addHeader("WCToken", FunLibrary.WCToken);
		restLibrary1.addHeader("WCTrustedToken", FunLibrary.WCTrustedToken);
		restLibrary1.addQueryParameter("updateCookies", "true");
		restLibrary1.addBody(body.addNewAddress(nickName, emailid.toString()));
		// Executing API with body and getting the response
		response = restLibrary1.executeAPI();
		funLibrary.validateStatusCode(response, "201");
		// Validating response data
		ValidatableResponse validatableResponse = response.thenReturn().then();
		funLibrary.validate_NotNullValue(validatableResponse, "userId");
		funLibrary.validate_NotNullValue(validatableResponse, "addressId");
		// User logging out
		funLibrary.logout();
	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
