package com.rest.api;

import com.rest.body.Payload;
import com.rest.main.RestLibrary;

import io.restassured.response.Response;

public class APIM {
	
	
	public static Response  updateRDCustomerDetails(String baseURI, String basePath, String requestType, String authorization, String userauthorization, String ocpApimSubscriptionKey, String storeId, String ver, String remoteDeliveryAddress, String deliveryInstruction, String remoteDeliveryPartnerPaymentAccount, String baggingOption, String unattendedType) {
		if(baseURI.equals("")) {
			baseURI = BaseURI.APIM.getBaseURI();
		}
		if(basePath.equals("")) {
			basePath = BasePath.CART_ATTRIBUTE_APIM.getBasePath();
		}
		if(requestType.equals("")) {
			requestType = HttpRequestMethods.POST.name();
		}
		
		RestLibrary restLibrary = new RestLibrary(baseURI, basePath, requestType);
		restLibrary.addQueryParameter("ver", ver);
		restLibrary.addHeader("authorization", authorization);
		restLibrary.addHeader("userauthorization", userauthorization);
		restLibrary.addHeader("Ocp-Apim-Subscription-Key", ocpApimSubscriptionKey);
		restLibrary.addBody(Payload.updateRDDetailsCustomer_PS(storeId, remoteDeliveryAddress, deliveryInstruction, remoteDeliveryPartnerPaymentAccount, baggingOption, unattendedType));
		return restLibrary.executeAPI();
	}

}
