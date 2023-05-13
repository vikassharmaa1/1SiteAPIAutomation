package com.page.objects;

import com.rest.main.FunLibrary;

public class WCS_MyAccountPage extends FunLibrary {

	public WCS_MyAddressesPage clickMyAddresses() {
		ExplicitWait("My_Addresses_Link", 10);
		clickElementByXpath("My_Addresses_Link");
		return new WCS_MyAddressesPage();
	}

	public WCS_MyAccountPage clickAccountEdit() {
		ExplicitWait("Account_Details_Edit_Link", 10);
		clickElementByXpath("Account_Details_Edit_Link");
		return this;
	}

	public WCS_MyAccountPage enterFirstName(String name) {
		clearText("Contact_page_First_Name_Text");
		sendKeys("Contact_page_First_Name_Text", name);

		return this;
	}

	public WCS_MyAccountPage enterLastName(String name) {
		clearText("Contact_page_Last_Name_Text");
		sendKeys("Contact_page_Last_Name_Text", name);
		return this;
	}

	public WCS_MyAccountPage clickSaveChanges() {
		clickElementByXpath("Name_Contact_Details_SaveChanges_Button");
		return this;
	}

	public WCS_MyAccountPage clickChangeContactNoLink() {
		clickElementByXpath("ChangeContactNo");
		return this;
	}

	public WCS_MyAccountPage enterHomeNo(String homeNo) {
		clearText("WCS_HomeNo_TextBox");
		sendKeys("WCS_HomeNo_TextBox", homeNo);
		return this;
	}

	public WCS_MyAccountPage enterMobileNo(String mobileNo) {
		clearText("Registration_Mobile_Number_Field");
		sendKeys("Registration_Mobile_Number_Field", mobileNo);
		return this;
	}

	public WCS_MyAccountPage clickChangeSecurityPreference() {
		clickElementByXpath("MyAccount_Change_Your_Password");
		ExplicitWait("Preference_High", 10);
		return this;
	}

	public WCS_MyAccountPage clickSecurityPreference_High() {
		clickElementByXpath("Preference_High");
		return this;
	}

	public WCS_MyAccountPage clickSecurityPreference_Medium() {
		clickElementByXpath("Preference_Medium");
		return this;
	}

	public WCS_MyAccountPage clickSecurityPreference_Low() {
		clickElementByXpath("Preference_Low");
		return this;
	}

	public WCS_MyAccountPage clickSecurityPreference_OK_Button() {
		ExplicitWait("MyAccount_SecurityPreference_Change_Ok_Btn", 10);
		clickElementByXpath("MyAccount_SecurityPreference_Change_Ok_Btn");
		return this;
	}

	public WCS_MyAccountPage clickLogout() {
		ExplicitWait("Logout_Button", 10);
		clickElementByXpath("Logout_Button");
		return this;
	}

}
