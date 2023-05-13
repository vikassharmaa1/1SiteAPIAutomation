package ps.profileAttributes.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_ProfileAttributes extends Base_Class_API implements ITest {

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode",
					FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
					FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
					FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'",
					FunLibrary.excelData.get("ErrorMessage"));
			break;
		case "404":
			// funLibrary.validateStatusCode(response,
			// FunLibrary.excelData.get("StatusCode"));
			Assert.assertEquals(response.getBody().asString().equals(""), true);
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
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message",
				FunLibrary.excelData.get("ErrorMessage"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority",
				FunLibrary.excelData.get("ErrorLevel/Priority"));
		funLibrary.Assert.assertAll();
	}
	@Test(description = "validate B2B Attributes")
	public void verifyB2B() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String member_id = dbUtil.getValues("users", "users_id", "field1='testbuss051@getnada.com'");
		System.out.println(member_id);

		String DB_parentMemberDN = dbUtil.getValues("USERREG", "LOGONID", "users_id='" + member_id + "'");
		System.out.println(DB_parentMemberDN);

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
     	Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "isB2B", "true");
		funLibrary.validateJSONPathValue_Equals(response, "parentMemberDN",DB_parentMemberDN);
	    funLibrary.validateJSONPathValue_Equals(response, "edmTotalSpent", getDbValue("FLOATVALUE","EdmTotalSpend                                                                                    "));
		funLibrary.validateJSONPathValue_Equals(response, "edmFirstOrderDate", getDbValue("DATETIMEVALUE","EdmFirstOrderDate","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmDeliveryCustomer", getDbValue("DATETIMEVALUE","EdmDeliveryCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "colUserId", member_id);
		funLibrary.validateJSONPathValue_Equals(response, "csrRedemptionLimitExemption", getDbValue("INTEGERVALUE","CsrRedemptionLimitExemption"));
		funLibrary.validateJSONPathValue_Equals(response, "edmClickAndCollectCustomer", getDbValue("DATETIMEVALUE","EdmClickAndCollectCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmOrderCount", getDbValue("INTEGERVALUE","EdmOrderCount"));
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.isAdminUser", "false");
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.industry", getDbValue("STRINGVALUE","ProfileBusinessCustomerType"));
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.businessType", "Organisation");
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.hasSapAccount", "true");

	}
	@Test(description = "validate B2C attributes")
	public void verifyB2C() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String member_id = dbUtil.getValues("users", "users_id", "field1='npstest1@mailinator.com'");
		System.out.println(member_id);

		String DB_parentMemberDN = dbUtil.getValues("USERREG", "LOGONID", "users_id='" + member_id + "'");
		System.out.println(DB_parentMemberDN);

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
     	Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "isB2B", "false");
		funLibrary.validateJSONPathValue_Equals(response, "parentMemberDN",DB_parentMemberDN);
	    funLibrary.validateJSONPathValue_Equals(response, "edmTotalSpent", getDbValue("FLOATVALUE","EdmTotalSpend"));
		funLibrary.validateJSONPathValue_Equals(response, "edmFirstOrderDate", getDbValue("DATETIMEVALUE","EdmFirstOrderDate","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmDeliveryCustomer", getDbValue("DATETIMEVALUE","EdmDeliveryCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "colUserId", member_id);
		funLibrary.validateJSONPathValue_Equals(response, "csrRedemptionLimitExemption", getDbValue("INTEGERVALUE","CsrRedemptionLimitExemption"));
		funLibrary.validateJSONPathValue_Equals(response, "edmClickAndCollectCustomer", getDbValue("DATETIMEVALUE","EdmClickAndCollectCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmOrderCount", getDbValue("INTEGERVALUE","EdmOrderCount"));		
	}
	
	@Test(description = "validate aAdmin Attributes")
	public void verifyAdmin() {

		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String member_id = dbUtil.getValues("users", "users_id", "field1='admin1@getnada.com'");
		System.out.println(member_id);

		String DB_parentMemberDN = dbUtil.getValues("USERREG", "LOGONID", "users_id='" + member_id + "'");
		System.out.println(DB_parentMemberDN);

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
     	Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "isB2B", "true");
		funLibrary.validateJSONPathValue_Equals(response, "parentMemberDN",DB_parentMemberDN);
	    funLibrary.validateJSONPathValue_Equals(response, "edmTotalSpent", getDbValue("FLOATVALUE","EdmTotalSpend                                                                                    "));
		funLibrary.validateJSONPathValue_Equals(response, "edmFirstOrderDate", getDbValue("DATETIMEVALUE","EdmFirstOrderDate","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmDeliveryCustomer", getDbValue("DATETIMEVALUE","EdmDeliveryCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "colUserId", member_id);
		funLibrary.validateJSONPathValue_Equals(response, "csrRedemptionLimitExemption", getDbValue("INTEGERVALUE","CsrRedemptionLimitExemption"));
		funLibrary.validateJSONPathValue_Equals(response, "edmClickAndCollectCustomer", getDbValue("DATETIMEVALUE","EdmClickAndCollectCustomer","1"));
		funLibrary.validateJSONPathValue_Equals(response, "edmOrderCount", getDbValue("INTEGERVALUE","EdmOrderCount"));
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.isAdminUser", "true");
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.industry", getDbValue("STRINGVALUE","ProfileBusinessCustomerType"));
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.businessType", "Organisation");
		funLibrary.validateJSONPathValue_Equals(response, "b2bAttributes.hasSapAccount", "true");

	}
	@Test(description = "validate schema of response json")
	public void validateSchemaB2B() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String member_id = dbUtil.getValues("users", "users_id", "field1='b2bsitcompaignuser13@getnada.com'");
		System.out.println(member_id);
		String DB_parentMemberDN = dbUtil.getValues("USERREG", "LOGONID", "users_id='" + member_id + "'");
		System.out.println(DB_parentMemberDN);
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response, "attributesB2B");
	}
	@Test(description = "validate schema of response json")
	public void validateSchemaB2C() {
		DatabaseUtilities dbUtil = new DatabaseUtilities("SIT");
		String member_id = dbUtil.getValues("users", "users_id", "field1='npstest1@mailinator.com'");
		System.out.println(member_id);
		String DB_parentMemberDN = dbUtil.getValues("USERREG", "LOGONID", "users_id='" + member_id + "'");
		System.out.println(DB_parentMemberDN);
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
				FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader("UserAuthorization", FunLibrary.excelData.get("Header_UserAuthorization"));
		restLibrary.addHeader("Authorization", FunLibrary.excelData.get("Header_Authorization"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response, "attributesB2C");
	}
	

	public  String getDbValue( String column,String name)
	{
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "Name='"+name+"'");
		String value = dbUtil.getValues("MBRATTRVAL", column,
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");
		return value;
		
	}
	public  String getDbValue( String column,String name,String Date)

	{
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		
		String db_mbrattr = dbUtil.getValues("mbrattr", "MBRATTR_ID", "Name='"+name+"'");
		String value1 = dbUtil.getValues("MBRATTRVAL", column,
				"member_id = (select users_id from users where field1='" + FunLibrary.excelData.get("UserName")
						+ "') and mbrattr_id='" + db_mbrattr + "'");	
		value1=value1.trim();
		String[] ADD=value1.split("\\s+");
		String value=ADD[0]+"T";		
		String[] time=ADD[1].split("\\.");
		char t=time[1].charAt(0);
		String main=value+time[0]+":"+t;
   	return main;
		
	}
	@Override
	public String getTestName() {
		return testName.get();
	}
}