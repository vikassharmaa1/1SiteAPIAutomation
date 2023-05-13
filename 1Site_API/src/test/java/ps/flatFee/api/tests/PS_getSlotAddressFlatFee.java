package ps.flatFee.api.tests;

import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.api.PS;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PS_getSlotAddressFlatFee extends Base_Class_API implements ITest {

	@Test(description = "Verify Isflatfee exist for HD")
	public void validateFlatFeeHD() {
		Response response = null;
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		String storeid = FunLibrary.excelData.get("StoreId");
		String addressid = FunLibrary.excelData.get("AddressID");
		response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, storeid, addressid);
		response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, storeid, "1", "1", "Lisarow",
				"2250", "NSW", "NORMAL");

		// Get the response code
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// Get the required path of each field

		String partNumberRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].PARTNUMBER");
		String isFlatfeeRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].isFlatFee");
		String relationalSKURP = funLibrary.getJsonPathValue(response,
				"slotBagging.slotBaggingOptions[0].relationalSKU");
		String relationalBagTypeRP = funLibrary.getJsonPathValue(response,
				"slotBagging.slotBaggingOptions[0].relationalBagType");
		String bitFlagValueRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingBitFlag");

		// Get the value from database
		DatabaseUtilities dbUtil_SIT = new DatabaseUtilities("SIT");

		String relationalBagTypeDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "BAGNAME",
				"PARTNUMBER = '" + FunLibrary.excelData.get("Relational_Sku") + "' ");
		String partNumberDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "PARTNUMBER",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String relationalSKUDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "RELATIONAL_SKU",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String uomDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "UOM",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String transactionalSKUDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "FIELD3",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");

		dbUtil_SIT.closeDBConnection();

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");

		String bitFlagValueDB = dbUtil_DM.getValues("DMOWNER.delsubzone", "HD_BAGGING_OPTIONS",
				"delzone_name = '" + FunLibrary.excelData.get("Delzone_Name") + "' " + "and Suburb_ID= '"
						+ FunLibrary.excelData.get("Suburb_Id") + "'");

		// Verify response

		if (uomDB.contains("ORDER")) {

			if (isFlatfeeRP.contains("true")) {

				funLibrary.validate_Equals("partNumber", partNumberDB, partNumberRP);
				funLibrary.validate_Equals("relationalSKU", relationalSKUDB, relationalSKURP);
				funLibrary.validate_Equals("bitFlagValue", bitFlagValueDB, bitFlagValueRP);
				funLibrary.validate_Equals("transactionalSKU", transactionalSKUDB,
						FunLibrary.excelData.get("Transactional_SKU"));
				funLibrary.validate_Equals("relationalBagType", relationalBagTypeDB, relationalBagTypeRP);
				funLibrary.Assert.assertAll();

			} else {
				funLibrary.Assert.assertTrue(false, "Not a flat fee");
				funLibrary.testLog.info("Not a flat fee");
			}

		} else {
			funLibrary.Assert.assertTrue(false, "Not a flat fee");
			funLibrary.testLog.info("Not a flat fee");
		}

	}

	@Test(description = "Verify Isflatfee exist for PD")
	public void validateFlatFeePD() {

		Response response = null;
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		String storeid = FunLibrary.excelData.get("StoreId");
		String addressid = FunLibrary.excelData.get("AddressID");
		response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, storeid, addressid);
		response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, storeid, "1", "1", "Lisarow",
				"2250", "NSW", "PARTNERDELIVERY");

		// Get the response code
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// Get the required path of each field

		String partNumberRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].PARTNUMBER");
		String isFlatfeeRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].isFlatFee");
		String relationalSKURP = funLibrary.getJsonPathValue(response,
				"slotBagging.slotBaggingOptions[0].relationalSKU");
		String relationalBagTypeRP = funLibrary.getJsonPathValue(response,
				"slotBagging.slotBaggingOptions[0].relationalBagType");
		String bitFlagValueRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingBitFlag");

		// Get the value from database
		DatabaseUtilities dbUtil_SIT = new DatabaseUtilities("SIT");

		String relationalBagTypeDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "BAGNAME",
				"PARTNUMBER = '" + FunLibrary.excelData.get("Relational_Sku") + "' ");
		String partNumberDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "PARTNUMBER",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String relationalSKUDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "RELATIONAL_SKU",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String uomDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "UOM",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String transactionalSKUDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "FIELD3",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");

		dbUtil_SIT.closeDBConnection();

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");

		String bitFlagValueDB = dbUtil_DM.getValues("DMOWNER.delsubzone", "HD_BAGGING_OPTIONS",
				"delzone_name = '" + FunLibrary.excelData.get("Delzone_Name") + "' " + "and Suburb_ID= '"
						+ FunLibrary.excelData.get("Suburb_Id") + "'");

		// Verify response

		if (uomDB.contains("ORDER")) {

			if (isFlatfeeRP.contains("true")) {

				funLibrary.validate_Equals("partNumber", partNumberDB, partNumberRP);
				funLibrary.validate_Equals("relationalSKU", relationalSKUDB, relationalSKURP);
				funLibrary.validate_Equals("bitFlagValue", bitFlagValueDB, bitFlagValueRP);
				funLibrary.validate_Equals("transactionalSKU", transactionalSKUDB,
						FunLibrary.excelData.get("Transactional_SKU"));
				funLibrary.validate_Equals("relationalBagType", relationalBagTypeDB, relationalBagTypeRP);
				funLibrary.Assert.assertAll();

			} else {
				funLibrary.Assert.assertTrue(false, "Not a flat fee");
				funLibrary.testLog.info("Not a flat fee");
			}

		} else {
			funLibrary.Assert.assertTrue(false, "Not a flat fee");
			funLibrary.testLog.info("Not a flat fee");
		}

	}

	@Test(description = "Verify non flatfee for HD")
	public void validateNonFlatFeeHD() {

		Response response = null;
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		String storeid = FunLibrary.excelData.get("StoreId");
		String addressid = FunLibrary.excelData.get("AddressID");
		response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, storeid, addressid);
		response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, storeid, "1", "1", "Riverton",
				"6148", "WA", "NORMAL");

		// Get the response code funLibrary.validateStatusCode(response,
		FunLibrary.excelData.get("StatusCode");

		// Get the required path of each field

		String partNumberRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].PARTNUMBER");
		String isFlatfeeRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].isFlatFee");
		String bitFlagValueRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingBitFlag");

		// Get the value from database DatabaseUtilities dbUtil_SIT = new
		DatabaseUtilities dbUtil_SIT = new DatabaseUtilities("SIT");

		String partNumberDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "PARTNUMBER",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String uomDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "UOM",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");

		dbUtil_SIT.closeDBConnection();

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");

		String bitFlagValueDB = dbUtil_DM.getValues("DMOWNER.delsubzone", "HD_BAGGING_OPTIONS",
				"delzone_name = '" + FunLibrary.excelData.get("Delzone_Name") + "' " + "and Suburb_ID= '"
						+ FunLibrary.excelData.get("Suburb_Id") + "'");

		// Verify response

		if (uomDB.contains("UNIT")) {

			if (isFlatfeeRP.contains("false")) {
				if (response.jsonPath().getJsonObject("slotBagging.slotBaggingOptions[0].relationalSKU") == null) {
					funLibrary.Assert.assertTrue(true, "relational_SKU attribute not present for non flat fee");
					funLibrary.testLog.info("relationalSKU attribute not present for non flat fee");
				}
				if (response.jsonPath().getJsonObject("slotBagging.slotBaggingOptions[0].relationalBagType") == null) {
					funLibrary.Assert.assertTrue(true, "relational_BagType attribute not present for non flat fee");
					funLibrary.testLog.info("relational_BagType attribute not present for non flat fee");
				}

				funLibrary.validate_Equals("partNumber", partNumberDB, partNumberRP);
				funLibrary.validate_Equals("bitFlagValue", bitFlagValueDB, bitFlagValueRP);
				funLibrary.Assert.assertAll();

			} else {
				funLibrary.Assert.assertTrue(false, "flat fee");
				funLibrary.testLog.info("flat fee");
			}

		} else {
			funLibrary.Assert.assertTrue(false, "flat fee");
			funLibrary.testLog.info("flat fee");
		}

	}

	@Test(description = "Verify non flatfee for HD")
	public void validateNonFlatFeePD() {

		Response response = null;
		String authorization = FunLibrary.excelData.get("Header2_Value");
		String userAuthorization = FunLibrary.excelData.get("Header1_Value");
		String storeid = FunLibrary.excelData.get("StoreId");
		String addressid = FunLibrary.excelData.get("AddressID");
		response = PS.localizationByAddressId("", "", "", authorization, userAuthorization, storeid, addressid);
		response = PS.getSlotByAddress("", "", "", authorization, userAuthorization, storeid, "1", "1", "Riverton",
				"6148", "WA", "PARTNERDELIVERY");

		// Get the response code funLibrary.validateStatusCode(response,
		FunLibrary.excelData.get("StatusCode");

		// Get the required path of each field

		String partNumberRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].PARTNUMBER");
		String isFlatfeeRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingOptions[0].isFlatFee");
		String bitFlagValueRP = funLibrary.getJsonPathValue(response, "slotBagging.slotBaggingBitFlag");

		// Get the value from database DatabaseUtilities dbUtil_SIT = new
		DatabaseUtilities dbUtil_SIT = new DatabaseUtilities("SIT");

		String partNumberDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "PARTNUMBER",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");
		String uomDB = dbUtil_SIT.getValues("WCSOWNER.XBAGS", "UOM",
				"BITVALUE = '" + FunLibrary.excelData.get("Bit_Flag") + "' ");

		dbUtil_SIT.closeDBConnection();

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");

		String bitFlagValueDB = dbUtil_DM.getValues("DMOWNER.delsubzone", "HD_BAGGING_OPTIONS",
				"delzone_name = '" + FunLibrary.excelData.get("Delzone_Name") + "' " + "and Suburb_ID= '"
						+ FunLibrary.excelData.get("Suburb_Id") + "'");

		// Verify response

		if (uomDB.contains("UNIT")) {

			if (isFlatfeeRP.contains("false")) {
				if (response.jsonPath().getJsonObject("slotBagging.slotBaggingOptions[0].relationalSKU") == null) {
					funLibrary.Assert.assertTrue(true, "relational_SKU attribute not present for non flat fee");
					funLibrary.testLog.info("relationalSKU attribute not present for non flat fee");
				}
				if (response.jsonPath().getJsonObject("slotBagging.slotBaggingOptions[0].relationalBagType") == null) {
					funLibrary.Assert.assertTrue(true, "relational_BagType attribute not present for non flat fee");
					funLibrary.testLog.info("relational_BagType attribute not present for non flat fee");
				}

				funLibrary.validate_Equals("partNumber", partNumberDB, partNumberRP);
				funLibrary.validate_Equals("bitFlagValue", bitFlagValueDB, bitFlagValueRP);
				funLibrary.Assert.assertAll();

			} else {
				funLibrary.Assert.assertTrue(false, "flat fee");
				funLibrary.testLog.info("flat fee");
			}

		} else {
			funLibrary.Assert.assertTrue(false, "flat fee");
			funLibrary.testLog.info("flat fee");
		}

	}

	@Override
	public String getTestName() {
		// TODO Auto-generated method stub
		return null;
	}

}
