package com.rest.api;

public enum BaseURI {

	PS("https://col-ecommerce-test.azurewebsites.net"),
	WCS("https://wcssitint.cmltd.net.au"),
	APIM("https://test2apigw.cmltd.net.au");
	
	private String method;

	BaseURI(String method) {
		this.method = method;
	}
	
	public String getBaseURI() {
		return this.method;
	}

}
