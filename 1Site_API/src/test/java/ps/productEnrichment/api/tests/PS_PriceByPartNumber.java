package ps.productEnrichment.api.tests;

import java.text.DecimalFormat;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.API_DataProvider;

import io.restassured.response.Response;
import productEnrichment.api.response.pojo.PartNumber;
import productEnrichment.api.response.pojo.Root;
import productEnrichment.searchindex.response.pojo.CatalogEntryView;
import productEnrichment.searchindex.response.pojo.SearchIndexRoot;

public class PS_PriceByPartNumber extends Base_Class_API implements ITest {

	Root apiRoot = null;
	SearchIndexRoot solrRoot = null;
	String[] partnumber = null;

	public void productEnrichmentAPI() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));

		Response apiResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(apiResponse);
		funLibrary.validateStatusCode(apiResponse, "200");
		funLibrary.validate_NotEquals("TotalCount", "0", funLibrary.getJsonPathValue(apiResponse, "totalCount"));
		funLibrary.Assert.assertAll("No data found for given partnumbers");

		apiRoot = (Root) restLibrary.getResponseBody(apiResponse, Root.class);
	}

	public void solrAPI() {
		String searchIndexPartnumber = "";
		partnumber = FunLibrary.excelData.get("QueryParameterValue2").split(",");
		for (int i = 0; i < partnumber.length; ++i) {
			String part = "&partNumber=" + partnumber[i] + "P";
			searchIndexPartnumber = searchIndexPartnumber + part;
		}
		String searchIndexBaseURI = FunLibrary.excelData.get("SolarAPI") + searchIndexPartnumber;

		RestLibrary restLibrary = new RestLibrary(FunLibrary.excelData.get("PS_URI"), searchIndexBaseURI, FunLibrary.excelData.get("RequestType"));

		Response solrResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(solrResponse);
		solrRoot = (SearchIndexRoot) restLibrary.getResponseBody(solrResponse, SearchIndexRoot.class);
	}

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		if(FunLibrary.excelData.get("QueryParameterKey") != "") {
			restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		}
		if(FunLibrary.excelData.get("QueryParameterKey") != "") {
			restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		}

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			if(FunLibrary.excelData.get("TestMethodName").contains("Mandatory")) {
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.ParameterName", FunLibrary.excelData.get("ParameterName"));
			} else if(FunLibrary.excelData.get("TestMethodName").equals("ImproperFormat") || FunLibrary.excelData.get("TestMethodName").equals("ImproperDataTypes")) {
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.ParameterName", FunLibrary.excelData.get("ParameterName"));
				funLibrary.validateJSONPathValue_Equals(response, "errors[0].properties.expectedDataType", FunLibrary.excelData.get("ExpectedDataType"));
			}

		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].message", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].priority", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "404":
			Assert.assertEquals(response.getBody().asString().equals(""), true);
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC01- Verify the Partnumber")
	public void validatePartNumber() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating PartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting PartNumber
			// PartNumber from Search Index Response
			String partNumber_SI = searchIndexData.getPartNumber();
			// PartNumber from API Response
			String partNumber = apiResponseData.getPartNumber() + "P";
			// validate partNumber
			if(partNumber_SI == null || partNumber == null) {
				testLog.error("PartNumber Actual:" + partNumber);
				testLog.error("PartNumber Expected:" + partNumber_SI);
				Assert.assertTrue(false, "Partnumber field is null for" + partnumbervalue);
			} else {
				testLog.info("PartNumber Actual:" + partNumber);
				testLog.info("PartNumber Expected:" + partNumber_SI);
				Assert.assertEquals(partNumber, partNumber_SI, "PartNumber value for " + partnumbervalue + " is not matching");
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC02- Verify the attributes of Partnumber")
	public void validateAttributesOfPartNumber() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating AttributesOfPartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting AttributesOfPartNumber
			// AttributesofPartNumber from Search Index Response
			List<String> barcodeId_SI = searchIndexData.getAttributesMap().getbARCODEID();
			List<String> excludeFromSubstitutionFlag_SI = searchIndexData.getAttributesMap().geteXCLUDEFROMSUBSTITUTIONFLAG();
			List<String> weightedItemIndicator_SI = searchIndexData.getAttributesMap().getwEIGHTEDITEMINDICATOR();
			List<String> weightedByEach_SI = searchIndexData.getAttributesMap().getwEIGHTEDBYEACH();
			List<String> deliveryRestriction_SI = searchIndexData.getAttributesMap().getdELIVERYRESTRICTIONS();
			// AttributesofPartNumber from API Response
			List<String> barcodeId = apiResponseData.getAttributesOfPartNumber().getBarcodeID();
			Boolean excludeFromSubstitutionFlag = apiResponseData.getAttributesOfPartNumber().isExcludedFromSubstitution();
			Boolean weightedItemIndicator = apiResponseData.getAttributesOfPartNumber().isWeightedItemIndicator();
			Boolean weightedByEach = apiResponseData.getAttributesOfPartNumber().isWeigthedByEach();
			List<String> deliveryRestriction = apiResponseData.getAttributesOfPartNumber().getDeliveryRestrictions();

			// validate BarcodeId
			if(barcodeId == null) {
				testLog.warn("BarcodeId is " + barcodeId);

			} else {
				if(barcodeId_SI == null) {
					testLog.error("BarcodeId Actual:" + barcodeId);
					testLog.error("BarcodeId Expected:" + barcodeId_SI);
					Assert.assertTrue(false, "BarcodeId for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("BarcodeId Actual:" + barcodeId);
					testLog.info("BarcodeId Expected:" + barcodeId_SI);
					Assert.assertEquals(barcodeId_SI, barcodeId, "barcodeId for partnumber " + partnumbervalue + " is not matching");
				}
			}

			// validate excludedFromSubstitution
			if(excludeFromSubstitutionFlag == null) {
				testLog.warn("ExcludeFromSubstitution is " + excludeFromSubstitutionFlag);

			} else {
				if(excludeFromSubstitutionFlag_SI == null) {
					testLog.error("ExcludeFromSubstitution Actual:" + excludeFromSubstitutionFlag);
					testLog.error("ExcludeFromSubstitution Expected:" + excludeFromSubstitutionFlag_SI);
					Assert.assertTrue(false, "ExcludeFromSubstitution for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("ExcludeFromSubstitution Actual:" + excludeFromSubstitutionFlag);
					testLog.info("ExcludeFromSubstitution Expected:" + excludeFromSubstitutionFlag_SI);
					Assert.assertEquals(excludeFromSubstitutionFlag_SI.contains(excludeFromSubstitutionFlag.toString()), true, "ExcludeFromSubstitution for partnumber " + partnumbervalue + " is not matching");
				}
			}

			// validate weightedItemIndicator
			if(weightedItemIndicator == null) {
				testLog.warn("WeightedItemIndicator is " + weightedItemIndicator);

			} else {
				if(weightedItemIndicator_SI == null) {
					testLog.error("WeightedItemIndicator Actual:" + weightedItemIndicator);
					testLog.error("WeightedItemIndicator Expected:" + weightedItemIndicator_SI);
					Assert.assertTrue(false, "WeightedItemIndicator for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("WeightedItemIndicator Actual:" + weightedItemIndicator);
					testLog.info("WeightedItemIndicator Expected:" + weightedItemIndicator_SI);
					Assert.assertEquals(weightedItemIndicator_SI.contains(weightedItemIndicator.toString()), true, "WeightedItemIndicator for partnumber " + partnumbervalue + " is not matching");
				}
			}

			// validate weigthedByEach
			if(weightedByEach == null) {
				testLog.warn("WeightedByEach is " + weightedByEach);

			} else {
				if(weightedByEach_SI == null) {
					testLog.error("WeightedByEach Actual:" + weightedByEach);
					testLog.error("WeightedByEach Expected:" + weightedByEach_SI);
					Assert.assertTrue(false, "WeightedByEach for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("WeightedByEach Actual:" + weightedByEach);
					testLog.info("WeightedByEach Expected:" + weightedByEach_SI);
					Assert.assertEquals(weightedByEach_SI.contains(weightedByEach.toString()), true, "WeightedByEach for partnumber " + partnumbervalue + " is not matching");
				}
			}

			// validate deliveryRestrictions
			if(deliveryRestriction == null) {
				testLog.warn("DeliveryRestriction is " + deliveryRestriction);

			} else {
				if(deliveryRestriction_SI == null) {
					testLog.error("DeliveryRestriction Actual:" + deliveryRestriction);
					testLog.error("DeliveryRestriction Expected:" + deliveryRestriction_SI);
					Assert.assertTrue(false, "DeliveryRestriction for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("DeliveryRestriction Actual:" + deliveryRestriction);
					testLog.info("DeliveryRestriction Expected:" + deliveryRestriction_SI);
					Assert.assertEquals(deliveryRestriction_SI, deliveryRestriction, "DeliveryRestriction for partnumber " + partnumbervalue + " is not matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC03- Verify the Prices of Partnumber")
	public void validatePricesOfPartNumber() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Price of PartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting PricesOfPartNumber
			// PricesOfPartNumber from Search Index Response
			String offerPrice_SI = searchIndexData.getPrice().getOfferPrice();
			String listPrice_SI = searchIndexData.getPrice().getListPrice();
			// AttributesofPartNumber from API Response
			Double offerPrice = apiResponseData.getPricesOfPartNumber().getOfferPrice();
			Double listPrice = apiResponseData.getPricesOfPartNumber().getListPrice();

			// validate offerPrice
			if(offerPrice == null) {
				testLog.warn("offerPrice is " + offerPrice);

			} else {
				if(offerPrice_SI == null) {
					testLog.error("OfferPrice Actual:" + offerPrice);
					testLog.error("OfferPrice Expected:" + offerPrice_SI);
					Assert.assertTrue(false, "OfferPrice for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("OfferPrice Actual:" + offerPrice);
					testLog.info("OfferPrice Expected:" + offerPrice_SI);
					Assert.assertEquals(offerPrice.toString(), offerPrice_SI, "OfferPrice for partnumber " + partnumbervalue + " is not matching");
				}
			}
			// validate listPrice
			if(listPrice == null) {
				testLog.warn("ListPrice is " + listPrice);

			} else {

				if(listPrice_SI == null) {
					testLog.error("ListPrice Actual:" + listPrice);
					testLog.error("ListPrice Expected:" + listPrice_SI);
					Assert.assertTrue(false, "ListPrice for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("ListPrice Actual:" + listPrice);
					testLog.info("ListPrice Expected:" + listPrice_SI);
					Assert.assertEquals(listPrice.toString(), listPrice_SI, "ListPrice for partnumber " + partnumbervalue + " is not matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC04- Verify the Redemption of Partnumber")
	public void validateRedemptionsOfPartNumber() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Redemptions of PartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting RedemptionsOfPartNumber
			// RedemptionsOfPartNumber from Search Index Response
			List<String> redemptionLimitOfRetail_SI = searchIndexData.getAttributesMap().getrETAILLIMIT();
			List<String> redemptionLimitOfPromo_SI = searchIndexData.getAttributesMap().getpROMOTIONALLIMIT();

			// RedemptionsOfPartNumber from API Response
			Integer redemptionLimitOfRetail = apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfRetail();
			Integer redemptionLimitOfPromo = apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfPromo();

			// validate redemptionLimitOfRetail
			if(redemptionLimitOfRetail == null) {
				testLog.warn("redemptionLimitOfRetail is" + redemptionLimitOfRetail);
			} else {
				if(redemptionLimitOfRetail_SI == null) {
					testLog.error("redemptionLimitOfRetail Actual:" + redemptionLimitOfRetail);
					testLog.error("redemptionLimitOfRetail Expected:" + redemptionLimitOfRetail_SI);
					Assert.assertTrue(false, "redemptionLimitOfRetail for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("redemptionLimitOfRetail Actual:" + redemptionLimitOfRetail);
					testLog.info("redemptionLimitOfRetail Expected:" + redemptionLimitOfRetail_SI);
					Assert.assertEquals(redemptionLimitOfRetail_SI.contains(redemptionLimitOfRetail.toString()), true, "redemptionLimitOfRetail for partnumber " + redemptionLimitOfRetail + " is not matching");
				}
			}
			// validate redemptionLimitOfPromo
			if(redemptionLimitOfPromo == null) {
				testLog.warn("ListPrice is" + redemptionLimitOfPromo);

			} else {

				if(redemptionLimitOfPromo_SI == null) {
					testLog.error("redemptionLimitOfPromo Actual:" + redemptionLimitOfPromo);
					testLog.error("redemptionLimitOfPromo Expected:" + redemptionLimitOfPromo_SI);
					Assert.assertTrue(false, "redemptionLimitOfPromo for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("redemptionLimitOfPromo Actual:" + redemptionLimitOfPromo);
					testLog.info("redemptionLimitOfPromo Expected:" + redemptionLimitOfPromo_SI);
					Assert.assertEquals(redemptionLimitOfPromo_SI.contains(redemptionLimitOfPromo.toString()), true, "redemptionLimitOfPromo for partnumber " + partnumbervalue + " is not matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC05- Verify the Restriction of Partnumber")
	public void validateRestrictionOfPartNumber() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Restriction of PartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting RestrictionofPartNumber
			// RestrictionofPartNumber from Search Index Response
			List<String> liquorAgeRestricted_SI = searchIndexData.getAttributesMap().getlIQUORAGERESTRICTIONFLAG();
			List<String> tobaccoAgeRestricted_SI = searchIndexData.getAttributesMap().gettOBACCOAGERESTRICTIONFLAG();

			// RestrictionofPartNumber from API Response
			Boolean liquorAgeRestricted = null;
			Boolean tobaccoAgeRestricted = null;
			try {
				liquorAgeRestricted = apiResponseData.getRestrictionOfPartNumber().isLiquorAgeRestricted();
			} catch (Exception e) {
				liquorAgeRestricted = null;
			}
			try {
				tobaccoAgeRestricted = apiResponseData.getRestrictionOfPartNumber().isTobaccoAgeRestricted();
			} catch (Exception e) {
				tobaccoAgeRestricted = null;
			}
			// validate liquorAgeRestricted
			if(liquorAgeRestricted == null) {
				testLog.warn("liquorAgeRestricted is" + liquorAgeRestricted);
			} else {
				if(liquorAgeRestricted_SI == null) {
					testLog.error("liquorAgeRestricted Actual:" + liquorAgeRestricted);
					testLog.error("liquorAgeRestricted Expected:" + liquorAgeRestricted_SI);
					Assert.assertTrue(false, "liquorAgeRestricted for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("liquorAgeRestricted Actual:" + liquorAgeRestricted);
					testLog.info("liquorAgeRestricted Expected:" + liquorAgeRestricted_SI);
					Assert.assertEquals(liquorAgeRestricted_SI.contains(liquorAgeRestricted.toString()), true, "liquorAgeRestricted for partnumber " + liquorAgeRestricted + " is not matching");
				}
			}
			// validate tobaccoAgeRestricted
			if(tobaccoAgeRestricted == null) {
				testLog.warn("tobaccoAgeRestricted is" + tobaccoAgeRestricted);

			} else {

				if(tobaccoAgeRestricted_SI == null) {
					testLog.error("tobaccoAgeRestricted Actual:" + tobaccoAgeRestricted);
					testLog.error("tobaccoAgeRestricted Expected:" + tobaccoAgeRestricted_SI);
					Assert.assertTrue(false, "tobaccoAgeRestricted for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("tobaccoAgeRestricted Actual:" + tobaccoAgeRestricted);
					testLog.info("tobaccoAgeRestricted Expected:" + tobaccoAgeRestricted_SI);
					Assert.assertEquals(tobaccoAgeRestricted_SI.contains(tobaccoAgeRestricted.toString()), true, "tobaccoAgeRestricted for partnumber " + partnumbervalue + " is not matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC06- Validate the COO attributes of Partnumber")
	public void validateProduct_cooOfPartNumber() {

		productEnrichmentAPI();
		solrAPI();
		// Validating COO parameters values for the added part numbers
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);

			// Validating coo logo boolean value
			if(searchIndexData.getCountryOfOrigin().getCoologorq() == null && apiResponseData.getCooOfPartNumber().isCooLogoRequired() == null) {
				testLog.info("COO logo Actual:" + apiResponseData.getCooOfPartNumber().isCooLogoRequired());
				testLog.info("COO logo Expected:" + searchIndexData.getCountryOfOrigin().getCoologorq());
			} else if(searchIndexData.getCountryOfOrigin().getCoologorq() == null || apiResponseData.getCooOfPartNumber().isCooLogoRequired() == null) {
				testLog.info("COO logo Actual:" + apiResponseData.getCooOfPartNumber().isCooLogoRequired());
				testLog.info("COO logo Expected:" + searchIndexData.getCountryOfOrigin().getCoologorq());
				Assert.assertTrue(false, "COO logo for partnumber " + partnumbervalue + " is not matching");
			} else {
				if(searchIndexData.getCountryOfOrigin().getCoologorq().equalsIgnoreCase("Y")) {
					testLog.info("COO logo Actual:" + apiResponseData.getCooOfPartNumber().isCooLogoRequired());
					testLog.info("COO logo Expected:" + searchIndexData.getCountryOfOrigin().getCoologorq());
					Assert.assertTrue(apiResponseData.getCooOfPartNumber().isCooLogoRequired(), "COO logo for partnumber" + partnumbervalue + " is NOT matching");
				} else if(searchIndexData.getCountryOfOrigin().getCoologorq().equalsIgnoreCase("N")) {
					testLog.info("COO logo Actual:" + apiResponseData.getCooOfPartNumber().isCooLogoRequired());
					testLog.info("Country Of Origin Expected:" + searchIndexData.getCountryOfOrigin().getCoologorq());
					Assert.assertFalse(apiResponseData.getCooOfPartNumber().isCooLogoRequired(), "COO logo for partnumber" + partnumbervalue + " is NOT matching");
				}
			}

			// Validating COO barcode boolean value
			if(searchIndexData.getCountryOfOrigin().getCoobcrq() == null && apiResponseData.getCooOfPartNumber().isCooBarcodeRequired() == null) {
				testLog.info("COO barcode Actual:" + apiResponseData.getCooOfPartNumber().isCooBarcodeRequired());
				testLog.info("COO barcode Expected:" + searchIndexData.getCountryOfOrigin().getCoobcrq());
				Assert.assertTrue(true);
			} else if(searchIndexData.getCountryOfOrigin().getCoobcrq() == null || apiResponseData.getCooOfPartNumber().isCooBarcodeRequired() == null) {
				testLog.info("COO barcode Actual:" + apiResponseData.getCooOfPartNumber().isCooBarcodeRequired());
				testLog.info("COO barcode Expected:" + searchIndexData.getCountryOfOrigin().getCoobcrq());
				Assert.assertTrue(false, "COO barcode for partnumber " + partnumbervalue + " is not matching");
			} else {
				if(searchIndexData.getCountryOfOrigin().getCoobcrq().equalsIgnoreCase("Y")) {
					testLog.info("COO barcode Actual:" + apiResponseData.getCooOfPartNumber().isCooBarcodeRequired());
					testLog.info("COO barcode Expected:" + searchIndexData.getCountryOfOrigin().getCoobcrq());
					Assert.assertTrue(apiResponseData.getCooOfPartNumber().isCooBarcodeRequired(), "COO barcode for partnumber" + partnumbervalue + " is NOT matching");
				} else if(searchIndexData.getCountryOfOrigin().getCoobcrq().equalsIgnoreCase("N")) {
					testLog.info("COO barcode Actual:" + apiResponseData.getCooOfPartNumber().isCooBarcodeRequired());
					testLog.info("COO barcode Expected:" + searchIndexData.getCountryOfOrigin().getCoobcrq());
					Assert.assertFalse(apiResponseData.getCooOfPartNumber().isCooBarcodeRequired(), "COO barcode for partnumber" + partnumbervalue + " is NOT matching");
				}
			}

			// Validating COO description boolean value
			if(searchIndexData.getCountryOfOrigin().getCoostdsind() == null && apiResponseData.getCooOfPartNumber().isCooDescriptionRequired() == null) {
				testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().isCooDescriptionRequired());
				testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostdsind());
				Assert.assertTrue(true);
			} else if(searchIndexData.getCountryOfOrigin().getCoostdsind() == null || apiResponseData.getCooOfPartNumber().isCooDescriptionRequired() == null) {
				testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().isCooDescriptionRequired());
				testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostdsind());
				Assert.assertTrue(false, "COO description for partnumber " + partnumbervalue + " is not matching");
			} else {
				if(searchIndexData.getCountryOfOrigin().getCoostdsind().equalsIgnoreCase("Y")) {
					testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().isCooDescriptionRequired());
					testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostdsind());
					Assert.assertTrue(apiResponseData.getCooOfPartNumber().isCooDescriptionRequired(), "COO description for partnumber" + partnumbervalue + " is NOT matching");
				} else if(searchIndexData.getCountryOfOrigin().getCoostdsind().equalsIgnoreCase("N")) {
					testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().isCooDescriptionRequired());
					testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostdsind());
					Assert.assertFalse(apiResponseData.getCooOfPartNumber().isCooBarcodeRequired(), "COO description for partnumber" + partnumbervalue + " is NOT matching");
				}

				// Validating COO percentage value
				if(searchIndexData.getCountryOfOrigin().getCoopercentage() == null && apiResponseData.getCooOfPartNumber().getCooPercentage() == null) {
					testLog.info("COO percentage Actual:" + apiResponseData.getCooOfPartNumber().getCooPercentage());
					testLog.info("COO percentage Expected:" + searchIndexData.getCountryOfOrigin().getCoopercentage());
				} else if(searchIndexData.getCountryOfOrigin().getCoopercentage() == null || apiResponseData.getCooOfPartNumber().getCooPercentage() == null) {
					testLog.info("COO percentage Actual:" + apiResponseData.getCooOfPartNumber().getCooPercentage());
					testLog.info("COO percentage Expected:" + searchIndexData.getCountryOfOrigin().getCoopercentage());
					Assert.assertTrue(false, "COO percentage for partnumber " + partnumbervalue + " is not matching");
				} else {

					if(apiResponseData.getCooOfPartNumber().getCooPercentage() == 0) {

						if(searchIndexData.getCountryOfOrigin().getCoopercentage() == "N/A" || searchIndexData.getCountryOfOrigin().getCoopercentage() == null) {
							testLog.info("COO percentage Actual:" + apiResponseData.getCooOfPartNumber().getCooPercentage());
							testLog.info("COO percentage Expected:" + searchIndexData.getCountryOfOrigin().getCoopercentage());
						}
					} else {
						testLog.info("COO percentage Actual:" + apiResponseData.getCooOfPartNumber().getCooPercentage());
						testLog.info("COO percentage Expected:" + searchIndexData.getCountryOfOrigin().getCoopercentage());
						Assert.assertEquals(apiResponseData.getCooOfPartNumber().getCooPercentage().toString(), searchIndexData.getCountryOfOrigin().getCoopercentage(), "COO percentage for partnumber " + partnumbervalue + " is not matching");
					}
				}

				// Validating COO description value
				if(searchIndexData.getCountryOfOrigin().getCoostmt() == null && apiResponseData.getCooOfPartNumber().getCooDescription() == null) {
					testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().getCooDescription());
					testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoopercentage());
					Assert.assertTrue(true);
				} else if(searchIndexData.getCountryOfOrigin().getCoostmt() == null || apiResponseData.getCooOfPartNumber().getCooDescription() == null) {
					testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().getCooDescription());
					testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostmt());
					Assert.assertTrue(false, "COO description for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("COO description Actual:" + apiResponseData.getCooOfPartNumber().getCooDescription());
					testLog.info("COO description Expected:" + searchIndexData.getCountryOfOrigin().getCoostmt());
					Assert.assertEquals(apiResponseData.getCooOfPartNumber().getCooDescription(), searchIndexData.getCountryOfOrigin().getCoostmt(), "COO description for partnumber " + partnumbervalue + " is not matching");
				}

				// Validating COO statement value
				if(searchIndexData.getCountryOfOrigin().getCoostmt() == null && apiResponseData.getCooOfPartNumber().getCooStatement() == null) {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooStatement());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoostmt());
					Assert.assertTrue(true);
				} else if(searchIndexData.getCountryOfOrigin().getCoostmt() == null || apiResponseData.getCooOfPartNumber().getCooStatement() == null) {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooStatement());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoostmt());
					Assert.assertTrue(false, "COO statement for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooStatement());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoostmt());
					Assert.assertEquals(apiResponseData.getCooOfPartNumber().getCooStatement(), searchIndexData.getCountryOfOrigin().getCoostmt(), "COO statement for partnumber " + partnumbervalue + " is not matching");
				}

				// Validating COO country name
				if(searchIndexData.getCountryOfOrigin().getCoocountry() == null && apiResponseData.getCooOfPartNumber().getCooCountry() == null) {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooCountry());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoocountry());
					Assert.assertTrue(true);
				} else if(searchIndexData.getCountryOfOrigin().getCoocountry() == null || apiResponseData.getCooOfPartNumber().getCooCountry() == null) {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooCountry());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoocountry());
					Assert.assertTrue(false, "COO statement for partnumber " + partnumbervalue + " is not matching");
				} else {
					testLog.info("COO statement Actual:" + apiResponseData.getCooOfPartNumber().getCooCountry());
					testLog.info("COO statement Expected:" + searchIndexData.getCountryOfOrigin().getCoocountry());
					Assert.assertEquals(apiResponseData.getCooOfPartNumber().getCooCountry(), searchIndexData.getCountryOfOrigin().getCoocountry(), "COO statement for partnumber " + partnumbervalue + " is not matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC07- Validate unit price of Partnumber")
	public void validateProduct_unitPriceDisplayOfPartNumber() {
		productEnrichmentAPI();
		solrAPI();
		// Validating unit price values for the added part numbers
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);

			// Validating unit price of part number
			if(searchIndexData.getUnitPrice() == null && apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay() == null) {
				testLog.info("API unit price Actual:" + apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay());
				testLog.info("SI unit price Expected:" + searchIndexData.getUnitPrice());
				Assert.assertTrue(true);
			} else if(searchIndexData.getUnitPrice() == null || apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay() == null) {
				testLog.info("API unit price Actual:" + apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay());
				testLog.info("SI unit price Expected:" + searchIndexData.getUnitPrice());
				Assert.assertTrue(false, "Unit price for partnumber " + partnumbervalue + " is not matching");
			} else {
				testLog.info("API unit price Actual:" + apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay());
				testLog.info("SI unit price Expected:" + searchIndexData.getUnitPrice());
				Assert.assertEquals(apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay(), searchIndexData.getUnitPrice(), "Unit price for partnumber " + partnumbervalue + " is not matching");
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC08- Validate temporarly unavailable of Partnumber")
	public void validateProduct_temporarilyUnavailable() {
		productEnrichmentAPI();
		solrAPI();
		// Validating temporarily unavailable for the added part numbers
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);

			// Validating temporarily unavailable of part number
			if(searchIndexData.isAvailable() == null && apiResponseData.isTemporarilyUnavailable() == null) {
				testLog.info("API product temporary unavailable Actual:" + apiResponseData.isTemporarilyUnavailable());
				testLog.info("SI product available Expected:" + searchIndexData.isAvailable());
				Assert.assertTrue(true);
			} else if(searchIndexData.isAvailable() == null || apiResponseData.isTemporarilyUnavailable() == null) {
				testLog.info("API product temporary unavailable Actual:" + apiResponseData.isTemporarilyUnavailable());
				testLog.info("SI product available Expected:" + searchIndexData.isAvailable());
				Assert.assertTrue(false, "Temparary unavailable for partnumber " + partnumbervalue + " is not matching");
			} else {
				if(apiResponseData.isTemporarilyUnavailable() == true && searchIndexData.isAvailable() == false) {
					testLog.info("API product temporary unavailable Actual:" + apiResponseData.isTemporarilyUnavailable());
					testLog.info("SI product available Expected:" + searchIndexData.isAvailable());
				} else if(apiResponseData.isTemporarilyUnavailable() == false && searchIndexData.isAvailable() == null) {
					testLog.info("API product temporary unavailable Actual:" + apiResponseData.isTemporarilyUnavailable());
					testLog.info("SI product available Expected:" + searchIndexData.isAvailable());
				} else {
					testLog.info("API product temporary unavailable Actual:" + apiResponseData.isTemporarilyUnavailable());
					testLog.info("SI product available Expected:" + searchIndexData.isAvailable());
					Assert.assertTrue(false, "API and SI temprary unavailable for " + partnumbervalue + " is NOT matching");
				}
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC02_TC01- Verify the status code 200 when PartNumber of a product that is not buyable or is out of range or is a recalled product is sent in the request.")
	public void validateOutOfRangeProduct() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		// Adding header value
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		// Adding query parameter
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		// Executing API with body and getting the response
		Response response = restLibrary.executeAPI();
		// Validating the response, status code
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		funLibrary.Assert.assertAll();
		// To be check with dev team for API blank response.
	}

	@Test(description = "AC03_TC01- Verify the status code 200 when Invalid PartNumbers is sent in the request.")
	public void validateResponseWithInvalidProductNumber() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));
		Response apiResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(apiResponse);
		funLibrary.validateStatusCode(apiResponse, FunLibrary.excelData.get("StatusCode"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type- S,S10,S15,S20,S25,S30 and offer details", dataProvider = "specialTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Special(String testName) {
		productEnrichmentAPI();
		solrAPI(); // validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Down Down for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String tctType_SI = searchIndexData.getTickettype();
			String ticketType_SI = tctType_SI.substring(0, tctType_SI.length() - 2);
			DecimalFormat format = new DecimalFormat("0.00");
			String listPrice_SI = searchIndexData.getPrice().getListPrice();
			String offerPrice_SI = searchIndexData.getPrice().getOfferPrice();
			double discount_Price = Double.parseDouble(listPrice_SI) - Double.parseDouble(offerPrice_SI);
			String secondShout_SI = "save $" + format.format(discount_Price);
			List<String> hideColesPromotionIconFlag_SI = searchIndexData.getAttributesMap().gethIDECOLESPROMOTIONICONFLAG();
			List<String> hideProductSavingsFlag_SI = searchIndexData.getAttributesMap().gethIDEPRODUCTSAVINGSFLAG();

			// Getting ticketType, secondShout, graphicCode and onlinePromo from API
			// Response
			Boolean hideProductSavingsFlag_API = apiResponseData.getAttributesOfPartNumber().ishideProductSavingsFlag();
			Boolean hideColesPromotionIconFlag_API = apiResponseData.getAttributesOfPartNumber().ishideColesPromotionIconFlag();
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String offerPrice_API = apiResponseData.getPricesOfPartNumber().getOfferPrice().toString();
			String listPrice_API = apiResponseData.getPricesOfPartNumber().getListPrice().toString();
			String secondShout_API = apiResponseData.getTicketTypeOfPartNumber().get_2ndShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating offer price
			funLibrary.validate_Equals("OfferPrice", offerPrice_SI, offerPrice_API);
			// Validating list price
			funLibrary.validate_Equals("ListPrice", listPrice_SI, listPrice_API);
			// Validating second shout
			funLibrary.validate_Equals("_2ndShout", secondShout_SI, secondShout_API);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("HideColesPromotionIconFlag", hideColesPromotionIconFlag_SI, hideColesPromotionIconFlag_API.toString());
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("Hide_ProductSavingsFlag", hideProductSavingsFlag_SI, hideProductSavingsFlag_API.toString());
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 40% off")
	public void validateTicketType_Special40() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 50% off")
	public void validateTicketType_Special50() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type- S,S10,S15,S20,S25,S30 and offer details", dataProvider = "specialOnlineTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Online_Special(String testName) {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Online Special for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String tctType_SI = searchIndexData.getTickettype();
			String ticketType_SI = tctType_SI.substring(0, tctType_SI.length() - 2);
			testLog.info("Ticket type :: " + ticketType_SI);

			DecimalFormat format = new DecimalFormat("0.00");
			String listPrice_SI = searchIndexData.getPrice().getListPrice();
			String offerPrice_SI = searchIndexData.getPrice().getOfferPrice();
			double discount_Price = Double.parseDouble(listPrice_SI) - Double.parseDouble(offerPrice_SI);
			String secondShout_SI = "save $" + format.format(discount_Price);

			// Getting ticketType, secondShout, graphicCode and onlinePromo from API
			// Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String offerPrice_API = apiResponseData.getPricesOfPartNumber().getOfferPrice().toString();
			String listPrice_API = apiResponseData.getPricesOfPartNumber().getListPrice().toString();
			String secondShout_API = apiResponseData.getTicketTypeOfPartNumber().get_2ndShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating offer price
			funLibrary.validate_Equals("OfferPrice", offerPrice_SI, offerPrice_API);
			// Validating list price
			funLibrary.validate_Equals("ListPrice", listPrice_SI, listPrice_API);
			// Validating second shout
			funLibrary.validate_Equals("_2ndShout", secondShout_SI, secondShout_API);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 40% off")
	public void validateTicketType_Online_Special40() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - S40 for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 50% off")
	public void validateTicketType_Online_Special50() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - S50 for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Down Down' Ticket Type and offer details")
	public void validateTicketType_Down_Down() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Down Down for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String tctType_SI = searchIndexData.getTickettype();
			String ticketType_SI = tctType_SI.substring(0, tctType_SI.length() - 2);
			String listPrice_SI = searchIndexData.getPrice().getListPrice();
			String downDate_SI = searchIndexData.getDownDate();
			DecimalFormat format = new DecimalFormat("0.00");
			
			String formatted_listPrice = format.format(Double.parseDouble(listPrice_SI));
			String _2ndShout_SI = "Was $" + formatted_listPrice + " on " + downDate_SI;
			List<String> hideColesPromotionIconFlag_SI = searchIndexData.getAttributesMap().gethIDECOLESPROMOTIONICONFLAG();
			List<String> hideProductSavingsFlag_SI = searchIndexData.getAttributesMap().gethIDEPRODUCTSAVINGSFLAG();

			// Getting ticketType, secondShout, graphicCode and onlinePromo from API
			// Response
			Boolean hideProductSavingsFlag_API = apiResponseData.getAttributesOfPartNumber().ishideProductSavingsFlag();
			Boolean hideColesPromotionIconFlag_API = apiResponseData.getAttributesOfPartNumber().ishideColesPromotionIconFlag();
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _2ndShout_API = apiResponseData.getTicketTypeOfPartNumber().get_2ndShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating second shout
			funLibrary.validate_Equals("_2ndShout", _2ndShout_API, _2ndShout_SI);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("HideColesPromotionIconFlag", hideColesPromotionIconFlag_SI, hideColesPromotionIconFlag_API.toString());
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("Hide_ProductSavingsFlag", hideProductSavingsFlag_SI, hideProductSavingsFlag_API.toString());
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'New' Ticket Type and details")
	public void validateTicketType_New() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// validating ticket type
			if(ticketType_API.equals("N") && ticketType_SI.equals("N")) {
				testLog.info("TicketType API Response Actual:" + ticketType_API);
				testLog.info("TicketType SOLR Response Expected:" + ticketType_SI);
			} else {
				testLog.error("TicketType API Response Actual:" + ticketType_API);
				testLog.error("TicketType SOLR Response Actual:" + ticketType_SI);
				testLog.error("TicketType Expected: N");
				Assert.assertEquals(ticketType, ticketType_SI, "Ticket type should be N");
			}
			// Validating grahicCode
			if(graphicCode.equals(FunLibrary.excelData.get("TicketType") + "_" + onlinePromo)) {
				testLog.info("GraphicCode API Response Actual:" + graphicCode);
				testLog.info("GraphicCode API Response Expected:" + FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			} else {
				testLog.error("GraphicCode API Response Actual:" + ticketType);
				testLog.error("GraphicCode API Response Expected: " + FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
				Assert.assertEquals(graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Dollar Dazzler' Ticket Type and details")
	public void validateTicketType_Dollar_Dazzler() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - Dollar Dazzler for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticketType from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType and graphicCode from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// validating ticket type
			if(ticketType_API.equals("D") && ticketType_SI.equals("D")) {
				testLog.info("TicketType API Response Actual:" + ticketType_API);
				testLog.info("TicketType SOLR Response Expected:" + ticketType_SI);
			} else {
				testLog.error("TicketType API Response Actual:" + ticketType_API);
				testLog.error("TicketType SOLR Response Actual:" + ticketType_SI);
				testLog.error("TicketType Expected: D");
				Assert.assertEquals(ticketType, ticketType_SI, "Ticket type should be D");
			}
			// Validating grahicCode
			if(graphicCode.equals(FunLibrary.excelData.get("TicketType") + "_" + onlinePromo)) {
				testLog.info("GraphicCode API Response Actual:" + graphicCode);
				testLog.info("GraphicCode API Response Expected:" + FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			} else {
				testLog.error("GraphicCode API Response Actual:" + ticketType);
				testLog.error("GraphicCode API Response Expected: " + FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
				Assert.assertEquals(graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			}
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type EveryDay.")
	public void validateTicketType_EveryDay() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - EveryDay for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting PricesOfPartNumber
			// PricesOfPartNumber from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();

			// AttributesofPartNumber from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// validating ticket type
			funLibrary.validate_Equals("TicketType", "V", ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", "V_" + onlinePromo, graphicCode);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type FlyBuy.", dataProvider = "flyBuyTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Flybuy(String testName) {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - FlyBuy for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();

			// TicketType from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Equals("1stShout", FunLibrary.excelData.get("1stShout"), _1stShout);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type FlyBuy.", dataProvider = "flyBuyOnlineTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Online_Flybuy(String testName) {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - FlyBuy for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();

			// TicketType from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Equals("1stShout", FunLibrary.excelData.get("1stShout"), _1stShout);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type MultiSaveSingleSKU.")
	public void validateTicketType_MultiSaveSingleSKU() {

		productEnrichmentAPI();
		solrAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - Mulit Save Single SKU for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			String promoMinQty_SI = searchIndexData.getPromo_min_qty();
			String promoReward_SI = searchIndexData.getPromo_reward();
			promoReward_SI = promoReward_SI.replaceAll("[.0]", "");
			// TicketType from API Response
			String ticketType = null, graphicCode = null, _1stShout = null;
			int onlinePromo = 0;
			if(apiResponseData.getTicketTypeOfPartNumber() != null) {
				ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
				graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
				_1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
				onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			}
			// multiBuyPromoOfPartNumber from API response
			String promoType = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoType();
			String promoDesc = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoDesc();
			String promoId = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoId();
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Equals("1stShout", promoDesc_SI.toLowerCase(), _1stShout);
			// validating promoType
			funLibrary.validate_Equals("PromoType", promoType_SI, promoType);
			// validating promoDesc
			funLibrary.validate_Equals("PromoDesc", promoDesc_SI, promoDesc);
			// validating promoId
			funLibrary.validate_Equals("PromoId", promoId_SI, promoId);
			// validating promoMinQty
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty.toString());
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", Integer.parseInt(promoReward_SI), promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type MultiSaveMultiSKU.")
	public void validateTicketType_MultiSaveMultiSKU() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - Mulit Save Single SKU for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			String promoMinQty_SI = searchIndexData.getPromo_min_qty();
			String promoReward_SI = searchIndexData.getPromo_reward();
			promoReward_SI = promoReward_SI.replaceAll("[.0]", "");
			// TicketType from API Response
			String ticketType = null, graphicCode = null, _1stShout = null;
			int onlinePromo = 0;
			if(apiResponseData.getTicketTypeOfPartNumber() != null) {
				ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
				graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
				_1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
				onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			}
			// multiBuyPromoOfPartNumber from API response
			String promoType = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoType();
			String promoDesc = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoDesc();
			String promoId = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoId();
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Contains("1stShout", "Any " + promoMinQty + " $" + promoDesc.substring(promoDesc.lastIndexOf("$") + 1), _1stShout);
			// validating promoType
			funLibrary.validate_Equals("PromoType", promoType_SI, promoType);
			// validating promoDesc
			funLibrary.validate_Equals("PromoDesc", promoDesc_SI, promoDesc);
			// validating promoId
			funLibrary.validate_Equals("PromoId", promoId_SI, promoId);
			// validating promoMinQty
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty.toString());
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", Integer.parseInt(promoReward_SI), promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type MultiSaveSingleSKU.")
	public void validateTicketType_Online_MultiSaveSingleSKU() {

		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - Mulit Save Single SKU for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			String promoMinQty_SI = searchIndexData.getPromo_min_qty();
			String promoReward_SI = searchIndexData.getPromo_reward();
			promoReward_SI = promoReward_SI.replaceAll("[.0]", "");
			// TicketType from API Response
			String ticketType = null, graphicCode = null, _1stShout = null;
			int onlinePromo = 0;
			if(apiResponseData.getTicketTypeOfPartNumber() != null) {
				ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
				graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
				_1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
				onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			}
			// multiBuyPromoOfPartNumber from API response
			String promoType = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoType();
			String promoDesc = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoDesc();
			String promoId = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoId();
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Equals("1stShout", promoDesc_SI.toLowerCase(), _1stShout);
			// validating promoType
			funLibrary.validate_Equals("PromoType", promoType_SI, promoType);
			// validating promoDesc
			funLibrary.validate_Equals("PromoDesc", promoDesc_SI, promoDesc);
			// validating promoId
			funLibrary.validate_Equals("PromoId", promoId_SI, promoId);
			// validating promoMinQty
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty.toString());
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", Integer.parseInt(promoReward_SI), promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type MultiSaveMultiSKU.")
	public void validateTicketType_Online_MultiSaveMultiSKU() {
		productEnrichmentAPI();
		solrAPI();
		;

		// validate record count
		funLibrary.validate_Equals("Record count", apiRoot.getTotalCount(), solrRoot.getRecordSetCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating Ticket Type - Mulit Save Single SKU for PartNumber: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			String promoMinQty_SI = searchIndexData.getPromo_min_qty();
			String promoReward_SI = searchIndexData.getPromo_reward();
			promoReward_SI = promoReward_SI.replaceAll("[.0]", "");
			// TicketType from API Response
			String ticketType = null, graphicCode = null, _1stShout = null;
			int onlinePromo = 0;
			if(apiResponseData.getTicketTypeOfPartNumber() != null) {
				ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
				graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
				_1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
				onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			}
			// multiBuyPromoOfPartNumber from API response
			String promoType = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoType();
			String promoDesc = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoDesc();
			String promoId = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoId();
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			// validating ticket type
			funLibrary.validate_Equals("TicketType", FunLibrary.excelData.get("TicketType"), ticketType);
			// validating graphicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
			// validating 1st Shout
			funLibrary.validate_Contains("1stShout", "Any " + promoMinQty + " $" + promoDesc.substring(promoDesc.lastIndexOf("$") + 1), _1stShout);
			// validating promoType
			funLibrary.validate_Equals("PromoType", promoType_SI, promoType);
			// validating promoDesc
			funLibrary.validate_Equals("PromoDesc", promoDesc_SI, promoDesc);
			// validating promoId
			funLibrary.validate_Equals("PromoId", promoId_SI, promoId);
			// validating promoMinQty
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty.toString());
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", Integer.parseInt(promoReward_SI), promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'BigPackValue' Ticket Type and details")
	public void validateTicketType_BigPackValue() {
		productEnrichmentAPI();
		solrAPI();
		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Down Down for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);

		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'WhileStockLast' Ticket Type and details")
	public void validateTicketType_WhileStockLast() {
		productEnrichmentAPI();
		solrAPI();

		// validate record count
		Assert.assertEquals(solrRoot.getRecordSetCount(), apiRoot.getTotalCount());

		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Down Down for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = apiRoot.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = solrRoot.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);

			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();

			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);

		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "validate schema of response json")
	public void validateSchema_ProductEnrichment() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"), FunLibrary.excelData.get("QueryParameterValue1"));

		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"), FunLibrary.excelData.get("QueryParameterValue2"));

		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateJsonStructure(response, "productenrichment");
	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
