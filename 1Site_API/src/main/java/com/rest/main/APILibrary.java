package com.rest.main;

import com.rest.body.Payload;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APILibrary {

	static String baseUri_WCS = Base_Class_API.WCS_Base_URL;
	static String baseUri_PS = Base_Class_API.PS_Base_URL_1;
	static String baseUri_APIM = Base_Class_API.APIM_Base_URL;

	/**
	 * SSO Authentication for WCS layer Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values
	 * for them
	 * 
	 * @param
	 */
	public static Response SSOAuthentication_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String channel, String storeId, String ver) {

		if(baseURI.equals("")) {
			baseURI = baseUri_WCS;
		}
		if(basePath.equals("")) {
			basePath = "/wcs/resources/store/{storeId}/authenticate/auth";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addHeader("channel", channel);
		restLibrary.addPathParameter("storeId", storeId);
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Address ID Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response localizationByAddressId_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver, String addressId) {

		if(baseURI.equals("")) {
			baseURI = baseUri_WCS;
		}
		if(basePath.equals("")) {
			basePath = "/wcs/resources/store/{storeId}/localisation/byAddressIdentifier/{colAddressId}";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("colAddressId", addressId);
		restLibrary.addQueryParameter("ver", ver);
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationBySuburbPostCode_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver, String postcode, String suburb) {

		if(baseURI.equals("")) {
			baseURI = baseUri_WCS;
		}
		if(basePath.equals("")) {
			basePath = "/wcs/resources/store/{storeId}/localisation/byAddress";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addQueryParameter("postcode", postcode);
		restLibrary.addQueryParameter("suburb", suburb);

		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Full Address Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response localizationByFullAddress_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String addressId) {
		if(baseURI.equals("")) {
			baseURI = baseUri_WCS;
		}
		if(basePath.equals("")) {
			basePath = "/wcs/resources/store/{storeId}/localisation/byAddress";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("colAddressId", addressId);
		return restLibrary.executeAPI();
	}

	/**
	 * Add Update Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addUpdateTrolley_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/updateOrderItemByPartnumber";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response addUpdateTrolley_WCS_1(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String qty, String partNumber) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/updateOrderItemByPartnumber";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "	{  \"additionalDataRequired\":" + "false" + ",\r\n" + "				 \"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + "				  ]\r\n" + "				}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add an Item to Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addItem_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String partNumber, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/updateOrderItemByPartnumber";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			String body = "	{  \"additionalDataRequired\":" + additionalFields + ",\r\n" + "				 \"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + "				  ]\r\n" + "				}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Update an Item in the Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values
	 * for them
	 * 
	 * @param
	 */
	public static Response updateItem_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String partNumber, String orderItemId, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/updateOrderItemByPartnumber";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "	{  \"additionalDataRequired\":" + additionalFields + ",\r\n" + "			\"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \"" + qty + "\",\r\n"
					+ "					  \"orderItemId\": \"" + orderItemId + "\" \r\n" + "				    }\r\n" + "				  ]\r\n" + "				}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Light Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderLightWeightSummary_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/orderDetailsFullweight";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Order Medium Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderMediumWeightSummary_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/orderDetailsMediumweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}
			if(ver.equals(""))
				ver = "1";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Full Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderFullWeightSummary_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/orderDetailsFullweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response OrderFullWeightSummaryWithAddRequired_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver,String addressRequired) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/orderDetailsFullweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("addressRequired", addressRequired);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Get Slots for a particular day Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values
	 * for them
	 * 
	 * @param
	 */
	public static Response getSlots_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String ver) {
		try {
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reserve Slot - Requires Body to be passed as string Base URI, Base Path and Request Type are optional param can be left blank the function will
	 * provide default values for them
	 * 
	 * @param
	 */
	public static Response reserveSlot_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/slots/reserve";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reserve Slot - This function has defined body. User need to pass slotId only Base URI, Base Path and Request Type are optional param can be left
	 * blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response reserveSlot(String storeId, String ver, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {

			String body = "{\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n" + "    \"ccpAddressId\" : \"" + ccpAddressId
					+ "\"\r\n" + "}";

			String basePath = "/wcs/resources/store/{storeId}/slots/reserve";

			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "PUT");
			restLibrary.addHeader("access-token", FunLibrary.excelData.get("Header_access-token"));
			restLibrary.addHeader("user-jwt-token", FunLibrary.excelData.get("Header_user-jwt-token"));
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Process API Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response orderProcess_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/process";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Submit API Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response orderSubmit_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/submit";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderSubmit_WCS_1(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String ver, String flybuysBarcodes, String staffDiscountNums) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/submit";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			String body = "{\r\n" + "    \"salesChannel\" : \"" + "sia" + "\",\r\n" + "    \"flybuysBarcode\" : \"" + flybuysBarcodes + "\",\r\n" + "    \"staffDiscountNum\" : \"" + staffDiscountNums + "\"\r\n" + " }";

			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Promotion API Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response Promotion_WCS(String baseURI, String basePath, String requestType, String AccessToken, String storeId, String promotionId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/productview/enrichment/promotion/{promotionId}";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("promotionId", promotionId);

			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_Card_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String name, String ver, String saveToProfile, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			String body = "{\r\n" + "  \"identifier\": \"" + identifier + "\",\r\n" + "  \"saveToProfile\": " + saveToProfile + "\r\n" + "}";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("name", name);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String name, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("name", name);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_WCS_1(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String name, String ver, String saveToProfile, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/payment/method?name=Card";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			String body = "{\r\n" + "  \"identifier\": \"" + identifier + "\",\r\n" + "  \"saveToProfile\": " + saveToProfile + "\r\n" + "}";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("name", name);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderValidation_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/cart/@self/syncValidateOrder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Apply Promo Code Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response applyPromoCode_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String body, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_WCS;

			if(basePath.equals(""))
				basePath = "/wcs/resources/store/{storeId}/cart/@self/applyPromotionCode";

			if(requestType.equals(""))
				requestType = "POST";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Remove Promo Code Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response removePromoCode_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String promoCode, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_WCS;

			if(basePath.equals(""))
				basePath = "/wcs/resources/store/{storeId}/cart/@self/promotionCode/{promoCode}";

			if(requestType.equals(""))
				requestType = "DELETE";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("promoCode", promoCode);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Related to Coles plus subscription, in progress
	 * 
	 * @param
	 */
	public static Response editSubscription_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String subscriptionId, String CardType, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/v2/subscription/{subscriptionId}";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("subscriptionId", subscriptionId);
			restLibrary.addBody(Payload.getEditSubscriptionPayload_WCS(CardType, identifier));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Related to Coles plus subscription and inserts sessionID and CardType in the body
	 * 
	 * @param
	 */
	public static String insertIdentifierNewCard_WCS(String SessionId, String CardType) {

		return "{\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\",\r\n        \"data\": {\r\n            \"identifier\": \"" + SessionId + "\"\r\n        }\r\n    }\r\n}";

	}

	/*
	 * Related to Coles plus subscription and inserts CardType in the body
	 * 
	 * @param
	 */
	public static String insertIdentifierSavedCard_WCS(String CardType) {

		return "{\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\"\r\n    }\r\n}";

	}

	/*
	 * Related to Coles plus initiate subscription and inserts sessionID and CardType in the body
	 * 
	 * @param
	 */

	public static String initiateSubscriptionNewCard_WCS(String billingFrequency, String CardType, String SessionId) {

		return "{\r\n  \"billingFrequency\": \"" + billingFrequency + "\",\r\n  \"paymentMethod\": {\r\n    \"name\": \"" + CardType + "\",\r\n    \"data\": {\r\n      \"identifier\": \"" + SessionId + "\"\r\n    }\r\n  }\r\n}";

	}

	/*
	 * Related to Coles plus initiate subscription and inserts sessionID and CardType in the body
	 * 
	 * @param
	 */
	public static String initiateSubscriptionSavedCard_WCS(String billingFrequency, String CardType) {

		return "{\r\n    \"billingFrequency\": \"" + billingFrequency + "\",\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\"\r\n    }\r\n}";

	}

	public static Response getUserAddress_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/person/@colself/contact/addresses";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response initiate3Ds_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/{orderId}/payment/authentication/initiate";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addBody(Payload.getInitiate3dsPayload_WCS());
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response proceed3Ds_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/{orderId}/payment/authentication/payer-data";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addBody(Payload.getProceed3dsPayload_WCS());
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response final3Ds_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/{orderId}/payment/authentication?retryCount=0&ver=1";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addBody(Payload.getFinal3dsPayload_WCS());
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response editSubscriptionComplete_WCS(String baseURI, String basePath, String requestType, String AccessToken, String userJWT, String storeId, String subscriptionId, String recurringOrderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/v2/subscription/{subscriptionId}/order/{recurringOrderId}";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("subscriptionId", subscriptionId);
			restLibrary.addPathParameter("recurringOrderId", recurringOrderId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response cancelOrder_WCS(String baseURI, String basePath, String requestType, String AccessToken,String userJWT,String storeId,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/cancelOrder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			/*restLibrary.addQueryParameter("ver", ver);*/
			restLibrary.addQueryParameter("orderId", orderId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response getOrderItems_WCS(String baseURI, String basePath, String requestType, String AccessToken,String userJWT,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/items";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("orderId", orderId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response modifyOrder_WCS(String baseURI, String basePath, String requestType, String AccessToken,String userJWT,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/modifyOrder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("orderId", orderId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response invoice_WCS(String baseURI, String basePath, String requestType, String AccessToken,String userJWT,String storeId, String orderId, String format, String fileName) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/{orderId}/invoice";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("format", format);
			restLibrary.addQueryParameter("fileName", fileName);
			
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response getOrderDetail_WCS(String baseURI, String basePath, String requestType, 
			String AccessToken, String userJWT, String storeId, String orderId, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_WCS;
			}
			if(basePath.equals("")) {
				basePath = "/wcs/resources/store/{storeId}/order/{orderId}/details";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("access-token", AccessToken);
			restLibrary.addHeader("user-jwt-token", userJWT);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/********************************************* PS LAYER APIs ****************************************************/
	/**************************************************************************************************************/

	public static Response getSlotByLocation_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String collectionPointId, String daysForward, String daysSpan, String windowType) {

		if(baseURI.equals("")) {
			baseURI =baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/slots/collection/details/self";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n  \"storeId\": \"" + storeId + "\",\r\n  \"collectionPointId\": \"" + collectionPointId + "\",\r\n  \"daysForward\": " + daysForward + ",\r\n  \"daysSpan\": " + daysSpan + ",\r\n  \"sortBy\": \"1\",\r\n  \"slotsChannel\": 1,\r\n  \"windowType\": \"" + windowType
				+ "\"\r\n}";

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	public static Response getSlotByAddress_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String daysForward, String daysSpan, String suburb, String postcode, String state, String windowType) {

		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/slots/delivery/address/self";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n    \"storeId\": \"" + storeId + "\",\r\n    \"daysForward\": " + daysForward + ",\r\n    \"daysSpan\": " + daysSpan + ",\r\n    \"sortBy\": \"1\",\r\n    \"slotsChannel\": 1,\r\n    \"windowType\": \"" + windowType + "\",\r\n    \"suburb\": \"" + suburb
				+ "\",\r\n    \"postcode\": \"" + postcode + "\",\r\n    \"state\": \"" + state + "\",\r\n    \"longitude\": \"144.97151\",\r\n    \"latitude\": \"-37.82505\"\r\n}";

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	public static Response localizationByAddressId_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String addressId) {

		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/localisation/address/id";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{ \"storeId\":\"" + storeId + "\",  \r\n" + " \"colAddressId\": \"" + addressId + "\" \r\n" + "}";

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	public static Response localizationByLocationId_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String locationId) {
		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/localisation/location/id";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n" + "  \"locationId\": \"" + locationId + "\",\r\n" + "  \"storeId\":\"" + storeId + "\"\r\n" + "}";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, "/localisation/location/id", "POST");
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationBySuburbPostCode_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String postcode, String suburb) {

		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/localisation/address";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n" + "\"storeId\": \"" + storeId + "\",\r\n" + "\"suburb\": \"" + suburb + "\",\r\n" + "\"postcode\": \"" + postcode + "\"\r\n}";
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}
	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationBySuburbPostCode_APIM(String baseURI, String basePath, String requestType, 
			String authorization, String userauthorization, String subscriptionKey, 
			String storeId, String postcode, String suburb) {

		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/localisation/address";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n" + "\"storeId\": \"" + storeId + "\",\r\n" + "\"suburb\": \"" + suburb + "\",\r\n" + "\"postcode\": \"" + postcode + "\"\r\n}";
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationByFullAddress_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String ver, String postcode, String suburb, String verificationId, String CCPAddressID) {

		if(baseURI.equals("")) {
			baseURI = baseUri_PS;
		}
		if(basePath.equals("")) {
			basePath = "/localisation/address";
		}
		if(requestType.equals("")) {
			requestType = "POST";
		}
		String body = "{\r\n" + "\"storeId\": \"" + storeId + "\",\r\n" + "\"suburb\": \"" + suburb + "\",\r\n" + "\"postcode\": \"" + postcode + "\",\r\n" + "\"country\": \"AU\",\r\n" + "\"longitude\": \"130.85404981\",\r\n" + "\"latitude\": \"-12.43978589\",\r\n" + "\"verificationId\": \""
				+ verificationId + "\",\r\n" + "\"ccpAddressId\": \"" + CCPAddressID + "\"\r\n" + "}";
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	/*
	 * Add Update Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addUpdateTrolley_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String body) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response addUpdateTrolley_PS_1(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String partNumber, String qty) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			String body = "	{  \"additionalDataRequired\":" + "false" + ",\r\n" + "				 \"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + "				  ]\r\n" + "				}";

			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add an Item to Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addItem_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String partNumber, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			String body = "	{ \"storeId\":\"" + storeId + "\",\r\n" + "" + "\"additionalDataRequired\":\"" + additionalFields + "\",\r\n" + "" + " \"orderItem\": [\r\n" + "	" + " {\r\n" + "\"partNumber\": \"" + partNumber + "\",\r\n" + "	" + " \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + " ]\r\n" + "}";
			

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Update an Item in the Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values
	 * for them
	 * 
	 * @param
	 */
	public static Response updateItem_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String partNumber, String orderItemId, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/item";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "	{  \"storeId\":\"" + storeId + "\",\r\n\"additionalDataRequired\":" + additionalFields + ",\r\n" + "			\"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \""
					+ qty + "\",\r\n" + "					  \"orderItemId\": \"" + orderItemId + "\" \r\n" + "				    }\r\n" + "				  ]\r\n" + "				}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Order Full Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderLightWeightSummary_PS(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/lightweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
//			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Order Medium Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderMediumWeightSummary_PS(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/mediumweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}
			if(ver.equals(""))
				ver = "1";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Full Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderFullWeightSummary_PS(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/fullweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderProcessPS_1(String baseURI, String basePath, String requestType, String storeId, String Authorization, String userAuthorization, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/process";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}
			// String storeId="0404";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			String body = "{\r\n" + "    \"storeId\" : \"" + "0404" + "\"\r\n" + "}";
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderProcessPS(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String RequestBody, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/process";
			}
			if(requestType.equals("")) {
				requestType = "POST";

			}
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);

			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(RequestBody);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_Card_PS(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String storeId, String name, String saveToProfile, String identifier, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}
			String body = "{\r\n" + "  \"storeId\": \"" + storeId + "\",\r\n" + "  \"name\": \"" + name + "\",\r\n" + "  \"identifier\": \"" + identifier + "\",\r\n" + "  \"saveToProfile\": " + saveToProfile + "\r\n" + "}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_PS(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String body, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Submit API Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response orderSubmit_PS(String baseURI, String basePath, String requestType, 
			String Authorization, String userAuthorization, String body) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/submit";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response getOrderDetail_PS(String baseURI, String basePath, String requestType, String Authorization,
			String userAuthorization, String orderId, String ver) {
		try {
			if (baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if (basePath.equals("")) {
				basePath = "/order/{orderId}/details";
			}
			if (requestType.equals("")) {
				requestType = "GET";
			}
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderSubmit_PS_1(String baseURI, String basePath, String requestType, String storeId, String Authorization, String userAuthorization, String flybuysBarcodes, String staffDiscountNums, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/submit";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			String body = "{\r\n" + "    \"salesChannel\" : \"" + "cusp" + "\",\r\n" + "    \"flybuysBarcode\" : \"" + flybuysBarcodes + "\",\r\n" + "    \"staffDiscountNum\" : \"" + staffDiscountNums + "\",\r\n" + "      \"storeId\"  : \"" + storeId + "\"\r\n" + " }";

			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response reserveSlot_PS(String storeId, String ver, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {
			String body = "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n"
					+ "    \"ccpAddressId\" : \"" + ccpAddressId + "\"\r\n" + "}\r\n" + "";
			String basePath = "/slots/reserve";

			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "PUT");
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("userauthorization", FunLibrary.excelData.get("Header_userauthorization"));
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response promotion_PS(String baseURI, String basePath, String requestType, String authorization, String storeId, String promotionId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/promotion";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("promotionId", promotionId);
			return restLibrary.executeAPI();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderValidation_PS(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String body, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/sync?storeId={storeId}";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Apply Promo Code Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response applyPromoCode_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String body, String storeId, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_PS;

			if(basePath.equals(""))
				basePath = "/applyPromotionCode";

			if(requestType.equals(""))
				requestType = "POST";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);

			restLibrary.addBody(body);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Remove Promo Code Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response removePromoCode_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String promoCode, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_PS;

			if(basePath.equals(""))
				basePath = "/deletepromotioncode";

			if(requestType.equals(""))
				requestType = "DELETE";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("promoCode", promoCode);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response authentication_PS(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String channel) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/authenticate/auth";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("channel", channel);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response getUserAddress_PS(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/profile/addresses";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Related to Coles plus subscription and inserts subscriptionId, sessionID and CardType in the body with 3DS required scenario
	 * 
	 * @param
	 */
	public static String insertIdentifierNewCard_PS(String subscriptionId, String CardType, String SessionId) {

		return "{\r\n  \"subscriptionId\": \"" + subscriptionId + "\",\r\n  \"paymentMethod\": {\r\n    \"name\": \"" + CardType + "\",\r\n    \"data\": {\r\n      \"identifier\": \"" + SessionId + "\"\r\n    }\r\n  }\r\n}";

	}

	/*
	 * Related to Coles plus subscription and inserts subscriptionId and CardType in the body for No 3DS scenario
	 * 
	 * @param
	 */
	public static String insertIdentifierSavedCard_PS(String subscriptionId, String CardType) {

		return "{\r\n    \"subscriptionId\": \"" + subscriptionId + "\",\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\"\r\n    }\r\n}";

	}

	/*
	 * Related to Coles plus initiate subscription and inserts billing frequency sessionID and CardType in the body with 3DS required scenario
	 * 
	 * @param
	 */
	public static String initiateSubscriptionNewCard_PS(String billingFrequency, String CardType, String SessionId) {

		return "{\r\n  \"billingFrequency\": \"" + billingFrequency + "\",\r\n  \"paymentMethod\": {\r\n    \"name\": \"" + CardType + "\",\r\n    \"data\": {\r\n      \"identifier\": \"" + SessionId + "\"\r\n    }\r\n  }\r\n}";

	}

	/*
	 * Related to Coles plus initiate subscription and inserts billing frequency sessionID and CardType in the body with 3DS required scenario
	 * 
	 * @param
	 */
	public static String initiateSubscriptionSavedCard_PS(String billingFrequency, String CardType) {

		return "{\r\n    \"billingFrequency\": \"" + billingFrequency + "\",\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\"\r\n    }\r\n}";

	}

	public static Response deleteSavedCard(String baseURI, String basePath, String requestType, String storeId, String Authorization, String userAuthorization, String ver, String cardName) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "DELTE";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("name", cardName);

			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response editSubscription_PS(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionId, String CardType, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/subscription";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addBody(Payload.getEditSubscriptionPayload_PS(subscriptionId, CardType, identifier));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response initiate3Ds_PS(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/authentication/initiate";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addBody(Payload.getInitiate3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response proceed3Ds_PS(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/authentication/payer-data";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addBody(Payload.getProceed3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response final3Ds_PS(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/order/payment/authentication";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addBody(Payload.getFinal3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response editSubscriptionComplete_PS(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String ver, String subscriptionId, String recurringOrderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/subscription/order?ver=2.0";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addBody(Payload.getEditSubscriptionComplete_PS(subscriptionId, recurringOrderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Cancel order Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	
	public static Response cancelOrder_PS(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/orders/cancelorder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			/*restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);*/
			restLibrary.addBody(Payload.getOrderId(orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response getOrderItems_PS(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/orders/{orderId}/items";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response modifyOrder_PS(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/orders/modifyorder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addBody(Payload.getmodifyOrderId(orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response invoice_PS(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization, String format, String fileName) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/orders/{orderId}/invoice";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			//restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("format", format);
			restLibrary.addQueryParameter("fileName", fileName);
			
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/********************************************* APIM LAYER APIs ***********************************************************/
	/***********************************************************************************************************************/

	/*
	 * Add Update Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addUpdateTrolley_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String subscriptionKey, String body) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response addUpdateTrolley_APIM_1(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String subscriptionKey, String partNumber, String qty) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			String body = "	{  \"additionalDataRequired\":" + "false" + ",\r\n" + "				 \"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + "				  ]\r\n" + "				}";

			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add an Item to Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response addItem_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String subscriptionKey, String storeId, String partNumber, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/items";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			
			String body= "{ \n" + 
					"    \"storeId\":\""+storeId+"\",\n" + 
					"    \"additionalDataRequired\":\""+additionalFields+"\",\n" + 
					"	    \"orderItem\": [\n" + 
					"	        {\n" + 
					"                \"partNumber\": \""+partNumber+"\",\n" + 
					"	             \"quantity\": \""+qty+"\"\n" + 
					"			  }\n" + 
					" ]\n" + 
					"}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Update an Item in the Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values
	 * for them
	 * 
	 * @param
	 */
	public static Response updateItem_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String subscriptionKey, String storeId, String partNumber, String orderItemId, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_PS;
			}
			if(basePath.equals("")) {
				basePath = "/cart/item";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "	{  \"storeId\":\"" + storeId + "\",\r\n\"additionalDataRequired\":" + additionalFields + ",\r\n" + "			\"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \""
					+ qty + "\",\r\n" + "					  \"orderItemId\": \"" + orderItemId + "\" \r\n" + "				    }\r\n" + "				  ]\r\n" + "				}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(body);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Order Full Weight Summary Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for
	 * them
	 * 
	 * @param
	 */
	public static Response OrderFullWeightSummary_APIM(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String subscriptionKey, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/fullweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderProcessAPIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String RequestBody, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/process";
			}
			if(requestType.equals("")) {
				requestType = "POST";

			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(RequestBody);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderProcessAPIM_1(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String subscriptionKey, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/process";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			String body = "{\r\n" + "    \"storeId\" : \"" + "0404" + "\"\r\n" + "}";

			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response reserveSlot_APIM(String storeId, String ver, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {
			String body = "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n"
					+ "    \"ccpAddressId\" : \"" + ccpAddressId + "\"\r\n" + "}\r\n" + "";
			String basePath = "/slots/reserve";

			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "PUT");
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("userauthorization", FunLibrary.excelData.get("Header_userauthorization"));
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response reserveSlot_APIM_3(String baseURI, String basePath, String requestType,String authorization,String userauthorization,String Subscription, String storeId, String ver, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/slots/reserve";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n"
					+ "    \"ccpAddressId\" : \"" + ccpAddressId + "\"\r\n" + "}\r\n" + "";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization",authorization );
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key",Subscription);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response reserveSlot_APIM_1(String baseURI, String basePath, String requestType, String storeId, String ver, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/slots/reserve";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}
			String body = "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n"
					+ "    \"ccpAddressId\" : \"" + ccpAddressId + "\"\r\n" + "}\r\n" + "";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("userauthorization", FunLibrary.excelData.get("Header_userauthorization"));
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response promotion_APIM(String baseURI, String basePath, String requestType, String authorization, String subscription, String storeId, String promotionId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/promotion";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("promotionId", promotionId);

			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_Card_APIM(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String subscription, String storeId, String name, String saveToProfile, String identifier, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}
			String body = "{\r\n" + "  \"storeId\": \"" + storeId + "\",\r\n" + "  \"name\": \"" + name + "\",\r\n" + "  \"identifier\": \"" + identifier + "\",\r\n" + "  \"saveToProfile\": " + saveToProfile + "\r\n" + "}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response savePaymentMethod_APIM(String baseURI, String basePath, String requestType, String Authorization, String subscription, String userAuthorization, String body, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/order/payment/method";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", FunLibrary.excelData.get("Header_authorization"));
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", FunLibrary.excelData.get("Header_subscriptionKey"));
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderValidation_APIM(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String subscriptionKey, String storeId, String body, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/sync?storeId={storeId}";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response applyPromoCode_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String body, String storeId, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_APIM;

			if(basePath.equals(""))
				basePath = "/col/ecommerce/v1/applypromotioncode";

			if(requestType.equals(""))
				requestType = "POST";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");

			restLibrary.addBody(body);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Remove Promo Code Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */
	public static Response removePromoCode_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String promoCode, String ver) {
		try {

			if(baseURI.equals(""))
				baseURI = baseUri_APIM;

			if(basePath.equals(""))
				basePath = "/deletepromotioncode";

			if(requestType.equals(""))
				requestType = "DELETE";

			if(ver.equals(""))
				ver = "1";
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userauthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("promoCode", promoCode);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderSubmit_APIM(String baseURI, String basePath, String requestType, String storeId, String Authorization, String userAuthorization, String subscriptionKey, String flybuysBarcodes, String staffDiscountNums, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/cart/submit";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			String body = "{\r\n" + "    \"salesChannel\" : \"" + "sia" + "\",\r\n" + "    \"flybuysBarcode\" : \"" + flybuysBarcodes + "\",\r\n" + "    \"staffDiscountNum\" : \"" + staffDiscountNums + "\",\r\n" + "      \"storeId\"  : \"" + storeId + "\"\r\n" + " }";

			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response editSubscription_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String subscriptionId, String CardType, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/subscription";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(Payload.getEditSubscriptionPayload_PS(subscriptionId, CardType, identifier));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response initiate3Ds_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/order/payment/authentication/initiate";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(Payload.getInitiate3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response proceed3Ds_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/order/payment/authentication/payer-data";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(Payload.getProceed3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response final3Ds_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String storeId, String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/order/payment/authentication";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(Payload.getFinal3dsPayload_PS(storeId, orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response editSubscriptionComplete_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String subscriptionId, String recurringOrderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/subscription/order";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addBody(Payload.getEditSubscriptionComplete_APIM(subscriptionId, recurringOrderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * Cancel Order Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */

	public static Response cancelOrder_APIM(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/orders/cancelorder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");
			/*restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);*/
			restLibrary.addBody(Payload.getOrderId(orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static Response getOrderItems_APIM(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/orders/{orderId}/items";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response modifyOrder_APIM(String baseURI, String basePath, String requestType, String userAuthorization,String Authorization,String storeId,String ver,String orderId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/orders/modifyorder";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addBody(Payload.getmodifyOrderId(orderId));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response OrderMediumWeightSummary_APIM(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/col/ecommerce/v1/cart/summary/mediumweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}
			if(ver.equals(""))
				ver = "1";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", "2b8f282f96ec42899881bf30ca9f5fd1");
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static Response invoice_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String fileName, String format) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/orders/158611042/invoice";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			//restLibrary.addQueryParameter("orderId",orderId);
			restLibrary.addQueryParameter("fileName", fileName);
			restLibrary.addQueryParameter("format", format);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response getOrderDetail_APIM(String baseURI, String basePath, String requestType, String userAuthorization, String Authorization, String subscriptionKey, String orderId, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri_APIM;
			}
			if(basePath.equals("")) {
				basePath = "/order/{orderId}/details";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			restLibrary.addPathParameter("orderId", orderId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/********************************************* COMMON UTILITY METHODS ****************************************************/
	/***********************************************************************************************************************/
	public static String RemoveBearer(String Header_userauthorization) {
		String JWT = Header_userauthorization.replace("Bearer", "");
		JWT = JWT.replaceAll("\\s+", "");
		return JWT;
	}

	public static void EmptyTrolley(String userId, String userJWT, String storeId) {
		String orderId = "";
		String orderItemId = "";
		String partNumber = "";
		userJWT = userJWT.replace("Bearer", "");
		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();

		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + userId + "') and STATUS='P'");
		orderItemId = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");

		while (!orderItemId.equals("No Record Found")) {
			partNumber = dbUtils.getValues("ORDERITEMS", "PARTNUM", "ORDERS_ID='" + orderId + "'");
			Response response = updateItem_WCS("", "", "", "YZGvsvvE6O2NF126uIBVFvX/L/RQstwN", userJWT, storeId, partNumber, orderItemId, "0", "false");
			response.getBody().prettyPrint();
			if(response.getStatusCode() == 200) {
				System.out.println(partNumber + " Removed successfully from cart");
			} else {
				System.out.println("Failed to remove item from cart. Partnumber: " + partNumber);
			}

			orderItemId = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		}
		dbUtils.closeDBConnection();
	}

	public static String getMPGSCardToken(String cardNo, String month, String year, String cvv) {
		Response response;
		String token = "";
		String baseURI = "https://test-cipg.mtf.gateway.mastercard.com/";
		String body = "{\r\n" + "    \"sourceOfFunds\": {\r\n" + "        \"type\": \"CARD\",\r\n" + "        \"provided\": {\r\n" + "            \"card\": {\r\n" + "                \"expiry\": {\r\n" + "                    \"month\": \"" + month + "\",\r\n" + "                    \"year\": \""
				+ year + "\"\r\n" + "                },\r\n" + "                \"number\": \"" + cardNo + "\",\r\n" + "                \"securityCode\": \"" + cvv + "\"\r\n" + "            }\r\n" + "        }\r\n" + "    }\r\n" + "}";

		try {
			RestLibrary restLibrary = new RestLibrary(baseURI, "form/version/56/merchant/TESTCOLESIPG01/session", "POST");
			restLibrary.setBasicAuthentication("merchant.TESTCOLESIPG01", "bfcf68990e20c2f535f9842472a7962a");
			response = restLibrary.executeAPI();
			response.getBody().prettyPrint();
			
		} catch (Exception e) {
			RestAssured.proxy("proxy.cmltd.net.au", 8080);
			RestLibrary restLibrary = new RestLibrary(baseURI, "form/version/56/merchant/TESTCOLESIPG01/session", "POST");
			restLibrary.setBasicAuthentication("merchant.TESTCOLESIPG01", "bfcf68990e20c2f535f9842472a7962a");
			response = restLibrary.executeAPI();
			response.getBody().prettyPrint();
		}

		FunLibrary funLibrary = new FunLibrary();
		token = funLibrary.getJsonPathValue(response, "session.id");
		// RestAssured.reset();
		RestLibrary restLibrary1 = new RestLibrary(baseURI, "api/rest/version/56/merchant/TESTCOLESIPG01/session/" + token, "PUT");
		restLibrary1.setBasicAuthentication("merchant.TESTCOLESIPG01", "bfcf68990e20c2f535f9842472a7962a");
		restLibrary1.addBody(body);
		response = restLibrary1.executeAPI();
		response.getBody().prettyPrint();
		return token;
	}
	
	

}
