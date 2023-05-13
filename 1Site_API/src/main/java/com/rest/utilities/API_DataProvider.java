package com.rest.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.testng.annotations.DataProvider;

import com.rest.main.Base_Class_API;

public class API_DataProvider {

	
	 @DataProvider	  
	  public static Object[] valid() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"valid");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] invalid() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"invalid");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] functional() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"Functional");
	         return (testObjArray);
	 }
	
	
	  @DataProvider	  
	  public static Object[] genericTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"Generic");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] functionalTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"Functional");
	         return (testObjArray);
	 }
	  @DataProvider	  
	  public static Object[] flyBuyTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"FlyBuy");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] flyBuyOnlineTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"FlyBuyOnline");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] specialTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"Special");
	         return (testObjArray);
	 }
	  
	  @DataProvider	  
	  public static Object[] specialOnlineTestData() throws Exception{
	 
	         Object[] testObjArray =  Base_Class_API.datatable.get().getTableArray(Base_Class_API.sheet_name.get(),"Special_Online");
	         return (testObjArray);
	 }
	  
	  @DataProvider
	    public Object[] emailIdProvider( ) throws IOException 
	    {   
	        String[] sarray = null;
	        BufferedReader  br = new BufferedReader(new FileReader("C:\\HCL\\1siteapiautomation\\1Site_API\\src\\test\\resources\\TestDataFiles\\emailid.csv"));
	        String s = br.readLine();
	        if (s != null) {
	            // use comma as separator
	           sarray =s.split(",");
	        }  
	       
	        br.close();
	        return (sarray);
	 
	    }
	          

}
