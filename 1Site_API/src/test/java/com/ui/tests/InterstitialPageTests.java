package com.ui.tests;

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

public class InterstitialPageTests {
	WebDriver driver= null;
	
	@BeforeMethod()
	public void setup(){
		
		executeSSOAuthenticationAPI();
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}
	@AfterMethod()
	public void tearDown(){
		driver.close();
		driver.quit();
	}
	
	@Test(description="Validate that system naviagte to colesonline and user is already logged-in into the application")
	public void validURL_UserLoggedIn() {
		driver.get("https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=home&channel=sia&col-token=fd637640-98b0-3a56-87a3-9869f0109a9f");
		//span[(starts-with(@class,'draw-text')) and (@data-intended-text='accountLinkVM.accountManager.intendedText')]
		driver.findElement(By.xpath("//span[(starts-with(@class,'draw-text')) and (@data-intended-text='accountLinkVM.accountManager.intendedText')]")).click();
	Assert.assertTrue(driver.findElement(By.xpath("	//a[@class='logout']")).isDisplayed());
		
	}
	
	@Test(description="Validate that system do not naviagte to colesonline when url is invalid")
	public void inValidURL_MissingDestination() {
		driver.get("https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=&channel=sia&col-token=fd637640-98b0-3a56-87a3-9869f0109a9f");
		Assert.assertEquals("Something went wrong",driver.findElement(By.xpath("//*[@class='error-heading']")).getText().trim());
	}
	@Test(description="Validate that system do not naviagte to colesonline when url is invalid")
	public void inValidURL_IncorrectDestination() {
		driver.get("https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=abcdef&channel=sia&col-token=fd637640-98b0-3a56-87a3-9869f0109a9f");
		Assert.assertEquals("Something went wrong",driver.findElement(By.xpath("//*[@class='error-heading']")).getText().trim());
	}
	@Test(description="Validate that system do not naviagte to colesonline when url is invalid")
	public void inValidURL_MissingToken() {
		driver.get("https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=home&channel=sia&col-token=");
		Assert.assertEquals("Something went wrong",driver.findElement(By.xpath("//*[@class='error-heading']")).getText().trim());
	}
	@Test(description="Validate that system do not naviagte to colesonline when url is invalid")
	public void inValidURL_IncorrectToken() {
		driver.get("https://wcssitint.cmltd.net.au:27901/online/COLRSSSOAuthenticateView?storeId=20501&dest=home&channel=sia&col-token=fd63764098b03a5687a39869f0109a9f");
		Assert.assertEquals("Something went wrong",driver.findElement(By.xpath("//*[@class='error-heading']")).getText().trim());
	}
	public void executeSSOAuthenticationAPI() {
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("channel","sia");
		header.put("user-jwt-token", "eyJhbGciOiJSUzI1NiIsInR5cGUiOiJqd3QifQ.eyJodHRwczovL2NjcC9wcm9maWxlSWQiOiI3M2RiOTkxOS01NDE2LTQzNTgtYjk0OS02YWM0NDQzYTJjMjMiLCJpc3MiOiJodHRwczovL2NvbGVzLXNpdC5hdS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NzNkYjk5MTktNTQxNi00MzU4LWI5NDktNmFjNDQ0M2EyYzIzIiwiYXVkIjpbImN1c3RvbWVyLXNlcnZpY2VzIiwiaHR0cHM6Ly9jb2xlcy1zaXQuYXUuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTYyMjU0MDQ2NCwiZXhwIjoxNjY1NzQwNDY0LCJhenAiOiJuWTY5eGlmNzIyRzRvRzhNcEpZeUhwTnJuMzk5TlcxWCIsInNjb3BlIjoic2NvcGU9b3BlbmlkIHJlYWQ6ZmlsZSByZWFkOnByZWZlcmVuY2VzIHVwZGF0ZTpwYXNzd29yZCB1cGRhdGU6cHJlZmVyZW5jZXMgdXBkYXRlOnByb2R1Y3QtbGlzdCByZWFkOnByb2R1Y3QtbGlzdCB1cGRhdGU6bG95YWx0eS1hY2NvdW50IHJlYWQ6bG95YWx0eS1hY2NvdW50IG9mZmxpbmVfYWNjZXNzIHJlYWQ6Y29sIHVwZGF0ZTpjb2wifQ.RPZl9hr-IkbncQl33HbTkIRuuYlX-rAWNSG2NMMgNfOo8iAI7WldVJcS-9oJXa19d5MXDSHAg2iuUeKrZDQwYJyqmutREvWUWBMc80Kxdr3dHmhfOqRaEb2-44_3VDaqzsWhzLFfqDrwPBpoUPgyOSrroP_ey000eYZqpxRucdCMa788r2LHVyyuLz_of8Pv4oWfWpNw9aEkhpKbgH40xNPy4sLgLk8RoDSjoV_mxpPm_ovcwSKbvyuGGEyAv5_D1Ap3QHb0zyBqU0OxyU_JUGDjJdEDr1wS4fxTDi5Ei8iyfYfdjLLuE5-snmL4fgZkcTOY5OIsKSORBah7x9IjJQ");
		header.put("Access-Token","YZGvsvvE6O2NF126uIBVFvX/L/RQstwN");
		Map<String, String> pathParameter = new HashMap<String, String>();
		pathParameter.put("storeId", "10503");
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeaders(header).addPathParams(pathParameter);
		RequestSpecification reqspec = builder.build(); 
		RequestSpecification reqspec1= RestAssured.given().relaxedHTTPSValidation();
		
		reqspec1.contentType(ContentType.JSON);
		reqspec1.spec(reqspec);
		reqspec1.log().all();
		Response res = reqspec1.post("https://wcssitint.cmltd.net.au:27901/wcs/resources/store/{storeId}/authenticate/auth");
		Assert.assertEquals(res.getStatusCode(),200);

	}
	
}