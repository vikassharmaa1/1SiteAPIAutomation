package com.rest.api;

public enum BasePath {
	
	LOCALIZATION_BYADDRESSID("/localisation/address/id"),
	LOCALIZATION_BYLOCATIONID("/localisation/location/id"),
	LOCALIZATION_BYSUBURBPOSTCODE("/localisation/address"),
	LOCALIZATION_BYFULLADDRESS("/localisation/address"),
	ADD_UPDATE_TROLLEY("/cart/items"),
	ORDER_SUMMARY_LIGHTWEIGHT("/cart/summary/lightweight"),
	ORDER_SUMMARY_MEDIUMWEIGHT("/cart/summary/mediumweight"),
	ORDER_SUMMARY_FULLWEIGHT("/cart/summary/fullweight"),
	GET_SLOT_BYLOCATION("/slots/collection/details/self"),
	GET_SLOT_BYADDRESS("/slots/delivery/address/self"),
	RESERVE_SLOT("/slots/reserve"),
	CART_ATTRIBUTE("/cart/attributes"),
	CART_ATTRIBUTE_APIM("/col/ecommerce/v1/cart/attributes"),
	PROMOTION("/promotion"),
	ORDER_PROCESS("/cart/process"),
	PAYMENT_BY_CARD("/order/payment/method"),
	DELETE_PAYMENT("/order/payment/method"),
	ORDER_SUBMIT("/cart/submit"),
	SYNC_VALIDATE("/cart/sync?storeId={storeId}"),
	APPLY_PROMOCODE("/applyPromotionCode"),
	REMOVE_PROMOCODE("/deletepromotioncode"),
	BAG_ESTIMATOR("/cart/bags"),
	UPDATE_RD_DETAILS_WCS("/wcs/resources/store/{storeId}/cart/cartAttributes"),
	GET_SLOT_BYRDLOCATION_PRIVATE_WCS("/wcs/resources/store/{storeId}/slots/collection/{collectionPointId}/details/self"),
    GET_SLOT_BYRDLOCATION_PUBLIC_WCS("/wcs/resources/store/{storeId}/slots/collection/{collectionPointId}/details"),
    GET_SLOT_BYRDLOCATION_PRIVATE_APIM("/col/ecommerce/v1/slots/collection/details/self"),
    GET_SLOT_BYRDLOCATION_PUBLIC_APIM("/col/ecommerce/v1/slots/collection/details"),
    GET_SLOT_BYLOCATION_PUBLIC("/slots/collection/details");
	
	
	private String method;

	BasePath(String method) {
		this.method = method;
	}
	
	public String getBasePath() {
		return this.method;
	}

}
