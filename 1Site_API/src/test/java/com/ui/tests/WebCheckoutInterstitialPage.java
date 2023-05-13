package com.ui.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WebCheckoutInterstitialPage {
	WebDriver driver = null;
	String colToken = "";

	@BeforeMethod()
	public void setup() {

		executeSSOAuthenticationAPI();
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}

	@AfterMethod()
	public void tearDown() {
		driver.close();
		driver.quit();
	}

	@Test(description = "Validate that webcheckout")
	public void validURL_WebCheckout() throws Exception {
		driver.get(
				"https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=checkout&channel=sia&col-token="
						+ colToken + "&cid=1234567890&adobe_mc=12345abcde67890#qwert");
		Thread.sleep(1000);
		String url = driver.getCurrentUrl();
		URL aURL = new URL(url);
		String actual = aURL.getPath();
		Assert.assertEquals("/a/casuarina/checkout", actual);

	}

	@Test(description = "Validate that system do not naviagte to colesonline when url is invalid")
	public void inValidURL_MissingDestination() throws Exception {
		driver.get(
				"https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=checkout&channel=sia&col-token=&cid=1234567890");
		Thread.sleep(1000);
		Assert.assertEquals("Something went wrong",driver.findElement(By.xpath("//*[@class='error-heading']")).getText().trim());
	}
	
	public void executeSSOAuthenticationAPI() {

		Map<String, String> header = new HashMap<String, String>();
		header.put("channel", "sia");
		header.put("user-jwt-token",
				"eyJhbGciOiJSUzI1NiIsImtpZCI6ImZOYjZUODJ6OHhDS09Kd19jMmMwZSIsInR5cGUiOiJqd3QifQ.eyJodHRwczovL2NjcC9wcm9maWxlSWQiOiI2Mjg3NWY5ZC0xMGFjLTQyZGMtYjMxYy0xMTE0Zjc1ZTUiLCJpc3MiOiJodHRwczovL2NvbGVzLXNpdC5hdS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NjI4NzVmOWQtMTBhYy00MmRjLWIzMWMtMTExNGY3NWU1IiwiYXVkIjpbImN1c3RvbWVyLXNlcnZpY2VzIiwiaHR0cHM6Ly9jb2xlcy1zaXQuYXUuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTYyNjMzMDYyOCwiZXhwIjoxNjQxODgyNjI4LCJhenAiOiJuWTY5eGlmNzIyRzRvRzhNcEpZeUhwTnJuMzk5TlcxWCIsInNjb3BlIjoib3BlbmlkIHJlYWQ6cHJvZmlsZSByZWFkOnByZWZlcmVuY2VzIHVwZGF0ZTpwYXNzd29yZCB1cGRhdGU6cHJlZmVyZW5jZXMgdXBkYXRlOnByb2R1Y3QtbGlzdCByZWFkOnByb2R1Y3QtbGlzdCB1cGRhdGU6bG95YWx0eS1hY2NvdW50IHJlYWQ6bG95YWx0eS1hY2NvdW50IG9mZmxpbmVfYWNjZXNzIHJlYWQ6Y29sIHVwZGF0ZTpjb2wgc3NvOmNvbCJ9.bSHNOIY8tHHRnq5ewvwbaLeSCmrfcMoz7cDc10LvO7M9QLNnHM4MdJI_rDX58pkVDcnbbd3sXTvwzE1AYghnYHWV2_XAOYO-nQgyhADCCSk4OrgoA5sdJ6rU8JoEFs9EzQNEerxxkSiABVKhniEE0cAmHVJonj2VJKouWrSvXksGVm1bKS6966kNX9YlKpJ8GZsfGsNNoMrkY7A8TaGMP9EW2Sx-X7znV6Q83dJ9GKsL7Z5YjEH-6CGpmHMe5vwKlw4aHmSnps6Bw_OWQo2xtFSUF4-C7LAX35P4PC7bpWI7JHatZqU1CfVkKMuIYBIzTiymb0brFPn2zDoIMbKBUw");
		header.put("Access-Token", "YZGvsvvE6O2NF126uIBVFvX/L/RQstwN");
		Map<String, String> pathParameter = new HashMap<String, String>();
		pathParameter.put("storeId", "20501");
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeaders(header).addPathParams(pathParameter);
		RequestSpecification reqspec = builder.build();
		RequestSpecification reqspec1 = RestAssured.given().relaxedHTTPSValidation();

		reqspec1.contentType(ContentType.JSON);
		reqspec1.spec(reqspec);
		reqspec1.log().all();
		Response res = reqspec1
				.post("https://wcssitint.cmltd.net.au:27901/wcs/resources/store/{storeId}/authenticate/auth");
		Assert.assertEquals(res.getStatusCode(), 200);
		colToken = res.getBody().jsonPath().get("col-token");

	}

}