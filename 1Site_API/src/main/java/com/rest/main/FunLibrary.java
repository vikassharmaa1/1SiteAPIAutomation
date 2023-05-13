package com.rest.main;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.rest.body.ColesOnlineAPIBody;
import com.rest.utilities.DatabaseUtilities;
import com.rest.utilities.Reporting;
import com.rest.utilities.XLS_Reader;

import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.JsonDiff;

public class FunLibrary extends Base_Class_API {

	public static String WCToken;
	public static String WCTrustedToken;
	public static String storeId;
	public static String catalogId;
	public static String orderItemId;
	public static String allSetCookies;
	public Logger testLog = Logger.getLogger("Log:");
	public XLS_Reader datatable = Base_Class_API.datatable.get();
	public static Map<String, String> excelData = new HashMap<String, String>();
	public SoftAssert Assert = new SoftAssert();
	public static WebDriver driver;
	public static Properties OR_OR;
	JsonNode submitOrderJsonNode;
	JsonNode getOrderJsonNode;

	public FunLibrary() {
		try {
			excelData = datatable.getTestData(sheet_name.get(), currentRow.get());
		} catch (Exception ex) {
			testLog.warn("Error in getting Excel Data.");
			testLog.warn(ex.getLocalizedMessage());
		}
	}

	public FunLibrary(WebDriver driver) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java/conflib/log4j_Rest.properties"));
			PropertyConfigurator.configure(properties);

