package com.rest.api;

import static com.rest.api.HttpRequestMethods.valueOf;

import com.rest.body.PS_Payload;
import com.rest.body.Payload;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PS {

	String post = valueOf("POST").name();
	String put = valueOf("PUT").name();
	String get = valueOf("GET").name();
	String delete = valueOf("DELETE").name();

	static String baseUri = "";

	public static Response localizationByAddressId(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String addressId) {

		if(baseURI.equals("")) {
			baseURI = Base_Class_API.BaseURI;
		}
		if(basePath.equals("")) {
			basePath = BasePath.LOCALIZATION_BYADDRESSID.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addBody(PS_Payload.LocalizationByAddressId(storeId, addressId));
		return restLibrary.executeAPI();
	}

	public static Response localizationByLocationId(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String locationId) {
		if(baseURI.equals("")) {
			baseURI = BaseURI.PS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.LOCALIZATION_BYLOCATIONID.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addBody(PS_Payload.localizationByLocationId(storeId, locationId));
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationBySuburbPostCode(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String postcode, String suburb) {

		if(baseURI.equals("")) {
			baseURI = baseUri;
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
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	/**
	 * Localization By Suburb and post code only Base URI, Base Path and Request Type are optional param can be left blank the function will provide
	 * default values for them
	 * 
	 * @param
	 */
	public static Response localizationByFullAddress(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver, String postcode, String suburb, String verificationId, String CCPAddressID) {

		if(baseURI.equals("")) {
			baseURI = baseUri;
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
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}

	public static Response bagEstimator(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {

		if(baseURI.equals("")) {
			baseURI = BaseURI.PS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.BAG_ESTIMATOR.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addBody(PS_Payload.bagEstimator(storeId));
		return restLibrary.executeAPI();
	}
	
	public static Response syncValidate(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String version) {

		if(baseURI.equals("")) {
			baseURI = BaseURI.PS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.SYNC_VALIDATE.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}

		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userAuthorization", userAuthorization);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("ver", version);
		restLibrary.addBody("{}");
		return restLibrary.executeAPI();
	}

	
	public static void EmptyTrolley(String userId, String authorization,String userAuthorization, String storeId) {
		String orderId = "";
		String orderItemId = "";
		String partNumber = "";

		// Check If items are added to the cart. if cart is empty ordderItemCount value should be "No Record Found"
		DatabaseUtilities dbUtils = new DatabaseUtilities();

		orderId = dbUtils.getValues("ORDERS", "ORDERS_ID", "MEMBER_ID = (select users_id from users where field1='" + userId + "') and STATUS='P'");
		orderItemId = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");

		while (!orderItemId.equals("No Record Found")) {
			partNumber = dbUtils.getValues("ORDERITEMS", "PARTNUM", "ORDERS_ID='" + orderId + "'");
			Response response = updateItem("", "", "", authorization, userAuthorization, storeId, partNumber, orderItemId, "0", "false");
			
			if(response.getStatusCode() == 200) {
				System.out.println(partNumber + " Removed successfully from cart");
			} else {
				System.out.println("Failed to remove item from cart. Partnumber: " + partNumber);
			}

			orderItemId = dbUtils.getValues("orderitems", "ORDERITEMS_ID", "orders_id = (select orders_id from orders where member_id = (select users_id from users where field1='" + userId + "') and status = 'P')");
		}
		dbUtils.closeDBConnection();
	}
	
	
	/*
	 * Add Update Trolley Base URI, Base Path and Request Type are optional param can be left blank the function will provide default values for them
	 * 
	 * @param
	 */

	public static Response addUpdateTrolley(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String partNumber, String qty) {
		try {
			//RestAssured.reset();
			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.ADD_UPDATE_TROLLEY.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.PUT.name();
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.addUpdateTrolley(partNumber, qty));
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
	public static Response addItem(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String partNumber, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/cart/item";
			}
			if(requestType.equals("")) {
				requestType = "PUT";
			}

			String body = "	{ \"storeId\":" + storeId + ",\r\n" + "" + "\"additionalDataRequired\":" + additionalFields + ",\r\n" + "" + "	{ \"orderItem\": [\r\n" + "	" + " {\r\n" + "\"partNumber\": \"" + partNumber + "\",\r\n" + "	" + " \"quantity\": \"" + qty + "\"\r\n"
					+ "				    }\r\n" + " ]\r\n" + "}";

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
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
	public static Response updateItem(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String partNumber, String orderItemId, String qty, String additionalFields) {
		try {

			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.ADD_UPDATE_TROLLEY.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.PUT.name();
			}
						
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.updateTrolley(storeId, additionalFields, orderItemId, qty, partNumber));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static Response  getSlotByLocation(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String collectionPointId, String daysForward, String daysSpan, String windowType) {
		if(baseURI.equals("")) {
			baseURI = Base_Class_API.BaseURI;
		}
		if(basePath.equals("")) {
			basePath = BasePath.GET_SLOT_BYLOCATION.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(PS_Payload.getSlotByLocation(storeId, collectionPointId, daysForward, daysSpan, windowType));
		return restLibrary.executeAPI();
	}
	
	
	public static Response  getSlotByLocationDate(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
		if(baseURI.equals("")) {
			baseURI = Base_Class_API.BaseURI;
		}
		if(basePath.equals("")) {
			basePath = BasePath.GET_SLOT_BYLOCATION.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(PS_Payload.getSlotByLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
		return restLibrary.executeAPI();
	}
	
	
	public static Response  getSlotByAddress(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String daysForward, String daysSpan, String suburb, String postcode, String state, String windowType) {
		if(baseURI.equals("")) {
			baseURI = Base_Class_API.BaseURI;
		}
		if(basePath.equals("")) {
			basePath = BasePath.GET_SLOT_BYADDRESS.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userAuthorization);
		restLibrary.addBody(PS_Payload.getSlotByAddress(storeId, daysForward, daysSpan, suburb, postcode, state, windowType));
		return restLibrary.executeAPI();
	}
	
	
	public static Response reserveSlot(String baseURI, String basePath, String requestType,String authorization, String userAuthorization, String storeId, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		try {
			
			if(baseURI.equals("")) {
				baseURI = Base_Class_API.BaseURI;
			}
			if(basePath.equals("")) {
				basePath = BasePath.RESERVE_SLOT.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.PUT.name();
			}
			String body =PS_Payload.reseverSlot(storeId, slotId, serviceType, shiftId, collectionPointId, ccpAddressId);
			
			if(ccpAddressId.equals("")) {
				body = PS_Payload.reseverSlot_NoCCPAddressID(storeId, slotId, serviceType, shiftId);
			}
			
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(body);
			
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Response cartAttribute(String baseURI, String basePath, String requestType,String authorization, String userAuthorization, String storeId, String baggingOption, String unattendedType,String delInstructions, String SubtitueOption) {
		try {
			
			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.CART_ATTRIBUTE.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.POST.name();
			}
			
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.cartAttribute(storeId, baggingOption, unattendedType, delInstructions, SubtitueOption));
			
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
	public static Response applyPromoCode(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String promoCode, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.APPLY_PROMOCODE.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.POST.name();
			}
			
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.applyPromoCode(promoCode));
			restLibrary.addQueryParameter("storeId", storeId);
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
	public static Response removePromoCode(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String promoCode, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.REMOVE_PROMOCODE.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.DELETE.name();
			}
			
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("promoCode", promoCode);
			restLibrary.addQueryParameter("ver", ver);
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
	public static Response OrderLightWeightSummary(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/lightweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
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
	public static Response OrderMediumWeightSummary(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri;
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
			restLibrary.addHeader("userAuthorization", userAuthorization);
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
	public static Response OrderFullWeightSummary(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/cart/summary/fullweight";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response orderProcess(String baseURI, String basePath, String requestType, String storeId, String authorization, String userAuthorization, String ver) {
		try {

			if(baseURI.equals("")) {
				baseURI =BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.ORDER_PROCESS.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.POST.name();
			}
			
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.orderProcess(storeId));
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
	public static Response orderSubmit(String baseURI, String basePath, String requestType, String storeId, String authorization, String userAuthorization, String flybuysBarcodes, String staffDiscountNums, String ver,String salesChannel) {
		try {
			if(baseURI.equals("")) {
				baseURI = BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath =BasePath.ORDER_SUBMIT.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.POST.name();;
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.orderSubmit(salesChannel, storeId, flybuysBarcodes, staffDiscountNums));
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static Response promotion(String baseURI, String basePath, String requestType, String authorization, String storeId, String promotionId) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri;
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

	public static Response orderValidation(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String storeId, String body, String ver) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/cart/sync?storeId={storeId}";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addPathParameter("storeId", storeId);
			restLibrary.addBody(body);
			restLibrary.addQueryParameter("ver", ver);
			return restLibrary.executeAPI();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public static Response authentication(String baseURI, String basePath, String requestType, String authorization, String userAuthorization, String channel) {
		try {
			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/authenticate/auth";
			}
			if(requestType.equals("")) {
				requestType = "POST";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addHeader("channel", channel);
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response getUserAddress(String baseURI, String basePath, String requestType, String Authorization, String userAuthorization, String storeId) {
		try {

			if(baseURI.equals("")) {
				baseURI = baseUri;
			}
			if(basePath.equals("")) {
				basePath = "/profile/addresses";
			}
			if(requestType.equals("")) {
				requestType = "GET";
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", Authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
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
	public static String insertIdentifierNewCard(String subscriptionId, String CardType, String SessionId) {

		return "{\r\n  \"subscriptionId\": \"" + subscriptionId + "\",\r\n  \"paymentMethod\": {\r\n    \"name\": \"" + CardType + "\",\r\n    \"data\": {\r\n      \"identifier\": \"" + SessionId + "\"\r\n    }\r\n  }\r\n}";

	}

	/*
	 * Related to Coles plus subscription and inserts subscriptionId and CardType in the body for No 3DS scenario
	 * 
	 * @param
	 */
	public static String insertIdentifierSavedCard(String subscriptionId, String CardType) {

		return "{\r\n    \"subscriptionId\": \"" + subscriptionId + "\",\r\n    \"paymentMethod\": {\r\n        \"name\": \"" + CardType + "\"\r\n    }\r\n}";

	}
	
	public static String getMPGSCardToken(String cardNo, String month, String year, String cvv) {
		RestAssured.proxy("proxy.cmltd.net.au", 8080);
		String token = "";
		String baseURI = "https://test-cipg.mtf.gateway.mastercard.com/";
		          
		RestLibrary restLibrary = new RestLibrary(baseURI, "form/version/56/merchant/TESTCOLESIPG01/session", "POST");
		restLibrary.setBasicAuthentication("merchant.TESTCOLESIPG01", "bfcf68990e20c2f535f9842472a7962a");
		Response response = restLibrary.executeAPI();
		response.getBody().prettyPrint();

		FunLibrary funLibrary = new FunLibrary();
		token = funLibrary.getJsonPathValue(response, "session.id");
		// RestAssured.reset();
		RestLibrary restLibrary1 = new RestLibrary(baseURI, "api/rest/version/56/merchant/TESTCOLESIPG01/session/" + token, "PUT");
		restLibrary1.setBasicAuthentication("merchant.TESTCOLESIPG01", "bfcf68990e20c2f535f9842472a7962a");
		restLibrary1.addBody(PS_Payload.getMPGSCardToken(cardNo, month, year, cvv));
		response = restLibrary1.executeAPI();
		response.getBody().prettyPrint();
		return token;
	}
	public static Response savePaymentMethod_Card(String baseURI, String basePath, String requestType, String authorization, String userAuthorization,String storeId, String name, String saveToProfile, String identifier) {
		try {

			if(baseURI.equals("")) {
				baseURI =  BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.PAYMENT_BY_CARD.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.POST.name();
			}
			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userAuthorization", userAuthorization);
			restLibrary.addBody(PS_Payload.savePaymentMethod_Card(storeId, name, saveToProfile, identifier));
			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Response deleteSavedCard(String baseURI, String basePath, String requestType, String storeId,String authorization, String userAuthorization,String ver,String paymentMethod) {
		try {
			if(baseURI.equals("")) {
				baseURI =BaseURI.PS.getBaseURI();
			}
			if(basePath.equals("")) {
				basePath = BasePath.DELETE_PAYMENT.getBasePath();
			}
			if(requestType.equals("")) {
				requestType = HttpRequestMethods.DELETE.name();
			}

			RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
			restLibrary.addHeader("authorization", authorization);
			restLibrary.addHeader("userauthorization", userAuthorization);
			restLibrary.addQueryParameter("ver", ver);
			restLibrary.addQueryParameter("storeId", storeId);
			restLibrary.addQueryParameter("name", paymentMethod);

			return restLibrary.executeAPI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Response  updateRDCustomerDetails(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String ver, String remoteDeliveryAddress, String deliveryInstruction, String remoteDeliveryPartnerPaymentAccount, String baggingOption, String unattendedType) {
		if(baseURI.equals("")) {
			baseURI = BaseURI.PS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.CART_ATTRIBUTE.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(Payload.updateRDDetailsCustomer_PS(storeId, remoteDeliveryAddress, deliveryInstruction, remoteDeliveryPartnerPaymentAccount, baggingOption, unattendedType));
		return restLibrary.executeAPI();
	}
	
	public static Response  getSlotByRDLocationDate(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            baseURI = BaseURI.PS.getBaseURI();
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYLOCATION.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("authorization", authorization);
        restLibrary.addHeader("userauthorization", userauthorization);
        restLibrary.addBody(PS_Payload.getSlotByRDLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
	
    public static Response  getSlotByRDLocationDatePublic(String baseURI, String basePath, String requestType, String authorization, String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            //baseURI = BaseURI.PS.getBaseURI();
             baseURI = Base_Class_API.BaseURI;
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYLOCATION_PUBLIC.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("authorization", authorization);
        restLibrary.addBody(PS_Payload.getSlotByRDLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
	
}
