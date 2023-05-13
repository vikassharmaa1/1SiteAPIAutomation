package com.rest.body;


import io.restassured.response.Response;

import com.rest.api.BasePath;
import com.rest.api.BaseURI;
import com.rest.api.HttpRequestMethods;
import com.rest.main.Base_Class_API;
import com.rest.main.RestLibrary;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Payload {
	
	public static String rdAddressPaylod(String storeid, String suburb, String postcode, String country, String verificationid, String ccpaddressid )
	{
		return "{\r\n" + 
				"  \"storeId\": \""+storeid+"\",\r\n" + 
				"  \"addressLine\": \"100 Collins Street\",\r\n" + 
				"  \"suburb\": \""+suburb+"\",\r\n" + 
				"  \"postcode\": \""+postcode+"\",\r\n" + 
				"  \"country\": \""+country+"\",\r\n" + 
				"  \"longitude\": \"138.61275\",\r\n" + 
				"  \"latitude\": \"-33.839798\",\r\n" + 
				"  \"verificationId\": \""+verificationid+"\",\r\n" + 
				"  \"ccpAddressId\": \""+ccpaddressid+"\"\r\n" + 
				"}";
	}
	public static String rdAddressPayload(String storeid, String suburb, String postcode, String country, String verificationid, String ccpaddressid,String longitude,String latitude )
	{
		return "{\r\n" + 
				"  \"storeId\": \""+storeid+"\",\r\n" + 
				"  \"suburb\": \""+suburb+"\",\r\n" + 
				"  \"postcode\": \""+postcode+"\",\r\n" + 
				"  \"country\": \""+country+"\",\r\n" + 
				"  \"longitude\": \""+longitude+"\",\r\n" + 
				"  \"latitude\": \""+latitude+"\",\r\n" + 
				"  \"verificationId\": \""+verificationid+"\",\r\n" + 
				"  \"ccpAddressId\": \""+ccpaddressid+"\"\r\n" + 
				"}";
	}
	public static Response  slotreservation( String AccessToken, String userJWT, String storeId,String slotID) {

		String basePath="wcs/resources/store/{storeId}/slots/reserve";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "Put");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addBody(getreservationpayload(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayload(String slotid )
	{
		return "{\r\n" + 
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"CC\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \"0404CC0404\",\r\n" + 
				"  \"ccpAddressId\": \"af924fda-3b79-427d-b042-eccb76721b4e\",\r\n" + 
				"}";
	}
	
	
	public static Response  slotreservation_WCS( String AccessToken, String userJWT, String storeId,String slotID) {

		String basePath="/wcs/resources/store/{storeId}/slots/reserve";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "Put");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addBody(getreservationpayload_WCS(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayload_WCS(String slotid )
	{
		return "{\r\n" + 
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"CC\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \"0404CC0404\"\r\n" + 
				"}";
	}
	
	
	public static Response  slotreservation_PS( String authorization, String userauthorization, String slotID) {

		String basePath="/slots/reserve";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "Put");
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(getreservationpayload_PS(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayload_PS(String slotid )
	{
		return "{\r\n" + 
				"  \"storeId\": \"0404\",\r\n" +
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"CC\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \"0404CC0404\"\r\n" + 
				"}";
	}
	
	
	public static Response  slotreservation_APIM(String OcpApimSubscriptionKey, String authorization, String userauthorization, String slotID) {

		String basePath="/slots/reserve";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "Put");
		restLibrary.addHeader("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(getreservationpayload_APIM(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayload_APIM(String slotid )
	{
		return "{\r\n" + 
				"  \"storeId\": \"0404\",\r\n" +
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"CC\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \"0404CC0404\"\r\n" + 
				"}";
	}
	
	 
	public static Response  localizationBySuburb( String AccessToken, String userJWT,  String storeId,String postcode, String suburb) {

		String basePath="/wcs/resources/store/{storeId}/localisation/byAddress";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "POST");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("postcode", postcode);
		restLibrary.addQueryParameter("suburb", suburb);
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
	
		}
	
	
	public static Response  localizationByCollection( String AccessToken, String userJWT,  String storeId,String locationID) {

		String basePath="/wcs/resources/store/{storeId}/localisation/byLocationIdentifier/{locationId}";
		RestLibrary restLibrary = new RestLibrary("https://wcssitint.cmltd.net.au:27901", basePath, "POST");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("locationId", locationID);
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
	
		}
	
	
	public static Response localizationByAddressId(  String AccessToken, String userJWT, String storeId,  String addressId) {
		String basePath="/wcs/resources/store/{storeId}/localisation/byAddressIdentifier/{colAddressId}";
		RestLibrary restLibrary = new RestLibrary("https://wcssitint.cmltd.net.au:27901", basePath, "POST");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("colAddressId", addressId);
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static Response  slotreservationAddress_WCS( String AccessToken, String userJWT, String storeId,String slotID) {

		String basePath="/wcs/resources/store/{storeId}/slots/reserve";
		RestLibrary restLibrary = new RestLibrary("https://wcssitint.cmltd.net.au:27901", basePath, "Put");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addBody(getreservationpayloadAddress_WCS(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayloadAddress_WCS(String slotid )
	{
		return "{\r\n" + 
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"HD\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\"\r\n" +
				"}";
	}
	
	
	
	
	public static Response  slotreservationAddress_PS( String authorization, String userauthorization, String slotID) {

		String basePath="/slots/reserve";
		RestLibrary restLibrary = new RestLibrary("https://col-ecommerce-test.azurewebsites.net", basePath, "Put");
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(getreservationpayloadAddress_PS(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	public static Response  slotreservationAddres_PS( String authorization, String userauthorization, String slotID) {

		String basePath="/slots/reserve";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "Put");
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(getreservationpayloadAddress_PS(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	public static String getreservationpayloadAddress_PS(String slotid )
	{
		return "{\r\n" + 
				"  \"storeId\": \"0404\",\r\n" +
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"HD\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\"\r\n" + 
				"}";
	}
	
	
	public static Response  slotreservationAddress_APIM(String OcpApimSubscriptionKey, String authorization, String userauthorization, String slotID) {

		String basePath="/slots/reserve";
		RestLibrary restLibrary = new RestLibrary("https://test2apigw.cmltd.net.au/col/ecommerce/v1", basePath, "Put");
		restLibrary.addHeader("Ocp-Apim-Subscription-Key", OcpApimSubscriptionKey);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addBody(getreservationpayloadAddress_APIM(slotID));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	
	public static String getreservationpayloadAddress_APIM(String slotid )
	{
		return "{\r\n" + 
				"  \"storeId\": \"0404\",\r\n" +
				"  \"slotId\": \""+slotid+"\",\r\n" + 
				"  \"serviceType\": \"HD\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\"\r\n" +
				"}";
	}
	
	public static String getEditSubscriptionPayload_WCS(String CardType, String identifier)
	{
		return "{\r\n" + 
				"    \"paymentMethod\": {\r\n" + 
				"        \"name\": \""+CardType+"\",\r\n" + 
				"        \"data\": {\r\n" + 
				"            \"identifier\": \""+identifier+"\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
	}
	
	public static String getInitiate3dsPayload_WCS()
	{
		return "{\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getProceed3dsPayload_WCS()
	{
		return "{\r\n" + 
				"    \"browserUserAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.81 Safari/537.36\",\r\n" + 
				"    \"browserDetails\": {\r\n" + 
				"        \"threedsChallengeWindowSize\": \"FULL_SCREEN\",\r\n" + 
				"        \"acceptHeaders\": \"application/json\",\r\n" + 
				"        \"colorDepth\": 23,\r\n" + 
				"        \"javaEnabled\": true,\r\n" + 
				"        \"language\": \"en-US\",\r\n" + 
				"        \"screenHeight\": 1152,\r\n" + 
				"        \"screenWidth\": 2049,\r\n" + 
				"        \"timeZone\": -660\r\n" + 
				"    },\r\n" + 
				"    \"customerIPAddress\": \"124.184.216.211\",\r\n" + 
				"    \"redirectUrl\": \"https://shop.coles.com.au/webapp/wcs/stores/servlet/authenticationRedirect\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getFinal3dsPayload_WCS()
	{
		return "{\r\n" + 
				"    \"gatewayOrderId\": \"115579006-bGjjRjVRWh001\",\r\n" + 
				"    \"gatewayRecommendation\": \"PROCEED\",\r\n" + 
				"    \"transactionId\": \"150111957_1597825216511\",\r\n" + 
				"    \"tag\": \"n0m4fUd1JOP72qsKubDWqg==\",\r\n" + 
				"    \"nonce\": \"7oqdOwz1fUyAAAA5\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getEditSubscriptionPayload_PS(String subscriptionId, String CardType, String identifier)
	{
		return "{\r\n" + 
				"    \"subscriptionId\": \""+subscriptionId+"\",\r\n" + 
				"    \"paymentMethod\": {\r\n" + 
				"        \"name\": \""+CardType+"\",\r\n" + 
				"        \"data\": {\r\n" + 
				"            \"identifier\": \""+identifier+"\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
	}
	
	public static String getInitiate3dsPayload_PS(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"isSubscriptionOrder\": true,\r\n" + 
				"    \"orderId\": \""+orderId+"\"\r\n" + 
				"}";
	}
	
	public static String getProceed3dsPayload_PS(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"orderId\": \""+orderId+"\",\r\n" + 
				"    \"browserUserAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.81 Safari/537.36\",\r\n" + 
				"    \"browserDetails\": {\r\n" + 
				"        \"threedsChallengeWindowSize\": \"FULL_SCREEN\",\r\n" + 
				"        \"acceptHeaders\": \"application/json\",\r\n" + 
				"        \"colorDepth\": 23,\r\n" + 
				"        \"javaEnabled\": true,\r\n" + 
				"        \"language\": \"en-US\",\r\n" + 
				"        \"screenHeight\": 1152,\r\n" + 
				"        \"screenWidth\": 2049,\r\n" + 
				"        \"timeZone\": -660\r\n" + 
				"    },\r\n" + 
				"    \"customerIPAddress\": \"124.184.216.211\",\r\n" + 
				"    \"redirectUrl\": \"https://shop.coles.com.au/webapp/wcs/stores/servlet/authenticationRedirect\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getFinal3dsPayload_PS(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"orderId\": \""+orderId+"\",\r\n" + 
				"    \"gatewayOrderId\": \"115579006-bGjjRjVRWh001\",\r\n" + 
				"    \"gatewayRecommendation\": \"PROCEED\",\r\n" + 
				"    \"transactionId\": \"150111957_1597825216511\",\r\n" + 
				"    \"tag\": \"n0m4fUd1JOP72qsKubDWqg==\",\r\n" + 
				"    \"nonce\": \"7oqdOwz1fUyAAAA5\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getEditSubscriptionComplete_PS(String subscriptionId, String recurringOrderId)
	{
		return "{\r\n" + 
				"    \"subscriptionId\": \""+subscriptionId+"\",\r\n" + 
				"    \"recurringOrderId\": \""+recurringOrderId+"\"\r\n" + 
				"}";
	}
	
	public static String getEditSubscriptionPayload_APIM(String subscriptionId, String CardType, String identifier)
	{
		return "{\r\n" + 
				"    \"subscriptionId\": \""+subscriptionId+"\",\r\n" + 
				"    \"paymentMethod\": {\r\n" + 
				"        \"name\": \""+CardType+"\",\r\n" + 
				"        \"data\": {\r\n" + 
				"            \"identifier\": \""+identifier+"\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
	}
	
	public static String getInitiate3dsPayload_APIM(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"isSubscriptionOrder\": true,\r\n" + 
				"    \"orderId\": \""+orderId+"\"\r\n" + 
				"}";
	}
	
	public static String getProceed3dsPayload_APIM(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"orderId\": \""+orderId+"\",\r\n" + 
				"    \"browserUserAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.81 Safari/537.36\",\r\n" + 
				"    \"browserDetails\": {\r\n" + 
				"        \"threedsChallengeWindowSize\": \"FULL_SCREEN\",\r\n" + 
				"        \"acceptHeaders\": \"application/json\",\r\n" + 
				"        \"colorDepth\": 23,\r\n" + 
				"        \"javaEnabled\": true,\r\n" + 
				"        \"language\": \"en-US\",\r\n" + 
				"        \"screenHeight\": 1152,\r\n" + 
				"        \"screenWidth\": 2049,\r\n" + 
				"        \"timeZone\": -660\r\n" + 
				"    },\r\n" + 
				"    \"customerIPAddress\": \"124.184.216.211\",\r\n" + 
				"    \"redirectUrl\": \"https://shop.coles.com.au/webapp/wcs/stores/servlet/authenticationRedirect\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getFinal3dsPayload_APIM(String storeId, String orderId)
	{
		return "{\r\n" + 
				"    \"storeId\": \""+storeId+"\",\r\n" + 
				"    \"orderId\": \""+orderId+"\",\r\n" + 
				"    \"gatewayOrderId\": \"115579006-bGjjRjVRWh001\",\r\n" + 
				"    \"gatewayRecommendation\": \"PROCEED\",\r\n" + 
				"    \"transactionId\": \"150111957_1597825216511\",\r\n" + 
				"    \"tag\": \"n0m4fUd1JOP72qsKubDWqg==\",\r\n" + 
				"    \"nonce\": \"7oqdOwz1fUyAAAA5\",\r\n" + 
				"    \"isSubscriptionOrder\": true\r\n" + 
				"}";
	}
	
	public static String getEditSubscriptionComplete_APIM(String subscriptionId, String recurringOrderId)
	{
		return "{\r\n" + 
				"    \"subscriptionId\": \""+subscriptionId+"\",\r\n" + 
				"    \"recurringOrderId\": \""+recurringOrderId+"\"\r\n" + 
				"}";
	}
	
	public static String getOrderId(String orderId)
	{
		return "{\r\n" + 
				"  \"orderId\": \""+orderId+"\",\r\n" + 
				"   \"cancelReason\": \"Other\"\r\n" + 
				"}";
	}
	public static String getmodifyOrderId(String orderId)
	{
		return "{\r\n" + 
				"  \"orderId\": \""+orderId+"\"\r\n" + 
				"}";
	}
	
	public static String updateRDDetailsCustomer_WCS(String remoteDeliveryAddress, String deliveryInstruction, String remoteDeliveryPartnerPaymentAccount, String baggingOption, String unattendedType)
	{
		return "{\r\n" + 
				"  \"setRDAddress\": true ,\r\n" + 
				"  \"attributes\": [\r\n" + 
				"    {\r\n" + 
				"     \"name\": \"remoteDeliveryAddress\",\r\n" + 
				"     \"value\": \""+remoteDeliveryAddress+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"deliveryInstruction\",\r\n" + 
				"      \"value\": \""+deliveryInstruction+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"remoteDeliveryPartnerPaymentAccount\",\r\n" + 
				"      \"value\": \""+remoteDeliveryPartnerPaymentAccount+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"baggingOption\",\r\n" + 
				"      \"value\": \""+baggingOption+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"name\": \"unattendedType\",\r\n" + 
				"        \"value\": \""+unattendedType+"\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
	}
	
	
	public static String updateRDDetailsCustomer_PS(String storeId, String remoteDeliveryAddress, String deliveryInstruction, String remoteDeliveryPartnerPaymentAccount, String baggingOption, String unattendedType)
	{
		return "{\r\n" + 
				"  \"setRDAddress\": true,\r\n" + 
				"  \"storeId\": \""+storeId+"\",\r\n" + 
				"  \"attributes\": [\r\n" + 
				"    {\r\n" + 
				"     \"name\": \"remoteDeliveryAddress\",\r\n" + 
				"      \"value\": \""+remoteDeliveryAddress+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"deliveryInstruction\",\r\n" + 
				"      \"value\": \""+deliveryInstruction+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"remoteDeliveryPartnerPaymentAccount\",\r\n" + 
				"      \"value\": \""+remoteDeliveryPartnerPaymentAccount+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"name\": \"baggingOption\",\r\n" + 
				"      \"value\": \""+baggingOption+"\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"     \"name\": \"unattendedType\",\r\n" + 
				"      \"value\": \""+unattendedType+"\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
	}
	
	public static String getSlotByRDLocationDate_WCS(String startDate, String endDate, String windowType) {
        return "{\r\n" +
                "  \"serviceType\": \"RD\",\r\n" +
                "  \"slotsChannel\": 1,\r\n" +
                "  \"windowType\": \""+windowType+"\",\r\n" +
                "  \"startDateTimeUTC\": \""+startDate+"T00:00:00\",\r\n" +
                "  \"endDateTimeUTC\": \""+endDate+"T23:59:59\"\r\n" +
                "}";
    }
    public static Response  getSlotByRDLocationDate_Private_APIM(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String Subscription_Key, String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            baseURI = BaseURI.APIM.getBaseURI();
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYRDLOCATION_PRIVATE_APIM.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("Ocp-Apim-Subscription-Key", Subscription_Key);
        restLibrary.addHeader("authorization", authorization);
        restLibrary.addHeader("userauthorization", userauthorization);
        restLibrary.addBody(PS_Payload.getSlotByRDLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
    public static Response  getSlotByRDLocationDate_Public_APIM(String baseURI, String basePath, String requestType, String authorization, String Subscription_Key, String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            baseURI = BaseURI.APIM.getBaseURI();
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYRDLOCATION_PUBLIC_APIM.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("Ocp-Apim-Subscription-Key", Subscription_Key);
        restLibrary.addHeader("authorization", authorization);       
        restLibrary.addBody(PS_Payload.getSlotByRDLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
    public static Response  getSlotByRDLocationDate_Private_WCS(String baseURI, String basePath, String requestType, String accessToken, String userJWTToken,  String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            baseURI = BaseURI.WCS.getBaseURI();
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYRDLOCATION_PRIVATE_WCS.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("user-jwt-token", userJWTToken);
        restLibrary.addHeader("Access-Token", accessToken);
        restLibrary.addPathParameter("storeId", storeId);
        restLibrary.addPathParameter("collectionPointId", collectionPointId);
        restLibrary.addBody(getSlotByRDLocationDate_WCS(startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
    public static Response  getSlotByRDLocationDate_Public_WCS(String baseURI, String basePath, String requestType, String accessToken,  String storeId, String collectionPointId, String startDate, String endDate, String windowType) {
        if(baseURI.equals("")) {
            baseURI = BaseURI.WCS.getBaseURI();
        }
        if(basePath.equals("")) {
            basePath = BasePath.GET_SLOT_BYRDLOCATION_PUBLIC_WCS.getBasePath();
        }
        if(requestType.equals("")) {
            requestType = HttpRequestMethods.POST.name();
        }

        RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
        restLibrary.addHeader("Access-Token", accessToken);
        restLibrary.addPathParameter("storeId", storeId);
        restLibrary.addPathParameter("collectionPointId", collectionPointId);
        restLibrary.addBody(PS_Payload.getSlotByRDLocationDate(storeId, collectionPointId, startDate, endDate, windowType));
        return restLibrary.executeAPI();
    }
	
}
