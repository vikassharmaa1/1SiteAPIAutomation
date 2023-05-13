package com.page.objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rest.main.FunLibrary;

public class WCS_MyAddressesPage extends FunLibrary {

	public WCS_MyAddressesPage clickAddNewADdress() {

		ExplicitWait("AddNewAddress", 20);
		clickElementByXpath("AddNewAddress");
		return this;
	}

	public WCS_MyAddressesPage editAddress(String addressName) {
		List<WebElement> el = driver.findElements(By.xpath("//div[@class='address-details-wrapper']/h2"));
		Boolean elClicked = false;
		for (int i = 1; i <= el.size(); i++) {

			String addName = driver.findElement(By.xpath("(//div[@class='address-details-wrapper']/h2)[" + i + "]")).getText();

			if(addName.equals(addressName)) {

				String editLink = "(//button[@class='edit-link has-icon'][contains(@data-ng-click,'editAddress')])[" + i + "]";
				clickElementByXpath(editLink);
				elClicked = true;
				break;
			}
		}
		if(!elClicked) {
			assertCheck("editAddress", "Address " +addressName + "not found");
		}
		return this;
	}

	public WCS_MyAddressesPage editFirstAddress() {

		String editLink = "(//button[@class='edit-link has-icon'][contains(@data-ng-click,'editAddress')])[1]";
		clickElementByXpath(editLink);

		return this;
	}

	public WCS_MyAddressesPage deleteAddress(String addressName) {

		List<WebElement> el = driver.findElements(By.xpath("//div[@class='address-details-wrapper']/h2"));
		for (int i = 1; i <= el.size(); i++) {

			String addName = driver.findElement(By.xpath("(//div[@class='address-details-wrapper']/h2)[" + i + "]")).getText();

			if(addName.equals(addressName)) {

				String Address_DeleteLink = "(//button[@class='edit-link has-icon'][contains(@data-ng-click,'deleteAddress')])[" + i + "]";
				clickElementByXpath(Address_DeleteLink);
			}
		}
		return this;
	}

	public WCS_MyAddressesPage deleteAddress_ConfirmButton() {
		ExplicitWait("Address_DeleteThisAddressButton", 10);
		clickElementByXpath("Address_DeleteThisAddressButton");
		return this;
	}

	public WCS_MyAddressesPage enterStreetAddress(String address) {
		clearText("Address_StreetAddress");
		sendKeys("Address_StreetAddress", address);
		return this;
	}

	public WCS_MyAddressesPage enterSuburb(String suburb) {
		clearText("Suburb_Input");
		sendKeys("Suburb_Input", suburb);
		return this;
	}

	public WCS_MyAddressesPage enterPostCode(String postcode) {
		clearText("Postcode_Input");
		sendKeys("Postcode_Input", postcode);
		return this;
	}

	public WCS_MyAddressesPage selectState(String state) {
		Select state_dropdown = new Select(driver.findElement(By.id("state-selection")));
		state_dropdown.selectByValue("NT");
		return this;
	}

	public WCS_MyAddressesPage enterNickName(String nickname) {
		clearText("Registration_Address_Nickname_Text");
		sendKeys("Registration_Address_Nickname_Text", nickname);
		return this;
	}

	public WCS_MyAddressesPage enterFirstName(String firstName) {
		ExplicitWait("Address_First_Name", 10);
		clearText("Address_First_Name");
		sendKeys("Address_First_Name", firstName);
		return this;
	}

	public WCS_MyAddressesPage enterLastName(String lastName) {
		clearText("Address_Last_Name");
		sendKeys("Address_Last_Name", lastName);
		return this;
	}

	public WCS_MyAddressesPage enterEmailID(String emailID) {
		clearText("Address_EmailID");
		sendKeys("Address_EmailID", emailID);
		return this;
	}

	public WCS_MyAddressesPage enterMobileNumber(String mobileNo) {
		clearText("Address_MobileNo");
		sendKeys("Address_MobileNo", mobileNo);
		return this;
	}

	public WCS_MyAddressesPage selectFirstAddress() {
		ExplicitWait("Address_FirstAddress", 10);
		clickElementByXpath("Address_FirstAddress");
		return this;
	}

	public WCS_MyAddressesPage verifyAddress() {
		ExplicitWait("VerifyAddress", 10);
		clickElementByXpath("VerifyAddress");
		return this;
	}

	public WCS_MyAddressesPage clickSaveChanges() {
		clickElementByXpath("Name_Contact_Details_SaveChanges_Button");
		return this;
	}

	public String getStreetAddressLine() {
		ExplicitWait("Address_StreetAddress", 10);
		return getElementTextValue("Address_StreetAddress");
		}
	
	public String getSuburb() {
		return getElementTextValue("Suburb_Input");
		}
	
	public String getPostcode() {
		return getElementTextValue("Postcode_Input");
		}
	
	public String getState() {
		return getElementTextValue("State_Dropdown");
		}
	
	public String getNickName() {
		return getElementTextValue("Address_Nickname");
		}
}
