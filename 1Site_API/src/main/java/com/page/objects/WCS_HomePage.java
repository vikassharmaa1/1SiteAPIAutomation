package com.page.objects;

import com.rest.main.FunLibrary;

public class WCS_HomePage extends FunLibrary {

	public WCS_HomePage clickWelcomeBackOKButton() {
		ExplicitWait("AfterLogin_Popup_StartShopping_Button", 25);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clickElementByXpath("AfterLogin_Popup_StartShopping_Button");
		return this;
	}

	

	public WCS_MyAccountPage clickMyAccount() {
		ExplicitWait("Account_Name", 20);
		clickElementByXpath("Account_Name");
		return new WCS_MyAccountPage();
	}

}
