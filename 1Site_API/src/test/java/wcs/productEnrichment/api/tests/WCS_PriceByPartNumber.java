package wcs.productEnrichment.api.tests;

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

public class WCS_PriceByPartNumber extends Base_Class_API implements ITest {
	RestLibrary restLibrary = null;
	Response apiResponse = null;
	Response searchIndexResponse = null;
	Root root = null;
	SearchIndexRoot indexResponse = null;
	String[] partnumber = null;

	public void execAPI() {
		String searchIndexPartnumber = "";
		restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.setUrlEncoding(false);
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
		apiResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(apiResponse);
		funLibrary.validateStatusCode(apiResponse, "200");
		funLibrary.validate_NotEquals("TotalCount", "0", funLibrary.getJsonPathValue(apiResponse, "totalCount"));
		funLibrary.Assert.assertAll("No data found for given partnumbers : "+ FunLibrary.excelData.get("QueryParameterValue"));
		partnumber = FunLibrary.excelData.get("QueryParameterValue").split(",");
		for (int i = 0; i < partnumber.length; ++i) {
			String part = "&partNumber=" + partnumber[i] + "P";
			searchIndexPartnumber = searchIndexPartnumber + part;
		}
		String searchIndexBaseURI = FunLibrary.excelData.get("SolarAPI") + searchIndexPartnumber;
		restLibrary = new RestLibrary(Base_Class_API.BaseURI, searchIndexBaseURI, FunLibrary.excelData.get("RequestType"));
		searchIndexResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(searchIndexResponse);
		root = (Root) restLibrary.getResponseBody(apiResponse, Root.class);
		indexResponse = (SearchIndexRoot) restLibrary.getResponseBody(searchIndexResponse, SearchIndexRoot.class);
	}

