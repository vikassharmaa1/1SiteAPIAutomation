package com.page.objects;

import org.openqa.selenium.By;

import com.rest.main.FunLibrary;

public class PM_HomePage extends FunLibrary {
	
	public PM_HomePage enterFirstName(String firstName) {
		ExplicitWait("FirstName", 10);
		clearText("FirstName");
		sendKeys("FirstName", firstName);
		return this;
	}
	
	public PM_HomePage enterLastName(String lastName) {
		ExplicitWait("LastName", 10);
		clearText("LastName");
		sendKeys("LastName", lastName);
		return this;
	}
	
	public PM_HomePage editFirstName() {
		clickElementByXpath("FirstName_Edit_Button");
		return this;
	}
	
	public PM_HomePage editLastName() {
		clickElementByXpath("LastName_Edit_Button");
		return this;
	}
	
	public PM_HomePage saveFirstName() {
		clickElementByXpath("FirstName_Edit_Button");
		return this;
	}
	
	public PM_HomePage saveLastName() {
		clickElementByXpath("LastName_Edit_Button");
		return this;
	}
	
	public PM_HomePage enterMobile(String mobileNo) throws Exception {
		clickElementByXpath("EditMobileNo_Link");
		clearText("MobileNo_TextBox");
		sendKeys("MobileNo_TextBox", mobileNo);
		clickElementByXpath("SaveButton");
		Thread.sleep(5000);
		if(driver.findElement(By.xpath("//*[@id='root']//button")).isDisplayed()) {
			clickElementByXpath("skip_Button1");
		} else {
			clickElementByXpath("VerifyLater_Button");
		}
		Thread.sleep(5000);
		return this;
	}
	
	public PM_HomePage enterHomeNumber(String homeNo) {
		clickElementByXpath("EditHomeNo_Link");
		clearText("HomeNo_TextBox");
		sendKeys("HomeNo_TextBox", homeNo);
		clickElementByXpath("SaveButton");
		return this;
	}

	public PM_HomePage deleteMobileNmber() throws InterruptedException {
		clickElementByXpath("EditMobileNo_Link");
		clickElementByXpath("DeleteMobileNumber_Link");
		clickElementByXpath("DeleteMobile_Button");
		Thread.sleep(5000);
		return this;
	}
	
	public PM_HomePage deleteHomeNmber() throws InterruptedException {
		clickElementByXpath("EditHomeNo_Link");
		clearText("HomeNo_TextBox");
		clickElementByXpath("SaveButton");
		Thread.sleep(5000);
		return this;
	}

	public PM_HomePage clickChangePrefernce() {
		clickElementByXpath("ChangePreference_Button");
		return this;
	}

	public PM_HomePage selectSecurityPreference(String SecurityLevel) {
		switch (SecurityLevel.toUpperCase()) {
		case "HIGH":
			clickElementByXpath("HighPereference");
			break;
		case "MEDIUM":
			clickElementByXpath("MediumPreference");

			break;
		case "LOW":
			clickElementByXpath("LowPreference");
			break;
		}
		return this;
	}

	public PM_HomePage clickSavePreference() throws Exception {
		clickElementByXpath("SavePreference_Button");
		Thread.sleep(2000);
		return this;
	}
	
	public String getFirstName() {
		return getElementTextValue("FirstName_Value");
	}
	public String getLastName()  {
		return getElementTextValue("LastName_Value");
	}
	public String getMobileNumber(){
		return getElementTextValue("MobileNo_Value");
	}
	public String getHomeNumber() {
		return getElementTextValue("HomeNo_Value");
	}
	public String getSecurityPreference() {
		if(driver.findElement(By.xpath("//input[@id='radio-input-high']")).isSelected()) {
			return "High";
		}else if(driver.findElement(By.xpath("//input[@id='radio-input-medium']")).isSelected()) {
			return "Medium";
		}else if(driver.findElement(By.xpath("//input[@id='radio-input-low']")).isSelected()) {
			return "Low";
		}
		return "";
	}
}
