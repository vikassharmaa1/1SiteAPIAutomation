package com.rest.utilities;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.Status;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredRequestFilter implements Filter {
	private static final Logger log = Logger.getLogger("Log:");

	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
		Response response = ctx.next(requestSpec, responseSpec);

		log.info("** API REQUEST **");
		log.info("Request method: " + requestSpec.getMethod());
		log.info("Request URI: " + requestSpec.getURI());
		log.info("Request Params: " + requestSpec.getRequestParams());
		log.info("Query Params: " + requestSpec.getQueryParams());
		log.info("Path Params: " + requestSpec.getPathParams());
		log.info("Form Params: " + requestSpec.getFormParams());
		log.info("Headers: " + requestSpec.getHeaders());
		log.info("Request Body: " + requestSpec.getBody());
		
		log.info("** API RESPONSE **");
		log.info("Status Code: " + response.getStatusCode());
		log.info("Status Line: " + response.getStatusLine());
		log.info("Response Body:");
		response.getBody().prettyPrint();

		log.info("****** API REQUEST END ******");

		Reporting.test.get().log(Status.INFO, String.format("------------------API REQUEST------------------"));
		Reporting.test.get().log(Status.INFO, String.format("Request method: %s", requestSpec.getMethod()));
		Reporting.test.get().log(Status.INFO, String.format("Request URI: %s", requestSpec.getURI()));
		Reporting.test.get().log(Status.INFO, String.format("Request Params: %s", requestSpec.getRequestParams()));
		Reporting.test.get().log(Status.INFO, String.format("Query Params: %s", requestSpec.getQueryParams()));
		Reporting.test.get().log(Status.INFO, String.format("Path Params: %s", requestSpec.getPathParams()));
		Reporting.test.get().log(Status.INFO, String.format("Form Params: %s", requestSpec.getFormParams()));
		Reporting.test.get().log(Status.INFO, String.format("Headers: %s", requestSpec.getHeaders()));
		Reporting.test.get().log(Status.INFO, "Request Body: "+ requestSpec.getBody());

		Reporting.test.get().log(Status.INFO, String.format("------------------API RESPONSE------------------"));
		Reporting.test.get().log(Status.INFO, String.format("Status Code: %s", response.getStatusCode()));
		Reporting.test.get().log(Status.INFO, String.format("Status Line: %s", response.getStatusLine()));
		Reporting.test.get().log(Status.INFO, String.format("Response Body: %s", response.getBody().asString()));
		return response;
	}

}