	@Test(dataProvider = "genericTestData", dataProviderClass = API_DataProvider.class)
	public void validateGenericErrorCodes(String testname) {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		if(FunLibrary.excelData.get("QueryParameterKey") != "") {
			restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		}
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));

		// ValidatableResponse
		switch (FunLibrary.excelData.get("StatusCode")) {
		case "400":
		case "401":
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorCode", FunLibrary.excelData.get("ErrorCode"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorMessage", FunLibrary.excelData.get("ErrorMessage"));
			funLibrary.validateJSONPathValue_Equals(response, "errors[0].errorLevel", FunLibrary.excelData.get("ErrorLevel/Priority"));
			break;
		case "404":
			funLibrary.validateJSONPathValue_Contains(response, "description", FunLibrary.excelData.get("Description"));
			break;
		case "500":
			funLibrary.validateJSONPathValue_Equals(response, "'Error message'", FunLibrary.excelData.get("ErrorMessage"));
			break;
		default:
			testLog.info("No validation found for this test. Please implement validations for " + FunLibrary.excelData.get("StatusCode"));
			break;
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC01- Verify the attributes of Partnumber")
	public void validateAttributesOfPartNumber() {
		int i = 0;
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());

		for (i = 0; i < partnumber.length; ++i) {

			String partnumbervalue = partnumber[i];

			testLog.info("-----------Validating AttributesOfPartNumber for: " + partnumbervalue + " -----------");

			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting AttributesOfPartNumber
			// AttributesofPartNumber from Search Index Response
			List<String> barcodeId_SI = searchIndexData.getAttributesMap().getbARCODEID();
			List<String> excludeFromSubstitutionFlag_SI = searchIndexData.getAttributesMap().geteXCLUDEFROMSUBSTITUTIONFLAG();
			List<String> weightedItemIndicator_SI = searchIndexData.getAttributesMap().getwEIGHTEDITEMINDICATOR();
			List<String> weightedByEach_SI = searchIndexData.getAttributesMap().getwEIGHTEDBYEACH();
			List<String> deliveryRestriction_SI = searchIndexData.getAttributesMap().getdELIVERYRESTRICTIONS();
			// AttributesofPartNumber from API Response
			List<String> barcodeId = apiResponseData.getAttributesOfPartNumber().getBarcodeID();
			List<String> deliveryRestriction = apiResponseData.getAttributesOfPartNumber().getDeliveryRestrictions();
			String excludeFromSubstitutionFlag = (apiResponseData.getAttributesOfPartNumber().isExcludedFromSubstitution() == null ? null : apiResponseData.getAttributesOfPartNumber().isExcludedFromSubstitution().toString());
			String weightedItemIndicatorFlag = (apiResponseData.getAttributesOfPartNumber().isWeightedItemIndicator() == null ? null : apiResponseData.getAttributesOfPartNumber().isWeightedItemIndicator().toString());
			String weightedByEachFlag = (apiResponseData.getAttributesOfPartNumber().isWeigthedByEach() == null ? null : apiResponseData.getAttributesOfPartNumber().isWeigthedByEach().toString());
			
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			funLibrary.validate_Equals("BarCodeID", barcodeId_SI, barcodeId);
			funLibrary.validate_Equals("DeliveryRestriction", deliveryRestriction_SI, deliveryRestriction);
			funLibrary.validate_Contains("ExcludeFromSubstitutionFlag", excludeFromSubstitutionFlag_SI, excludeFromSubstitutionFlag);
			funLibrary.validate_Contains("WeightedItemIndicatorFlag", weightedItemIndicator_SI, weightedItemIndicatorFlag);
			funLibrary.validate_Contains("WeightedByEachFlag", weightedByEach_SI, weightedByEachFlag);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC02- Verify the Partnumber")
	public void validatePartNumber() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating PartNumber for: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// PartNumber from Search Index Response
			String partNumber_SI = searchIndexData.getPartNumber();
			// PartNumber from API Response
			String partNumber = apiResponseData.getPartNumber() + "P";
			// validate partNumber
			funLibrary.validate_Equals("PartNumber", partNumber_SI, partNumber);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC03- Verify the Prices of Partnumber")
	public void validatePricesOfPartNumber() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Price of PartNumber for: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// PricesOfPartNumber from Search Index Response
			String offerPrice_SI = searchIndexData.getPrice().getOfferPrice();
			String listPrice_SI = searchIndexData.getPrice().getListPrice();
			// AttributesofPartNumber from API Response
			String offerPrice = (apiResponseData.getPricesOfPartNumber().getOfferPrice() == null ? null : apiResponseData.getPricesOfPartNumber().getOfferPrice().toString());
			String listPrice = (apiResponseData.getPricesOfPartNumber().getListPrice() == null ? null : apiResponseData.getPricesOfPartNumber().getListPrice().toString());
			
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// validate offerPrice
			funLibrary.validate_Equals("OfferPrice", offerPrice_SI, offerPrice);
			// validate listPrice
			funLibrary.validate_Equals("ListPrice", listPrice_SI, listPrice);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC04- Verify the Redemption of Partnumber")
	public void validateRedemptionsOfPartNumber() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Redemptions of PartNumber for: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// RedemptionsOfPartNumber from Search Index Response
			List<String> redemptionLimitOfRetail_SI = searchIndexData.getAttributesMap().getrETAILLIMIT();
			List<String> redemptionLimitOfPromo_SI = searchIndexData.getAttributesMap().getpROMOTIONALLIMIT();
			// RedemptionsOfPartNumber from API Response
			String redemptionLimitOfRetail = (apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfRetail() == null ? null : apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfRetail().toString());
			String redemptionLimitOfPromo = (apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfPromo() == null ? null : apiResponseData.getRedemptionsOfPartNumber().getRedemptionLimitOfPromo().toString());
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// validate redemptionLimitOfRetail
			funLibrary.validate_Contains("RedemptionLimitOfRetail", redemptionLimitOfRetail_SI, redemptionLimitOfRetail);
			// validate redemptionLimitOfPromo
			funLibrary.validate_Contains("RedemptionLimitOfPromo", redemptionLimitOfPromo_SI, redemptionLimitOfPromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC05- Verify the Restriction of Partnumber")
	public void validateRestrictionOfPartNumber() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Restriction of PartNumber for: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting RestrictionofPartNumber
			// RestrictionofPartNumber from Search Index Response
			List<String> liquorAgeRestricted_SI = searchIndexData.getAttributesMap().getlIQUORAGERESTRICTIONFLAG();
			List<String> tobaccoAgeRestricted_SI = searchIndexData.getAttributesMap().gettOBACCOAGERESTRICTIONFLAG();
			// RestrictionofPartNumber from API Response
			String liquorAgeRestricted = (apiResponseData.getRestrictionOfPartNumber() == null ? null : apiResponseData.getRestrictionOfPartNumber().isLiquorAgeRestricted().toString());
			String tobaccoAgeRestricted = (apiResponseData.getRestrictionOfPartNumber() == null ? null : apiResponseData.getRestrictionOfPartNumber().isTobaccoAgeRestricted().toString());
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			funLibrary.validate_Contains("liquorAgeRestricted", liquorAgeRestricted_SI, liquorAgeRestricted);
			funLibrary.validate_Contains("tobaccoAgeRestricted", tobaccoAgeRestricted_SI, tobaccoAgeRestricted);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC06- Validate the COO attributes of Partnumber")
	public void validateProduct_cooOfPartNumber() {
		execAPI();
		// Validating COO parameters values for the added part numbers
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// COO attributes from API response
			String cooLogoRequiredFlag = (apiResponseData.getCooOfPartNumber().isCooLogoRequired() == true ? "Y" : "N");
			String cooBarcodeFlag = (apiResponseData.getCooOfPartNumber().isCooBarcodeRequired() == true ? "Y" : "N");
			String cooDescriptionFlag = (apiResponseData.getCooOfPartNumber().isCooDescriptionRequired() == true ? "Y" : "N");
			Integer int_coopercentage = apiResponseData.getCooOfPartNumber().getCooPercentage();
			String cooPercentage = (int_coopercentage == null || int_coopercentage == 0) ? null : int_coopercentage.toString();
			String cooDescription = apiResponseData.getCooOfPartNumber().getCooDescription();
			String cooStatement = apiResponseData.getCooOfPartNumber().getCooStatement();
			String cooCountryName = apiResponseData.getCooOfPartNumber().getCooCountry();
			// COO attributes from SOLR response
			String cooLogoRequiredFlag_SI = searchIndexData.getCountryOfOrigin().getCoologorq();
			String cooBarcodeFlag_SI = searchIndexData.getCountryOfOrigin().getCoobcrq();
			String cooDescriptionFlag_SI = searchIndexData.getCountryOfOrigin().getCoostdsind();
			String cooPercentage_SI = searchIndexData.getCountryOfOrigin().getCoopercentage().equals("N/A") ? null : searchIndexData.getCountryOfOrigin().getCoopercentage();
			String cooDescription_SI = searchIndexData.getCountryOfOrigin().getCoostmt();
			String cooStatement_SI = searchIndexData.getCountryOfOrigin().getCoostmt();
			String cooCountryName_SI = searchIndexData.getCountryOfOrigin().getCoocountry();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			funLibrary.validate_Equals("CooLogoRequiredFlag", cooLogoRequiredFlag_SI, cooLogoRequiredFlag);
			funLibrary.validate_Equals("CooBarcodeFlag", cooBarcodeFlag_SI, cooBarcodeFlag);
			funLibrary.validate_Equals("CooDescriptionFlag", cooDescriptionFlag_SI, cooDescriptionFlag);
			funLibrary.validate_Equals("CooPercentage", cooPercentage_SI, cooPercentage);
			funLibrary.validate_Equals("CooDescription", cooDescription_SI, cooDescription);
			funLibrary.validate_Equals("CooStatement", cooStatement_SI, cooStatement);
			funLibrary.validate_Equals("CooCountryName", cooCountryName_SI, cooCountryName);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC07- Validate unit price of Partnumber")
	public void validateProduct_unitPriceDisplayOfPartNumber() {
		execAPI();
		// Validating unit price values for the added part numbers
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			funLibrary.validate_Contains("UnitPrice", searchIndexData.getUnitPrice(), apiResponseData.getPriceDisplayOfPartNumber().getUnitPriceDisplay());
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC01_TC08- Validate temporarly unavailable of Partnumber")
	public void validateProduct_temporarilyUnavailable() {
		execAPI();
		// Validating temporarily unavailable for the added part numbers
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("--------Validating Country Of Origin for: " + partnumbervalue + " ---------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			Boolean tempUnavailableFlag = apiResponseData.isTemporarilyUnavailable();
			Boolean tempUnavailableFlag_SI = (searchIndexData.isAvailable() == null ? false : true);
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			funLibrary.validate_Equals("isAvaiableFlag", tempUnavailableFlag_SI, tempUnavailableFlag);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "AC02_TC01- Verify the status code 200 when PartNumber of a product that is not buyable or is out of range or is a recalled product is sent in the request.")
	public void validateOutOfRangeProduct() {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"), FunLibrary.excelData.get("RequestType"));
		restLibrary.setUrlEncoding(false);
		// Adding header to API request
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		// Adding query parameter to API request
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		// Adding path parameter to API request
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
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
		restLibrary.setUrlEncoding(false);
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey"), FunLibrary.excelData.get("QueryParameterValue"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("PathParameterKey1"), FunLibrary.excelData.get("PathParameterValue1"));
		Response apiResponse = restLibrary.executeAPI();
		restLibrary.getResponseBody(apiResponse);
		funLibrary.validateStatusCode(apiResponse, FunLibrary.excelData.get("StatusCode"));
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type- S,S10,S15,S20,S25,S30 and offer details", dataProvider = "specialTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Special(String testName) {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Special for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
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
			String hideProductSavingsFlag_API = (apiResponseData.getAttributesOfPartNumber().ishideProductSavingsFlag() == null ? null : apiResponseData.getAttributesOfPartNumber().ishideProductSavingsFlag().toString());
			String hideColesPromotionIconFlag_API = (apiResponseData.getAttributesOfPartNumber().ishideColesPromotionIconFlag() == null ? null : apiResponseData.getAttributesOfPartNumber().ishideColesPromotionIconFlag().toString());
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String offerPrice_API = apiResponseData.getPricesOfPartNumber().getOfferPrice().toString();
			String listPrice_API = apiResponseData.getPricesOfPartNumber().getListPrice().toString();
			String secondShout_API = apiResponseData.getTicketTypeOfPartNumber().get_2ndShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating offer price
			funLibrary.validate_Equals("OfferPrice", offerPrice_API, offerPrice_SI);
			// Validating list price
			funLibrary.validate_Equals("ListPrice", listPrice_API, listPrice_SI);
			// Validating second shout
			funLibrary.validate_Equals("_2ndShout", secondShout_API, secondShout_SI);
			// Validating graphic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("HideColesPromotionIconFlag", hideColesPromotionIconFlag_SI, hideColesPromotionIconFlag_API);
			// Validating hideProductSavingsFlag boolean value
			funLibrary.validate_Contains("Hide_ProductSavingsFlag", hideProductSavingsFlag_SI, hideProductSavingsFlag_API);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 40% off")
	public void validateTicketType_Special40() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - S40 for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating graphic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type and offer details for 50% off")
	public void validateTicketType_Special50() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - S50 for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating graphic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Special' Ticket Type- S,S10,S15,S20,S25,S30 and offer details", dataProvider = "specialOnlineTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Online_Special(String testName) {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Online Special for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
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
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating offer price
			funLibrary.validate_Equals("OfferPrice", offerPrice_API, offerPrice_SI);
			// Validating list price
			funLibrary.validate_Equals("ListPrice", listPrice_API, listPrice_SI);
			// Validating second shout
			funLibrary.validate_Equals("_2ndShout", secondShout_API, secondShout_SI);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Online Special' Ticket Type and offer details for 40% off")
	public void validateTicketType_Online_Special40() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - S40 for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating graphic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Online Special' Ticket Type and offer details for 50% off")
	public void validateTicketType_Online_Special50() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - S50 for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating first shout
			funLibrary.validate_Equals("_1stShout", _1stShout, FunLibrary.excelData.get("1stShout"));
			// Validating graphic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Down Down' Ticket Type and offer details")
	public void validateTicketType_Down_Down() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Down Down for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
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
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// validating ticket type
			funLibrary.validate_Equals("TicketType", "N", ticketType_API);
			funLibrary.validate_Equals("TicketType", ticketType_SI, ticketType_API);
			// Validating grahicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'Dollar Dazzler' Ticket Type and details")
	public void validateTicketType_Dollar_Dazzler() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Dollar Dazzler for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticketType from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType and graphicCode from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// validating ticket type
			funLibrary.validate_Equals("TicketType", "D", ticketType_API);
			funLibrary.validate_Equals("TicketType", ticketType_SI, ticketType_API);
			// Validating grahicCode
			funLibrary.validate_Equals("GraphicCode", FunLibrary.excelData.get("TicketType") + "_" + onlinePromo, graphicCode);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type EveryDay.")
	public void validateTicketType_EveryDay() {
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - EveryDay for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting PricesOfPartNumber
			// PricesOfPartNumber from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// AttributesofPartNumber from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - FlyBuy for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// TicketType from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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

	@Test(description = "Verify the Ticket Type Online FlyBuy.", dataProvider = "flyBuyOnlineTestData", dataProviderClass = API_DataProvider.class)
	public void validateTicketType_Online_Flybuy(String testName) {
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Online FlyBuy for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// TicketType from API Response
			String ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			String _1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Mulit Save Single SKU for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			Integer promoMinQty_SI = searchIndexData.getPromo_min_qty() == null ? null : Integer.parseInt(searchIndexData.getPromo_min_qty());
			Integer promoReward_SI = searchIndexData.getPromo_reward() == null ? null : Integer.parseInt(searchIndexData.getPromo_reward().replaceAll("[.0]", ""));
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
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty);
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", promoReward_SI, promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type MultiSaveMultiSKU.")
	public void validateTicketType_MultiSaveMultiSKU() {
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Mulit Save Multi SKU for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			Integer promoMinQty_SI = searchIndexData == null ? null : Integer.parseInt(searchIndexData.getPromo_min_qty());
			Integer promoReward_SI = searchIndexData == null ? null : Integer.parseInt(searchIndexData.getPromo_reward().replaceAll("[.0]", ""));
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
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty);
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", promoReward_SI, promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type Online MultiSaveSingleSKU.")
	public void validateTicketType_Online_MultiSaveSingleSKU() {
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Online Multi Save Single SKU for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			Integer promoMinQty_SI = searchIndexData.getPromo_min_qty() == null ? null : Integer.parseInt(searchIndexData.getPromo_min_qty());
			Integer promoReward_SI = searchIndexData.getPromo_reward() == null ? null : Integer.parseInt(searchIndexData.getPromo_reward().replaceAll("[.0]", ""));
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
			Integer promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
			Integer promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();

			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty);
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", promoReward_SI, promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the Ticket Type Online MultiSaveMultiSKU.")
	public void validateTicketType_Online_MultiSaveMultiSKU() {
		execAPI();
		// validate record count
		funLibrary.validate_Equals("Record count", root.getTotalCount(), indexResponse.getRecordSetCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - Online Multi Save Multi SKU for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting Ticket Type
			// TicketType from Search Index Response
			String ticketType_SI = searchIndexData.getTickettype();
			// multiBuyPromoOfPartNumber from search index response
			String promoType_SI = searchIndexData.getPromo_type();
			String promoDesc_SI = searchIndexData.getPromo_desc();
			String promoId_SI = searchIndexData.getPromo_id();
			Integer promoMinQty_SI = searchIndexData.getPromo_min_qty() == null ? null : Integer.parseInt(searchIndexData.getPromo_min_qty());
			Integer promoReward_SI = searchIndexData.getPromo_reward() == null ? null : Integer.parseInt(searchIndexData.getPromo_reward().replaceAll("[.0]", ""));
			
			if(promoMinQty_SI == null) {
				testLog.error("Invalid Partnumber for ticekt typw MultiSaveMultiSKU. Promo min qty is not available for the partnumber"+ apiResponseData.getPartNumber());
				funLibrary.assertCheck("Invalid Partnumber for ticekt typw MultiSaveMultiSKU. Promo min qty is not available for the partnumber",apiResponseData.getPartNumber());
				funLibrary.Assert.assertAll();
			}
			
			// TicketType from API Response
			String ticketType = null, graphicCode = null, _1stShout = null;
			int onlinePromo = 0;
			if(apiResponseData.getTicketTypeOfPartNumber() != null) {
				ticketType = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
				graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
				_1stShout = apiResponseData.getTicketTypeOfPartNumber().get_1stShout();
				onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			}else {
				testLog.error("Attribute ticketTypeOfPartNumber is not avilable in response for tis partnumber:" + apiResponseData.getPartNumber());
			}
			// multiBuyPromoOfPartNumber from API response
			String promoType = null, promoDesc = null, promoId = null;
			int promoMinQty = 0, promoReward=0;
			if(apiResponseData.getMultiBuyPromoOfPartNumber() != null) {
				promoType = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoType();
				 promoDesc = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoDesc();
				 promoId = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoId();
				 promoMinQty = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoMinQty();
				 promoReward = apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward() == null ? null : apiResponseData.getMultiBuyPromoOfPartNumber().getPromoReward();
			}
			 
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
			funLibrary.validate_Equals("PromoMinQty", promoMinQty_SI, promoMinQty);
			// validating promoReward
			funLibrary.validate_Equals("PromoReward", promoReward_SI, promoReward);
			// validating Search Index TicketType
			funLibrary.validate_Equals("TicketType SearchIndex", graphicCode, ticketType_SI);
		}
		funLibrary.Assert.assertAll();
	}

	@Test(description = "Verify the 'BigPackValue' Ticket Type and details")
	public void validateTicketType_BigPackValue() {
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		int i = 0;
		for (i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
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
		execAPI();
		// validate record count
		Assert.assertEquals(indexResponse.getRecordSetCount(), root.getTotalCount());
		for (int i = 0; i < partnumber.length; ++i) {
			String partnumbervalue = partnumber[i];
			testLog.info("-----------Validating Ticket Type - New for PartNumber: " + partnumbervalue + " -----------");
			PartNumber apiResponseData = root.getPartNumbers().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue)).findFirst().orElse(null);
			CatalogEntryView searchIndexData = indexResponse.getCatalogEntryView().stream().filter(x -> x.getPartNumber().equalsIgnoreCase(partnumbervalue + "P")).findFirst().orElse(null);
			// Getting ticket type from Search Index Response
			String ticketType = searchIndexData.getTickettype();
			String ticketType_SI = ticketType.substring(0, ticketType.length() - 2);
			// Getting ticketType, graphicCode and onlinePromo from API Response
			String ticketType_API = apiResponseData.getTicketTypeOfPartNumber().getTicketType();
			String graphicCode = apiResponseData.getTicketTypeOfPartNumber().getGraphicCode();
			int onlinePromo = apiResponseData.getTicketTypeOfPartNumber().getOnlinePromo();
			funLibrary.validate_Equals("PartNumber", searchIndexData.getPartNumber(), apiResponseData.getPartNumber()+"P");
			// Validating api ticket type
			funLibrary.validate_Equals("API_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_API);
			// Validating search index ticket type
			funLibrary.validate_Equals("SI_TicketType", FunLibrary.excelData.get("TicketType"), ticketType_SI);
			// Validating grahic code
			funLibrary.validate_Equals("GrahicCode", graphicCode, FunLibrary.excelData.get("TicketType") + "_" + onlinePromo);
		}
		funLibrary.Assert.assertAll();
	}

	@Override
	public String getTestName() {
		return testName.get();
	}
}
