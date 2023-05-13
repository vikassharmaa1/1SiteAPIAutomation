package com.rest.main;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.utilities.Reporting;
import com.rest.utilities.RestAssuredRequestFilter;
import com.rest.utilities.XLS_Reader;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestLibrary {
	public Logger testLog = Logger.getLogger("Log:");
	private RequestSpecBuilder builder = new RequestSpecBuilder();
	private String method;
	private String url;
	public XLS_Reader datatable = Base_Class_API.datatable.get();
	public int status;
	public int currentRow = Base_Class_API.currentRow.get();
	public String sheet_name = Base_Class_API.sheet_name.get();
	
	// private RequestSpecBuilder builder;
	 static RestLibrary instance;
	  	   
	public RestLibrary() {
        
    }
	public RestLibrary(String baseURI, String basePath, String method) {
		this.url = baseURI + basePath;
		this.method = method;
		//this.builder = new RequestSpecBuilder();
	}

	/**
	 * ExecuteAPI to execute the API for GET/POST/DELETE *
	 * 
	 * @return ResponseOptions<Response>
	 */
	public Response executeAPI() {
		try {

			RequestSpecification requestSpecification = builder.build();
			RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
			httpRequest.contentType(ContentType.JSON);
			httpRequest.spec(requestSpecification);
			httpRequest.filter(new RestAssuredRequestFilter());
			//httpRequest.log().all();
						
//			testLog.info("Executing API...");
//			if(this.method.equalsIgnoreCase("POST"))
//				return httpRequest.post(this.url);
//			else if(this.method.equalsIgnoreCase("DELETE"))
//				return httpRequest.delete(this.url);
//			else if(this.method.equalsIgnoreCase("PUT"))
//				return httpRequest.put(this.url);
//			else if(this.method.equalsIgnoreCase("GET"))
//				return httpRequest.get(this.url);

//			 RequestSpecification httpRequest = this.builder.build()
//	                    .relaxedHTTPSValidation()
//	                    .contentType(ContentType.JSON);
//	                   // .filter(new RestAssuredRequestFilter());
//	            
	            switch(method.toUpperCase()) {
	                case "POST":
	                    return httpRequest.post(this.url);
	                case "GET":
	                    return httpRequest.get(this.url);
	                case "DELETE":
	                    return httpRequest.delete(this.url);
	                case "PUT":
	                    return httpRequest.put(this.url);
	                case "OPTIONS":
	                    return httpRequest.options(this.url);
	                default:
	                    return null;
	            }	
			
		} catch (Exception e) {
			e.printStackTrace();
			Reporting.test.get().log(Status.ERROR, "Error while executing api");
			Reporting.test.get().log(Status.ERROR, e.getLocalizedMessage());
		}
		return null;
	}

	public void setUrlEncoding(boolean bool) {
		builder.setUrlEncodingEnabled(bool);
	}

	public void setBasicAuthentication(String username, String password) {
		BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
		basicAuthScheme.setUserName(username);
		basicAuthScheme.setPassword(password);
		builder.setAuth(basicAuthScheme);
	}
	
	public void addHeader(String key, String value) {
//		Reporting.test.get().log(Status.INFO, String.format("Header - " + key + ": %s", value));
		builder.addHeader(key, value);
		
	}

	public void addQueryParameter(String key, String value) {
//		Reporting.test.get().log(Status.INFO, String.format("QueryParam - " + key + ": %s", value));
		builder.addQueryParam(key, value);
	}

	public void addPathParameter(String key, String value) {
//		Reporting.test.get().log(Status.INFO, String.format("PathParam - " + key + ": %s", value));
		builder.addPathParam(key, value);
	}

	public void addBody(String body) {
//		Reporting.test.get().log(Status.INFO, String.format("Body : %s", body));
		builder.setBody(body);
	}

	public void addBody(Map<Object, Object> body) {
		builder.setBody(body);
	}

	/**
	 * Method to execute API with body passed from an external file. Need to pass file name.
	 * 
	 * @param filename
	 * @return Response
	 */
	public void addBody(File file) {
		try {
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(file);
//			FileReader reader = new FileReader("C:\\HCL\\APIAUTOMATION\\ColesServices_API\\JsonInputFiles\\" + filename + ".json");
			Object obj = jsonParser.parse(reader);
			String requestBody = obj.toString();
			testLog.info("Adding json body to builder" + requestBody);
			builder.setBody(obj);
		} catch (Exception e) {

		}
	}

	public void addHeader(Map<String, String> headers) {
		builder.addHeaders(headers);
	}

	public void addQueryParameter(Map<String, String> queryParam) {
		builder.addQueryParams(queryParam);
	}

	public void addPathParameter(Map<String, String> pathParam) {
		builder.addPathParams(pathParam);
	}

	public void addFormData(Map<String, String> formdata) {
		builder.addFormParams(formdata);
	}

	/**
	 * Method to check if proper response is coming
	 * 
	 * @param Response
	 */
	public void getResponseBody(Response response) {
		try {
			response.getBody().prettyPrint();
//			Reporting.test.get().log(Status.INFO, String.format("Response: %s", response.getBody().asString()));
		} catch (Exception e) {
			testLog.error("Error while getting response - " + e.toString());
			assertCheck("getResponseBody", "Error while getting response", e);

		}
	}

	public Object getResponseBody(Response response, Class<?> className) {
		try {
			response.getContentType().toString();
			return new ObjectMapper().readValue(response.getBody().asString(), className);

		} catch (Exception e) {
			testLog.error("Error while getting response - " + e.toString());
			assertCheck("getResponseBody", "Error while getting response", e);
		}
		return className;
	}

	/**
	 * Authenticate to get the token variable
	 * 
	 * @param body
	 * @return string token
	 */
	public String getAccessToken(Object jsonBody) {
		builder.setBody(jsonBody);
		// httpRequest.body(jsonBody);
		return executeAPI().getBody().jsonPath().get("access_token");
	}

	public void assertCheck(String methodname, String errormsg, Exception e) {
		Assert.assertEquals(1, 0, "MethodName:" + methodname + ", ErrorMsg:" + errormsg + ", Exception:" + e.getLocalizedMessage());
	}
}