			OR_OR = new Properties();
			FileInputStream fp = new FileInputStream(
					System.getProperty("user.dir") + "/src/test/resources/browser_xpath.properties");
			OR_OR.load(fp);

		} catch (IOException e) {
			e.printStackTrace();
		}
		FunLibrary.driver = driver;
	}

	/** @return Method to return unique email id - String */
	public String getUniqueEmaild() {
		String str_email;
		str_email = "automation_";
		DateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		Date dateobj = new Date();
		str_email = str_email.concat(df.format(dateobj)).concat("@mailinator.com");
		str_email = str_email.replace("_", "");
		str_email.replace('/', ' ');
		return str_email;
	}

	/** @return Method to return current date in yyyy-MM-dd format - String */
	public String getCurrentDate() {
		Date date = new Date();
		String PresentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		testLog.info("Current date is: " + PresentDate);
		return PresentDate;
	}

	/** @return Method to return future date in yyyy-MM-dd format - String */
	public String getDateAddDays(int days) {
		SimpleDateFormat PresentDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, days); // number of days to add
		String nextDate = (String) (PresentDate.format(c.getTime()));
		return (nextDate);
	}

	public String storeTimeAfterWhitespace(String str) {

		String[] parts = str.split("\\s+");
		String second = parts[1];
		return second;

	}

	public String storeTime(String str) {

		String[] parts = str.split("T");
		String second = parts[1];
		return second;

	}

	public String storeDateBeforeWhitespace(String str) {

		String[] parts = str.split("\\s+");
		String first = parts[0];
		return first;

	}

	public String storeDateBeforeSeperation_T(String str) {

		String[] parts = str.split("T");
		String first = parts[0];
		return first;

	}

	/**
	 * Method to wait for a particular time
	 * 
	 * @param time in seconds
	 */
	public void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Assert.assertTrue(false, "Error during waiting : " + e);
		}
	}

	/** Method to login into coles online website */
	public void login() {
		try {
			testLog.info("Logging in...");
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
					"wcs/resources/store/20510/loginidentity?updateCookies=true", "POST");
			// Executed API with body
			ColesOnlineAPIBody body = new ColesOnlineAPIBody();
			restLibrary.addBody(body.userLoginBody());
			Response response = restLibrary.executeAPI();
			// Validating the response, status code
			restLibrary.getResponseBody(response);
			validateStatusCode(response, "201");
			// Getting WCToken and WCTrustedToken values from response
			JsonPath jsonPathEvaluator = response.jsonPath();
			WCToken = jsonPathEvaluator.getString("WCToken");
			WCTrustedToken = jsonPathEvaluator.getString("WCTrustedToken");
			catalogId = jsonPathEvaluator.getString("catalogId");
			storeId = jsonPathEvaluator.getString("storeId");
			// Getting set cookie values from response
			Headers headers = response.getHeaders();
			List<String> cookies = headers.getValues("Set-Cookie");
			allSetCookies = "";
			for (int i = 0; i < cookies.size(); i++) {
				String cookie = cookies.get(i);
				allSetCookies = allSetCookies + cookie + "; ";
			}
		} catch (Exception e) {
			testLog.error("login failed!!");
			fail("login failed!!");
		}
	}

	/** Method to logout from coles online */
	public void logout() {
		try {
			testLog.info("Logging out");
			RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI,
					"wcs/resources/store/20510/loginidentity/@self?rememberMe=false&storeFrontUser=true&updateCookies=true",
					"DELETE");
			restLibrary.addHeader("WCToken", WCToken);
			restLibrary.addHeader("WCTrustedToken", WCTrustedToken);
			// Executed API
			Response response = restLibrary.executeAPI();
			// Validating the response, status code
			restLibrary.getResponseBody(response);
			validateStatusCode(response, "200");
		} catch (Exception e) {
			testLog.error("logout failed!!");
			fail("logout failed!!");
		}
	}

	/**
	 * Method to compare two Strings. use assertAll explicitly with this method
	 * 
	 * @param
	 */
	public void validate_Equals(String attribute, String expected, String actual) {
		try {
			if (expected.equals(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public void validate_Equals(String attribute, List<String> expected, List<String> actual) {
		try {

			if (actual.containsAll(expected)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public void validate_Equals(String attribute, Boolean expected, Boolean actual) {
		try {
			if (expected.equals(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public void validate_Equals(String attribute, Collection<?> expected, Collection<?> actual) {
		try {
			if (actual.containsAll(expected)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public void checkNull(String attribute, String actual) {
		if (actual == null) {
			testLog.info(attribute + " Expected: null");
			testLog.info(attribute + " Actual:" + actual);
			Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
		} else {
			testLog.error("******Validation FAIL!!******");
			testLog.error(attribute + " Expected: null");
			testLog.error(attribute + " Actual:" + actual);
			Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
			Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
			Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", "null"));
			Assert.assertNull(actual, "Attribute Name:" + attribute + " is not null");
		}
	}

	/**
	 * Method to compare two Integers. use assertAll explicitly with this method
	 * 
	 * @param
	 */
	public void validate_Equals(String attribute, Integer expected, Integer actual) {
		try {
			if (expected.equals(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	/**
	 * Method to validate that Actual String contains the Expected String.
	 * 
	 * @param
	 */
	public void validate_Contains(String attribute, String expected, String actual) {
		try {
			if (expected.contains(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	/**
	 * Method to validate that Actual String contains the Expected String.
	 * 
	 * @param
	 */
	public void validate_Contains(String attribute, List<String> expected, String actual) {
		try {
			if (expected.contains(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format(attribute + ": Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public String getJsonPathValue(Response response, String jsonPath) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		if (jsonPathEvaluator.getString(jsonPath) == null) {
			return "";
		} else {
			return jsonPathEvaluator.getString(jsonPath);
		}
	}

	/**
	 * Method to validate that value of the key identified by the JSON Path is
	 * equals to the expected value.
	 * 
	 * @param
	 */
	public void validateJSONPathValue_Equals(Response response, String jsonPath, String expected) {
		try {
			String jsonPathValue = getJsonPathValue(response, jsonPath);

			jsonPathValue = java.net.URLDecoder.decode(jsonPathValue, "UTF-8");
			expected = java.net.URLDecoder.decode(expected, "UTF-8");

			if (jsonPathValue.equalsIgnoreCase(expected)) {
				testLog.info("Expected - " + jsonPath + " : " + expected);
				testLog.info("Actual   - " + jsonPath + " : " + jsonPathValue);
				Reporting.test.get().log(Status.INFO, String.format("Validation :" + jsonPath + " = %s", expected));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error("Expected - " + jsonPath + " : " + expected);
				testLog.error("Actual   - " + jsonPath + " : " + jsonPathValue);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Expected - " + jsonPath + " = %s", expected));
				Reporting.test.get().log(Status.ERROR, String.format("Actual - " + jsonPath + " = %s", jsonPathValue));
				Assert.assertEquals(jsonPathValue, expected);
			}
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {

			testLog.error("*****Error!! : Invalid JSONPath*****");
			Reporting.test.get().log(Status.ERROR, String.format("Invalid Json Path : %s", jsonPath));
			Assert.fail("invalid json path", e);

		}
	}

	/**
	 * Method to validate that response contains expected result
	 * 
	 * @param
	 */
	public void validateJSONPathValue_Contains(Response response, String jsonPath, String expected) {
		try {
			String jsonPathValue = getJsonPathValue(response, jsonPath);
			jsonPathValue = java.net.URLDecoder.decode(jsonPathValue, "UTF-8");
			expected = java.net.URLDecoder.decode(expected, "UTF-8");

			// if(jsonPathValue.contains(expected)) {
			if ((jsonPathValue.contains(expected)) || (expected.contains(jsonPathValue))) {
				testLog.info("Actual   - " + jsonPath + " : " + jsonPathValue);
				testLog.info("Expected - " + jsonPath + " : " + expected);
				testLog.info("Actual Result contains the Expected result");
				Reporting.test.get().log(Status.INFO,
						String.format("Validation :" + jsonPath + " = %s", jsonPathValue));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error("Expected - " + jsonPath + " : " + expected);
				testLog.error("Actual   - " + jsonPath + " : " + jsonPathValue);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Expected - " + jsonPath + " = %s", expected));
				Reporting.test.get().log(Status.ERROR, String.format("Actual - " + jsonPath + " = %s", jsonPathValue));
				Assert.assertEquals(jsonPathValue, expected);
			}
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {

			testLog.error("*****Error!! : Invalid JSONPath*****");
			Reporting.test.get().log(Status.ERROR, "******Invalid JSON Path******");
			Assert.fail("invalid json path", e);

		}
	}

	public void validate_Equals(String attribute, Object expected, Object actual) {
		try {
			if (expected.equals(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	public void validate_Equals(String attribute, Double expected, Double actual) {
		try {
			if (expected.equals(actual)) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);
			}
		} catch (Exception e) {
			if (expected == null && actual == null) {
				testLog.info(attribute + " Expected:" + expected);
				testLog.info(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error(attribute + " Expected:" + expected);
				testLog.error(attribute + " Actual:" + actual);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
				Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
				Assert.assertEquals(actual, expected, "Attribute Name:" + attribute);

			}
		}
	}

	/**
	 * Method to validate status code
	 * 
	 * @param
	 */
	public void validateStatusCode(Response response, String status) {
		try {
			int statusCode = response.getStatusCode();
			if (statusCode == Integer.parseInt(status)) {
				testLog.info("Expected - Status Code : " + status);
				testLog.info("Actual   - Status Code : " + statusCode);
				Reporting.test.get().log(Status.INFO, String.format("Status Code: %s", statusCode));
			} else {
				testLog.error("Expected - Status Code : " + status);
				testLog.error("Actual   - Status Code : " + statusCode);
				Reporting.test.get().log(Status.ERROR, String.format("Expected - Status Code: %s", status));
				Reporting.test.get().log(Status.ERROR, String.format("Actual   - Status Code: %s", statusCode));
				org.testng.Assert.assertEquals(statusCode, Integer.parseInt(status));
			}
		} catch (Exception e) {
			testLog.error("Exception while getting Statuscode - " + e.getLocalizedMessage());
			Assert.fail("Exception while getting Statuscode ", e);

		}
	}

	/**
	 * Assertion - This method is used where we need to fail test and passing
	 * information like method name etc
	 * 
	 * @param methodname, Error message, Exception
	 */
	public void assertCheck(String methodname, String errormsg, Exception e) {
		Assert.assertEquals(1, 0,
				"MethodName:" + methodname + ", ErrorMsg:" + errormsg + ", Exception:" + e.getLocalizedMessage());
	}

	public void assertCheck(String methodname, String errormsg) {
		Assert.assertEquals(1, 0, "MethodName:" + methodname + ", ErrorMsg:" + errormsg);
	}

	/**
	 * Method to validate status line
	 * 
	 * @param Response
	 * @param Status   Line
	 */
	public void validateStatusLine(Response response, String statusLine) {
		try {
			testLog.info("Status Code Is: " + response.getStatusLine());
			Assert.assertEquals(response.getStatusLine(), statusLine);
		} catch (Exception e) {
			testLog.error("Exception while getting statusLine");
			assertCheck("validateStatusLine", "Exception while getting statusLine", e);
		}
	}

	/**
	 * Method to validate that response is not null
	 * 
	 * @param ValidatableResponse
	 * @param jsonPath
	 */
	public void validate_NotNullValue(ValidatableResponse validatableResponse, String jsonPath) {
		try {
			testLog.info("Validating not null value at path : " + jsonPath);
			validatableResponse.body(jsonPath, Matchers.notNullValue());
		} catch (Exception e) {
			testLog.error("Given path has a null value : " + jsonPath);
			assertCheck("validate_NotNullValue", "Has null value", e);
		}
	}

	public void validate_NotNull(String attribute, String object) {

		testLog.info(object + " is not null");
		Assert.assertNotNull(object, object + " is empty");

	}

	public void validate_NotEquals(String attribute, String expected, String actual) {
		if (!(expected.equals(actual))) {
			Reporting.test.get().log(Status.PASS, String.format("Validation :" + attribute + " = %s", actual));
		} else {
			testLog.error("******Validation FAIL!!******");
			testLog.error("Not Equals validation fails for attribute " + attribute);
			testLog.error(attribute + " Actual: " + actual + "is found equal to " + expected);
			Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
			Reporting.test.get().log(Status.ERROR, String.format("Actual - %s", actual));
			Reporting.test.get().log(Status.ERROR, String.format("Expected - %s", expected));
			Assert.assertNotEquals(actual, expected, attribute + " Actual:" + actual + "is found equal to " + expected);
		}
	}

	public void compare_TwoJsonObjects(String firstJsonObject, String secondJsonObject) throws IOException {
		// Creating object mapper class object
		ObjectMapper objectMapper = new ObjectMapper();
		getOrderJsonNode = objectMapper.readTree(firstJsonObject);
		submitOrderJsonNode = objectMapper.readTree(secondJsonObject);
		// Validating API objects are identical or not
		if (getOrderJsonNode.equals(submitOrderJsonNode)) {
			testLog.info("Great!! Both the API objects are identical");
			Reporting.test.get().log(Status.PASS, String.format("Great!! both json objects are identical"));
		} else {
			testLog.error("******JSON Objects compare Validation FAIL!!******");
			testLog.error("Opps!! Both the API's response data is not identical");
			String diffValue = JsonDiff.asJson(getOrderJsonNode, submitOrderJsonNode).toString();
			testLog.info("Difference : " + diffValue);
			Reporting.test.get().log(Status.ERROR, "******Opps!! Both the API's response are NOT Identical!!******");
			Assert.assertTrue(firstJsonObject.equals(secondJsonObject));
		}
	}

	public String generateRandomString(int length) {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = length;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	public void openURL(String url) {
		driver.get(url);
	}

	public void clickElementByXpath(String objectName) {
		try {
			WebElement el;
			if (objectName.startsWith("//") || objectName.startsWith("(//")) {
				el = driver.findElement(By.xpath(objectName));
			} else {
				el = driver.findElement(By.xpath(OR_OR.getProperty(objectName)));
			}
			el.click();
			testLog.info("Element Clicked: " + objectName);
		} catch (Exception e) {
			testLog.error("Not able to click the element: " + objectName);
			testLog.error("Exception: " + e);
			assertTrue(false, "Not able to click element");

		}
	}

	public void sendKeys(String objectName, String key) {
		try {
			driver.findElement(By.xpath(OR_OR.getProperty(objectName))).sendKeys(key);
			testLog.info("Entered value in " + objectName + " :" + key);
		} catch (Exception e) {
			testLog.error("Not able to send keys to the element: " + objectName);
			testLog.error("Exception: " + e);

		}
	}

	public void clearText(String ObjName) {
		try {
			ExplicitWait(ObjName, 10);
			driver.findElement(By.xpath(OR_OR.getProperty(ObjName))).clear();
			testLog.info("Text cleared for " + ObjName);

		} catch (Exception e) {
			testLog.error("Failed to clear text for" + ObjName);
			assertCheck("Clear_Text", "Not able to clear field " + ObjName, e);
		}
	}

	public void ExplicitWait(String ObjName, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR_OR.getProperty(ObjName))));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR_OR.getProperty(ObjName))));
			testLog.info("ExplicitWait Element Present: " + ObjName);
		} catch (Exception e) {
			testLog.error("element not Present " + ObjName);
			assertCheck("ExplicitWait", "Element not Present" + ObjName, e);
		}
	}

	public String getElementTextValue(String ObjName) {
		try {
			ExplicitWait(ObjName, 10);
			return driver.findElement(By.xpath(OR_OR.getProperty(ObjName))).getText();
		} catch (Exception e) {
			testLog.error("element not Present " + ObjName);
			assertCheck("getElementTextValue", "Not able to get Text value" + ObjName, e);
			return "";
		}

	}

	public void validateJsonStructure(Response response, String fileName) {
		MatcherAssert.assertThat(response.getBody().asString(),
				matchesJsonSchema(new File(current_Dir + "/src/test/resources/JsonSchemaFiles/" + fileName + ".json")));
	}

	public String addItemToTrolley(String partNumber, String qty) {

		String body = "{\r\n" + "				\"additionalDataRequired\": false,\r\n"
				+ "			    \"orderItem\": [\r\n" + "			       \r\n" + "			        { \r\n"
				+ "			            \"quantity\": \"" + qty + "\",\r\n" + "			            \"partNumber\": \""
				+ partNumber + "\"\r\n" + "			        }\r\n" + "			    ]\r\n" + "			}";
		String basePath = "/wcs/resources/store/{storeId}/cart/@self/updateOrderItemByPartnumber";

		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "PUT");
		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter(FunLibrary.excelData.get("pathParameterKey"),
				FunLibrary.excelData.get("pathParameterValue"));
		restLibrary.addBody(body);
		Response response = restLibrary.executeAPI();
		return getJsonPathValue(response, "orderItems[0].orderItemId");
	}

	public void localizationByAddressId(String storeId, String addressId) {
		String basePath = "/wcs/resources/store/{storeId}/localisation/byAddressIdentifier/{colAddressId}";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "POST");

		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("colAddressId", addressId);
		restLibrary.getResponseBody(restLibrary.executeAPI());
	}

	public void localizationByPostCodeSuburb(String storeId, String postcode, String suburb) {
		String basePath = "/wcs/resources/store/{storeId}/localisation/byAddress";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "POST");

		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addQueryParameter("postcode", postcode);
		restLibrary.addQueryParameter("suburb", suburb);
		restLibrary.getResponseBody(restLibrary.executeAPI());
	}

	public void localizationByLocationId(String storeId, String locationId) {
		String basePath = "/wcs/resources/store/{storeId}/localisation/byLocationIdentifier/{locationId}";
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, basePath, "POST");

		restLibrary.addHeader(FunLibrary.excelData.get("Header1Key"), FunLibrary.excelData.get("Header1Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2Key"), FunLibrary.excelData.get("Header2Value"));
		restLibrary.addPathParameter("storeId", storeId);
		restLibrary.addPathParameter("locationId", locationId);
		restLibrary.getResponseBody(restLibrary.executeAPI());
	}

	public String getAnyDate(int daysForward, String format) {

		Date date = new Date();
		DateFormat df = new SimpleDateFormat(format);

		if (daysForward == 0) {
			return df.format(date);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, daysForward);
			return df.format(cal.getTime());
		}
	}

	public String getSlotID(String slotType, String delZone, String collectionPoint, int dayForward) {

		DatabaseUtilities dbUtil_DM = new DatabaseUtilities("DM");
		String slotId = "";

		if (slotType.equalsIgnoreCase("HD")) {

			slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
					"WINDOW_START_TIME like '%" + getAnyDate(dayForward, "yyyy-MM-dd")
							+ "%' and STATUS = 'OPEN' and DELZONE_NAME = '" + delZone + "' and service_type = 'HD'");

		} else if (slotType.equalsIgnoreCase("CC")) {
			slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
					"WINDOW_START_TIME like '%" + getAnyDate(dayForward, "yyyy-MM-dd")
							+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME = '" + collectionPoint
							+ "' and service_type = 'CC'");
		} else if (slotType.equalsIgnoreCase("RD")) {
			slotId = dbUtil_DM.getValues("DELWINDOW", "ID",
					"WINDOW_START_TIME like '%" + getAnyDate(dayForward, "yyyy-MM-dd")
							+ "%' and STATUS = 'OPEN' and COLLECTIONPOINT_NAME = '" + collectionPoint
							+ "' and service_type = 'RD'");
		}

		dbUtil_DM.closeDBConnection();

		return slotId;
	}

	public static String getUserID(String user) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String addressId = dbUtil.getValues("USERS", "USERS_ID", "field1='" + user.toLowerCase() + "'");
		dbUtil.closeDBConnection();
		return addressId;
	}

	public static String getAddressID(String user, String addressName) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String addressId = dbUtil.getValues("ADDRESS", "ADDRESS_ID",
				"member_id=(select users_id from users where field1='" + user.toLowerCase() + "') " + "and NICKNAME='"
						+ addressName + "' and STATUS='P' and ADDRESSTYPE='SB'");
		dbUtil.closeDBConnection();
		return addressId;
	}

	public static String getCCPAddressID(String user, String addressName) {
		String addressId = getAddressID(user, addressName);
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String ccpAddressId = dbUtil.getValues("XADDRESS", "CCPADDRESSID",
				"member_id = (select users_id from users where field1='" + user.toLowerCase() + "') and ADDRESS_ID='"
						+ addressId + "'");
		dbUtil.closeDBConnection();
		return ccpAddressId;
	}

	public static String getCurrentOrdersID(String user) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String orders_id = dbUtil.getValues("ORDERS", "ORDERS_ID",
				"member_id=(select users_id from users where field1='" + user.toLowerCase() + "') and STATUS = 'P' ");
		dbUtil.closeDBConnection();
		return orders_id;
	}

	public static String getOrderItemID(String user, String partNumber) {
		String orderId = getCurrentOrdersID(user);
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String orderItemId = dbUtil.getValues("orderitems", "ORDERITEMS_ID",
				"orders_id =" + orderId + " and PARTNUM = '" + partNumber + "'");
		dbUtil.closeDBConnection();
		return orderItemId;
	}
	
	/**
	 * Method to validate that value of the key identified by the first JSON Path is
	 * equals to the second JSON path value.
	 * 
	 * @param
	 */
	public void validateTwoJSONPathValue_Equals(Response firstAPIRespnse, String jsonPath1, Response secondAPIRespnse, String jsonPath2) {
		try {
			String jsonPathValue1 = getJsonPathValue(firstAPIRespnse, jsonPath1);
			jsonPathValue1 = java.net.URLDecoder.decode(jsonPathValue1, "UTF-8");
			
			String jsonPathValue2 = getJsonPathValue(firstAPIRespnse, jsonPath2);
			jsonPathValue2 = java.net.URLDecoder.decode(jsonPathValue2, "UTF-8");

			if (jsonPathValue1.equalsIgnoreCase(jsonPathValue2)) {
				testLog.info("First API response value : " + jsonPathValue1);
				testLog.info("Second API response value : " + jsonPathValue2);
				Reporting.test.get().log(Status.INFO, String.format("First API contains :" + jsonPath1 + " = %s", jsonPathValue1));
				Reporting.test.get().log(Status.INFO, String.format("Second API contains :" + jsonPath2 + " = %s", jsonPathValue2));
			} else {
				testLog.error("******Validation FAIL!!******");
				testLog.error("First API response value : " + jsonPathValue1);
				testLog.error("Second API response value : " + jsonPathValue2);
				Reporting.test.get().log(Status.ERROR, "******Validation FAIL!!******");
				Reporting.test.get().log(Status.ERROR, String.format("First API contains :" + jsonPath1 + " = %s", jsonPathValue1));
				Reporting.test.get().log(Status.ERROR, String.format("Second API contains :" + jsonPath2 + " = %s", jsonPathValue2));
				Assert.assertEquals(jsonPathValue1, jsonPathValue2);
			}
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {

			testLog.error("*****Error!! : Invalid JSONPath*****");
			Reporting.test.get().log(Status.ERROR, String.format("Invalid first API Json Path : %s", jsonPath1));
			Reporting.test.get().log(Status.ERROR, String.format("Invalid second API Json Path : %s", jsonPath2));
			Assert.fail("invalid json path", e);

		}
	}
}
