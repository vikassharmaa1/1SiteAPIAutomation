package ps.trolley.api.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.APILibrary;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_ShortSummary extends Base_Class_API implements ITest {

	Response response = null;
	RestLibrary restLibrary = null;

	public void OrderLightWeightSummary_API() {
		restLibrary = new RestLibrary();
		response = APILibrary.OrderLightWeightSummary_PS(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"), FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"),
				FunLibrary.excelData.get("QueryParam_storeId"));
		//restLibrary.getResponseBody(response);
	}

	@Test(dataProvider = "valid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_ValidRequest(String testname) {
		OrderLightWeightSummary_API();

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		Assert.assertEquals(response.getBody().asString().equals(""), true);
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "invalid", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_InvalidRequest(String testname) {
		OrderLightWeightSummary_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
		funLibrary.Assert.assertAll();
	}

	@Test(dataProvider = "functional", dataProviderClass = API_DataProvider.class)
	public void validateErroCodes_Functional(String testname) {
		OrderLightWeightSummary_API();
		String errorCode = FunLibrary.excelData.get("ErrorCode");
		String errorMessage = FunLibrary.excelData.get("ErrorMessage");
		String errorLevel = FunLibrary.excelData.get("ErrorLevel/Priority");

		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", errorCode);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", errorMessage);
		funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", errorLevel);
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateLightOrderSummary_HD() {
		String orderId = "";
		String addressId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		addressId = dbUtils.getValues("ADDRESS", "ADDRESS_ID", "member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"' and STATUS='P' and ADDRESSTYPE='SB'");

		// Localizing to HD address
		APILibrary.localizationByAddressId_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", addressId);

		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMLocationZoneId' and ORDERS_ID='" + orderId + "'");
		webStoreId = zoneId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		dbUtils.closeDBConnection();

		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();

		OrderLightWeightSummary_API();
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_trolley_HD");
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId", addressId);
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.Assert.assertAll();
	}

	@Test
	public void validateLightOrderSummary_CC() {
		String orderId = "";
		String serviceType = "";
		String ccLocationId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		
		// Localizing to HD address
		APILibrary.localizationByLocationId_PS("", "", "", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", "0404CC0404");
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		ccLocationId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMCollectionPointId' and ORDERS_ID='" + orderId + "'");
		webStoreId = ccLocationId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		dbUtils.closeDBConnection();

		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();

		OrderLightWeightSummary_API();
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_trolley_CC");
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);
		funLibrary.validateJSONPathValue_Equals(response, "ccLocationId", ccLocationId);
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.Assert.assertAll();
	}


	@Test(description = "Validate that reponse when user is localised by Suburb and Poscode")
	public void validateTrolleyLocalisedBySuburbPostcode() {
		
		String orderId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String suburbOfSPOnlyLocalisation="";
		String postcodeOfSPOnlyLocalisation="";
		String suburb = "";
		String postcode = "";
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		suburb = dbUtils.getValues("ADDRESS", "CITY", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		postcode = dbUtils.getValues("ADDRESS", "ZIPCODE", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		// Localizing to HD address by suburb and postcode. First localizing to RD location to remove field address id
		APILibrary.localizationBySuburbPostCode_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", "0822", "Daly River");		
		APILibrary.localizationBySuburbPostCode_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", postcode, suburb);
		
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMLocationZoneId' and ORDERS_ID='" + orderId + "'");
		webStoreId = zoneId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		suburbOfSPOnlyLocalisation = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='suburbOfSPOnlyLocalisation'");
		postcodeOfSPOnlyLocalisation = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='postcodeOfSPOnlyLocalisation'");
		dbUtils.closeDBConnection();
		
		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();
		
		OrderLightWeightSummary_API();
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_SuburbPostCode_HD");
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);		
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation", suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation", postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Validate that reponse when user is localised by full address")
	public void validateTrolleyLocalisedByFulladdress() {
		String orderId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String verificationId="";
		String CCPAddressID="";
		
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		verificationId = dbUtils.getValues("XADDRESS", "VALIDATIONID", "ADDRESS_ID = (select ADDRESS_ID from address where  member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"')");
		CCPAddressID = dbUtils.getValues("XADDRESS", "CCPADDRESSID", "ADDRESS_ID = (select ADDRESS_ID from address where  member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"')");
		// Localizing to HD address by suburb and postcode
		APILibrary.localizationByFullAddress_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510","1", "0820", "Bayview", verificationId, CCPAddressID);
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMLocationZoneId' and ORDERS_ID='" + orderId + "'");
		webStoreId = zoneId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		
		
		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();
		
		OrderLightWeightSummary_API();
		
		String orderAddressId = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='OrderAddressId'");
		dbUtils.closeDBConnection();
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_trolley_FullAddressLocalize");
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);		
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", CCPAddressID);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId", orderAddressId);		
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Validate that reponse when user is localised by RD Suburb and Poscode")
	public void validateTrolleyLocalisedByRDSuburbPostcode() {
		String orderId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String suburbOfSPOnlyLocalisation="";
		String postcodeOfSPOnlyLocalisation="";
		String suburb = "";
		String postcode = "";
		
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		suburb = dbUtils.getValues("ADDRESS", "CITY", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		postcode = dbUtils.getValues("ADDRESS", "ZIPCODE", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		
		// Localizing to HD address by suburb and postcode.  First localizing to HD location to remove field address id
		APILibrary.localizationBySuburbPostCode_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", "0820", "Bayview");
		APILibrary.localizationBySuburbPostCode_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510", postcode, suburb);
		
		// getting values to validate against api response
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMCollectionPointId' and ORDERS_ID='" + orderId + "'");
		webStoreId = zoneId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		suburbOfSPOnlyLocalisation = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='suburbOfSPOnlyLocalisation'");
		postcodeOfSPOnlyLocalisation = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='postcodeOfSPOnlyLocalisation'");
		dbUtils.closeDBConnection();
		
		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();
		
		OrderLightWeightSummary_API();
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_SuburbPostCode_RD");
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);		
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "suburbOfSPOnlyLocalisation", suburbOfSPOnlyLocalisation);
		funLibrary.validateJSONPathValue_Equals(response, "postcodeOfSPOnlyLocalisation", postcodeOfSPOnlyLocalisation);
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Validate that reponse when user is localised by RD full address")
	public void validateTrolleyLocalisedByRDFulladdress() {
		String orderId = "";
		String serviceType = "";
		String zoneId = "";
		String webStoreId = "";
		String storeId = "";
		String catalogId = "";
		String verificationId="";
		String CCPAddressID="";
		String suburb = "";
		String postcode = "";
		
		DatabaseUtilities dbUtils = new DatabaseUtilities();
		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and STATUS='P'");
		verificationId = dbUtils.getValues("XADDRESS", "VALIDATIONID", "ADDRESS_ID = (select ADDRESS_ID from address where  member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"')");
		CCPAddressID = dbUtils.getValues("XADDRESS", "CCPADDRESSID", "ADDRESS_ID = (select ADDRESS_ID from address where  member_id=(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"')");
		suburb = dbUtils.getValues("ADDRESS", "CITY", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		postcode = dbUtils.getValues("ADDRESS", "ZIPCODE", "MEMBER_ID =(select users_id from users where field1='" + FunLibrary.excelData.get("UserId") + "') and NICKNAME = '"+FunLibrary.excelData.get("AddressName")+"'");
		// Localizing to HD address by suburb and postcode
		APILibrary.localizationByFullAddress_PS("","","", FunLibrary.excelData.get("Header_Authorization"), FunLibrary.excelData.get("Header_UserAuthorization"), "20510","1", postcode, suburb, verificationId, CCPAddressID);
		serviceType = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMServiceType' and ORDERS_ID='" + orderId + "'");
		zoneId = dbUtils.getValues("XORDERATTR", "VALUE", "NAME = 'DMCollectionPointId' and ORDERS_ID='" + orderId + "'");
		webStoreId = zoneId.substring(0, 4);
		catalogId = dbUtils.getValues("XSTOREFFMCATREL", "CATALOG_ID", "FFMEXTID ='" + webStoreId + "'");
		storeId = dbUtils.getValues("XSTOREFFMCATREL", "STORE_ID", "FFMEXTID ='" + webStoreId + "'");
		
		
		Integer s = Integer.parseInt(storeId);
		s = s + 10000;
		storeId = s.toString();
		
		OrderLightWeightSummary_API();
		
		String orderAddressId = dbUtils.getValues("XORDERATTR", "VALUE", "orders_id='" + orderId + "' and NAME='rdAddressId'");
		dbUtils.closeDBConnection();
		
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.validateJsonStructure(response, "ShortSummary/shortsummary_trolley_FullAddressLocalize");
		funLibrary.validateJSONPathValue_Equals(response, "orderId", orderId);
		funLibrary.validateJSONPathValue_Equals(response, "colWebstoreId", webStoreId);
		funLibrary.validateJSONPathValue_Equals(response, "storeId", storeId);
		funLibrary.validateJSONPathValue_Equals(response, "catalogId", catalogId);		
		funLibrary.validateJSONPathValue_Equals(response, "serviceType", serviceType);
		funLibrary.validateJSONPathValue_Equals(response, "ccpAddressId", CCPAddressID);
		funLibrary.validateJSONPathValue_Equals(response, "dlAddressId", orderAddressId);		
		funLibrary.Assert.assertAll();
		funLibrary.Assert.assertAll();
	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
