package com.rest.body;

import com.rest.main.RestLibrary;

import io.restassured.response.Response;

public class SlotReservationPayload {
	
	public static Response  slotReservation( String AccessToken, String userJWT, String storeId,String slotID,String serviceType,String ccpAddressId,String collectionPointId) {

		String basePath="/wcs/resources/store/{storeId}/slots/reserve";
		RestLibrary restLibrary = new RestLibrary("https://wcssitint.cmltd.net.au:27901", basePath, "Put");
		restLibrary.addHeader("access-token", AccessToken);
		restLibrary.addHeader("user-jwt-token", userJWT);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addBody(wcsReservationPayload(slotID, serviceType, ccpAddressId, collectionPointId));	
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		return response;
		}
	
	public static String wcsReservationPayload(String slotId,String serviceType,String ccpAddressId,String collectionPointId )
	{
		return "{\r\n" + 
				"  \"slotId\": \""+slotId+"\",\r\n" + 
				"  \"serviceType\": \""+serviceType+"\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \""+collectionPointId+"\",\r\n" + 
				"  \"ccpAddressId\": \""+ccpAddressId+"\"\r\n" + 
				"}";
	}
	public static String psReservationPayload(String storeId,String slotId,String serviceType,String ccpAddressId,String collectionPointId )
	{
		return "{\r\n" + 
				"  \"storeId\": \""+storeId+"\",\r\n" + 
				"  \"slotId\": \""+slotId+"\",\r\n" + 
				"  \"serviceType\": \""+serviceType+"\",\r\n" + 
				"  \"shiftId\": \"DM-SHIFT\",\r\n" + 
				"  \"collectionPointId\": \""+collectionPointId+"\",\r\n" + 
				"  \"ccpAddressId\": \""+ccpAddressId+"\"\r\n" + 
				"}";
	}
}



