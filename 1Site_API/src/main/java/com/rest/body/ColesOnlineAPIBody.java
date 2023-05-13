package com.rest.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColesOnlineAPIBody {

	// Adding the json body for login request
	public Map<Object, Object> userLoginBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("email", "automation01api@mailinator.com");
		jsonbody.put("logonPassword", "passw0rd");
		jsonbody.put("rememberMe", true);
		jsonbody.put("storeFrontUser", "true");
		jsonbody.put("storeId", "20601");
		return jsonbody;
	}

	public Map<Object, Object> userLoginBody(String emailid) {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("email", emailid);
		jsonbody.put("logonPassword", "passw0rd");
		jsonbody.put("rememberMe", true);
		jsonbody.put("storeFrontUser", "true");
		jsonbody.put("storeId", "20601");
		return jsonbody;
	}

	// Adding the json body for creating new address
	public Map<Object, Object> addNewAddress(String nickname, String emailid) {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		ArrayList<Object> addressLineArray = new ArrayList<Object>();
		jsonbody.put("nickName", nickname);
		jsonbody.put("firstName", "APITest");
		jsonbody.put("lastName", "User");
		jsonbody.put("addressLine", addressLineArray);
		addressLineArray.add("20 Bayview Bvd");
		jsonbody.put("city", "BAYVIEW");
		jsonbody.put("state", "NT");
		jsonbody.put("country", "AU");
		jsonbody.put("zipCode", "0820");
		jsonbody.put("email1", emailid);
		jsonbody.put("xcont_latitude", "-12.4407971196293");
		jsonbody.put("xcont_longitude", "130.855541709509");
		jsonbody.put("xcont_geoprecision", "0");
		jsonbody.put("xcont_gnafpid", "GANT_703855011");
		jsonbody.put("phone1", "0456789123");
		jsonbody.put("phone1Type", "MPN");
		return jsonbody;
	}

	public Map<Object, Object> createProfileBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("fistname", "");
		jsonbody.put("lastname", "");
		jsonbody.put("email", "");
		jsonbody.put("phone", "");
		jsonbody.put("ccpprofileid", "");
		jsonbody.put("DOB", "");
		jsonbody.put("MFA", "");
		return jsonbody;
	}

	public Map<Object, Object> partNumberBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("partNumber", "3034348");
		jsonbody.put("qty", "2");
		jsonbody.put("orderItemId", "4703335626");
		return jsonbody;

	}

	public Map<Object, Object> psPartNumberBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", "");
		jsonbody.put("partNumber", "3034348");
		jsonbody.put("qty", "2");
		jsonbody.put("orderItemId", "4703335626");
		return jsonbody;

	}

	public Map<Object, Object> localisationBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", "");
		jsonbody.put("colAddressId", "");
		return jsonbody;

	}

	public Map<Object, Object> getBody() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", "20503");
		jsonbody.put("email", "kiranch202@gmail.com");
		return jsonbody;

	}

	public Map<Object, Object> parameterismissing() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", "");
		jsonbody.put("email", "anuliqemail@getnada.com");
		return jsonbody;

	}

	public Map<Object, Object> improperdatatype() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", 20503);
		jsonbody.put("email", "anuliqemail@getnada.com");
		return jsonbody;

	}

	public Map<Object, Object> improperformat() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("storeId", "20503");
		jsonbody.put("email", "anuliqemail@@getnada.com");
		return jsonbody;

	}

	public Map<Object, Object> updateUSer() {
		Map<Object, Object> jsonbody = new HashMap<Object, Object>();
		jsonbody.put("First name", "");
		jsonbody.put("Last name", "");
		jsonbody.put("Email", "");
		jsonbody.put("Phone", "");
		jsonbody.put("CCP Profile ID", "");
		jsonbody.put("DOB", "");
		return jsonbody;

	}

}
