package com.rest.api;

import com.rest.body.Payload;
import com.rest.main.RestLibrary;

import io.restassured.response.Response;

public class WCS {
	
	public static Response  updateRDCustomerDetails(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String ver, String remoteDeliveryAddress, String deliveryInstruction, String remoteDeliveryPartnerPaymentAccount, String baggingOption, String unattendedType) {
		if(baseURI.equals("")) {
			baseURI = BaseURI.WCS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.UPDATE_RD_DETAILS_WCS.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addHeader("Access-Token", authorization);
		restLibrary.addHeader("user-jwt-token", userauthorization);
		restLibrary.addBody(Payload.updateRDDetailsCustomer_WCS(remoteDeliveryAddress, deliveryInstruction, remoteDeliveryPartnerPaymentAccount, baggingOption, unattendedType));
		return restLibrary.executeAPI();
	}
	
	
	public static Response  updateRDCustomerDetailsWithBody(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String storeId, String ver, String body) {
		if(baseURI.equals("")) {
			baseURI = BaseURI.WCS.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.UPDATE_RD_DETAILS_WCS.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addHeader("Access-Token", authorization);
		restLibrary.addHeader("user-jwt-token", userauthorization);
		restLibrary.addBody(body);
		return restLibrary.executeAPI();
	}
	

}
