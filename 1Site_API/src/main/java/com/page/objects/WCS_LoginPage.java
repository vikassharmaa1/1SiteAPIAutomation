package com.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rest.main.FunLibrary;

public class WCS_LoginPage extends FunLibrary {

	public WCS_LoginPage clickLoginSignup() {
		ExplicitWait("SB_Login_Signup_Option", 5);
		clickElementByXpath("SB_Login_Signup_Option");
		return this;
	}

	public WCS_LoginPage enterUserName(String username) {
		ExplicitWait("Email_ID", 5);
		sendKeys("Email_ID", username);
		return this;
	}

	public WCS_LoginPage enterLoginPassword(String password) {
		sendKeys("WCSPassword", password);
		return this;
	}

	public WCS_HomePage clickLogin() {
		clickElementByXpath("Login_Button");
		return new WCS_HomePage();
	}

	public WCS_LoginPage clickNewToColesLink() {
		ExplicitWait("New_to_coles_online_link", 2);
		clickElementByXpath("New_to_coles_online_link");
		ExplicitWait("Registration_First_Name_Text", 30);
		return this;
	}

	public WCS_LoginPage enterFirstName(String firstName) {
		clearText("Registration_First_Name_Text");
		sendKeys("Registration_First_Name_Text", firstName);
		return this;
	}
	public WCS_LoginPage enterLastName(String lastName) {
		clearText("Registration_Last_Name_Text");
		sendKeys("Registration_Last_Name_Text", lastName);
		return this;
	}

	public WCS_LoginPage enterEmailID(String username) {
		sendKeys("Registration_Emailid_Text", username);
		return this;
	}

	public WCS_LoginPage enterPassword(String password) {
		clearText("Registration_Password_Text");
		sendKeys("Registration_Password_Text", password);
		return this;
	}

	public WCS_LoginPage ClickContinueButton() {
		clickElementByXpath("Registration_Continue_Button");
		ExplicitWait("Registration_Address_Header", 25);
		return this;
	}

	public WCS_LoginPage enterAddressDetails(String streetAddr, String suburb, String postcode, String nickName, String mobileNo) throws Exception {
		ExplicitWait("Registration_Address_Header", 5);
		clearText("Street_Address_Input");
		sendKeys("Street_Address_Input",streetAddr);
		clearText("Suburb_Input");
		sendKeys("Suburb_Input", suburb);
		clearText("Postcode_Input");
		sendKeys("Postcode_Input", postcode);

		Select state_dropdown = new Select(driver.findElement(By.id("state-selection")));
		state_dropdown.selectByValue("NT");

		clearText("Registration_Address_Nickname_Text");
		sendKeys("Registration_Address_Nickname_Text", nickName);
		clearText("Registration_Mobile_Number_Field");
		sendKeys("Registration_Mobile_Number_Field", mobileNo);
		clickElementByXpath("Registration_Continue_Button_page2");
		Thread.sleep(10000);
		ExplicitWait("Registration_Address_Header",20);
		clickElementByXpath("Registration_Select_Address_HD");
	return this;
	}

}
