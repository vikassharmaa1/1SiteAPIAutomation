package com.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.rest.main.FunLibrary;

public class PM_LoginPage extends FunLibrary {

	public PM_LoginPage loginPM(String mailId, String password) throws Exception {
		ExplicitWait("EmailID", 10);
		sendKeys("EmailID", mailId);
		clickElementByXpath("Continue_Button");
		sendKeys("Password", password);
		clickElementByXpath("Login_Buton_PM");
		Thread.sleep(4000);
		return this;
	}
	
	public PM_LoginPage clickLogin() throws Exception {
		clickElementByXpath("Login_Buton_PM");
		Thread.sleep(4000);
		return this;
	}
	
	public PM_LoginPage enterUserID(String mailId) throws Exception {
		ExplicitWait("EmailID", 10);
		sendKeys("EmailID", mailId);
		clickElementByXpath("Continue_Button");
		return this;
	}
	public PM_LoginPage enterPassword(String password) throws Exception {
		ExplicitWait("Password", 10);
		sendKeys("Password", password);
		Thread.sleep(4000);
		return this;
	}
	
	public void registerCCPUser(String mailId, String firstName, String lastName) throws Exception {
		sendKeys("EmailID", mailId);
		clickElementByXpath("Continue_Button");
		sendKeys("FirstName", firstName);
		sendKeys("LastName", lastName);
		sendKeys("Password", "passw0rd");
		sendKeys("ConfirmPassword", "passw0rd");
		Actions action = new Actions(driver);
		action.click(driver.findElement(By.id("terms-conditions"))).build().perform();
		clickElementByXpath("SignUp_Button");
		Thread.sleep(5000);
		clickElementByXpath("Continute_Button2");
	}
	
	public PM_LoginPage clickTermAndCondition(){
		Actions action = new Actions(driver);
		action.click(driver.findElement(By.id("terms-conditions"))).build().perform();
		return this;
	}
public PM_LoginPage clickContinueButton(){
	ExplicitWait("Continue_Button", 5);
	clickElementByXpath("Continue_Button");
	return this;
	}
	
}
