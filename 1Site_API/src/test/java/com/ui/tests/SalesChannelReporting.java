package com.ui.tests;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;
import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.utilities.API_DataProvider;
import com.rest.utilities.DatabaseUtilities;

public class SalesChannelReporting extends Base_Class_API implements ITest { 
	
	@Test(dataProvider = "functionalTestData", dataProviderClass = API_DataProvider.class)
	public void validateFunctionalScenarios(String testname) 
	{
		
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String buschnlid = dbUtil.getValues("orders", "buschn_id",  "orders_id='" + FunLibrary.excelData.get("OrderId") +"'");
		dbUtil.closeDBConnection();
		testLog.info("Expected - buschnlid :" + FunLibrary.excelData.get("buschnlid"));
		testLog.info("Actual - buschnlid : " + buschnlid);
		Assert.assertEquals(buschnlid, FunLibrary.excelData.get("buschnlid"), "DMLocationZoneId is not matching");
		funLibrary.Assert.assertAll();
	}

@Override
public String getTestName() {
	return testName.get();
}
}
