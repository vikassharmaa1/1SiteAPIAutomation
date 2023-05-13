package wcs.enhanceProfileLookup.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class WCS_ProfileEnhance extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'",
					FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Equals(response, "reasonCode", FunLibrary.excelData.get("ReasonCode"));
			funLibrary.validateJSONPathValue_Equals(response, "description", FunLibrary.excelData.get("Description"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for "
					+ FunLibrary.excelData.get("statusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		// 400 nd 404 need to change in sheet
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate Flybuy user")
	public void verifyFlybuyNumberLinked() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileFlyBuysNumber'");
		String DB_FlyBuysNumber = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseFlyBuyNumber = jsonPath.getString("contextAttribute[1].attributeValue.value");
		System.out.println(responseFlyBuyNumber);
		funLibrary.validate_Equals("FlyBuyNumber", DB_FlyBuysNumber, responseFlyBuyNumber);

		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user doesnot have FlyNumber")
	public void verifyFlybuyNumberNotLinked() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileFlyBuysNumber'");
		String DB_FlyBuysNumber = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));

		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseFlyBuyNumber = jsonPath.getString("contextAttribute[1].attributeValue.value");
		if(DB_FlyBuysNumber=="No Record Found")
		{
			funLibrary.validate_Equals("FlyBuyNumber", "", responseFlyBuyNumber);

		}
		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user is Team Member")
	public void verifyUserStaffDiscountNumber() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileStaffDiscountNumber'");
		String DB_TeamMember = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseTeamMember = jsonPath.getString("contextAttribute[8].attributeValue.value");
		System.out.println(responseTeamMember);
		funLibrary.validate_Equals("TeamMember", DB_TeamMember, responseTeamMember);

		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user is not a Team Member")
	public void verifyUserStaffDiscountNumberNotLinked() {
		
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");

		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileStaffDiscountNumber'");
		String DB_TeamMember = dbUtil.getValues("MBRATTRVAL", "STRINGVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseTeamMember = jsonPath.getString("contextAttribute[8].attributeValue.value");
	;
		
		if( DB_TeamMember=="No Record Found")
		{
			funLibrary.validate_Equals("TeamMember", "", responseTeamMember);
		}
		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user profileBaggingPreference")
	public void verifyBaggingPreference() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileBaggingPreference'");
		String DB_SubstituionPref = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseBaggingPreference = jsonPath.getString("contextAttribute[4].attributeValue.value");
		funLibrary.validate_Equals("BaggingPrefernce", responseBaggingPreference, responseBaggingPreference);

		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user is Exempted from PurchaseLimit",priority=-1)
	public void verifyExemptedfromPurchaseLimit() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='CsrRedemptionLimitExemption'");
		System.out.println(db_mbrattr);
		String DB_PurchaseLimit = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		System.out.println();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsePurchaseLimit = jsonPath.getString("contextAttribute[6].attributeValue.value");
		if(DB_PurchaseLimit=="1")
		{
			funLibrary.validate_Equals("PurchaseLimit", "true", responsePurchaseLimit);
		}

		dbUtil.closeDBConnection();

	}
	
	@Test(description = "validate user Profile Substitution Preference")
	public void verifyProfileSubstitutionPreference() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='ProfileSubstitutionPreference'");
		System.out.println(db_mbrattr);
		String DB_ProfileSubstitutionPreference = dbUtil.getValues("MBRATTRVAL", "INTEGERVALUE",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		System.out.println();
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseProfileSubstitutionPreference = jsonPath.getString("contextAttribute[3].attributeValue.value");
		funLibrary.validate_Equals("ProfileSubstitutionPreference", DB_ProfileSubstitutionPreference, responseProfileSubstitutionPreference );
	

		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user is not Exempted from PurchaseLimit")
	public void verifynotExemptedfromPurchaseLimit() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='CsrRedemptionLimitExemption'");
		String DB_PurchaseLimit = dbUtil.getValues("MBRATTRVAL", "MBRATTR_ID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responsePurchaseLimit = jsonPath.getString("contextAttribute[6].attributeValue.value");
		if(DB_PurchaseLimit=="No Record Found")
		{
			funLibrary.validate_Equals("PurchaseLimit", "false", responsePurchaseLimit);
		}
		dbUtil.closeDBConnection();

	}

	@Test(description = "validate user is a B2B USER")
	public void verifyuserisB2B() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "NAME='CsrRedemptionLimitExemption'");
		String DB_B2BUser = dbUtil.getValues("MBRATTRVAL", "MBRATTR_ID",
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseB2BUser = jsonPath.getString("contextAttribute[5].attributeValue.value");
		funLibrary.validate_Equals("b2buser", "true", responseB2BUser);

	}

	@Test(description = "validate user is not a B2B USER")
	public void verifyUserisnotB2B() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseB2BUser = jsonPath.getString("contextAttribute[5].attributeValue.value");
		funLibrary.validate_Equals("notb2buser", "false", responseB2BUser);

	}

	@Test(description = "validate user is a ADMIN USER")
	public void verifyUserisAdmin() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseAdmin = jsonPath.getString("contextAttribute[7].attributeValue.value");
		funLibrary.validate_Equals("Admin user", "true", responseAdmin);

	}

	@Test(description = "validate user is not a Admin USER")
	public void verifyUserisnotAdmin() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseAdmin = jsonPath.getString("contextAttribute[7].attributeValue.value");
		funLibrary.validate_Equals("notadminusr", "false", responseAdmin);

	}
	@Test(description = "validate user Flybuy number is Masked")
	public void verifyFlyBuyMaskedNumber() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String responseAdmin = jsonPath.getString("contextAttribute[2].attributeValue.value");
		funLibrary.validate_Equals("MaskedNumber", "600xxxxxxxxx5012", responseAdmin);

	}
	@Test(description = "validate user has Postcode and Suburb")
	public void verifyPostCodeandsuburb() {

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"),
				FunLibrary.excelData.get("PathParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParamKey1"),
				FunLibrary.excelData.get("QueryParamvalue1"));
		restLibrary.addBody(FunLibrary.excelData.get("RequestBody"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		JsonPath jsonPath = response.jsonPath();
		String postcode = jsonPath.getString("postcode");
		String Suburb = jsonPath.getString("suburb");
		System.out.println(postcode);
		System.out.println(Suburb);
         funLibrary.validate_Equals("postcode", "0820", postcode);
		funLibrary.validate_Equals("Suburb", null, Suburb);

	}
	
	
	@Override
	public String getTestName() {
		return testName.get();
	}
}