package com.rest.body;

public class PS_Payload {
	
		
	public static String LocalizationByAddressId(String storeId, String addressId) {
		return "{ \"storeId\":\"" + storeId + "\",  \r\n" + " \"colAddressId\": \"" + addressId + "\" \r\n" + "}";
		
	}
	
	public static String localizationByLocationId(String storeId, String locationId) {
		return "{\r\n" + "  \"locationId\": \"" + locationId + "\",\r\n" + "  \"storeId\":\"" + storeId + "\"\r\n" + "}";
		
	}

	public static String addUpdateTrolley(String partNumber, String qty) {
		
	return "	{  \"additionalDataRequired\":" + "false" + ",\r\n" + "	 \"orderItem\": [\r\n" + "    {\r\n" + "  \"partNumber\": \"" + partNumber + "\",\r\n" + " \"quantity\": \"" + qty + "\"\r\n"
				+ "				    }\r\n" + " ]\r\n" + "}";
	}
	
	public static String bagEstimator(String storeId) {
		
		return "{\r\n" + 
				"    \"storeId\":\""+storeId+"\"\r\n" + 
				"\r\n" + 
				"}";
		}
	
	public static String updateTrolley(String storeId, String additionalFields, String orderItemId, String qty, String partNumber) {
		
		return  "{  \"storeId\":\"" + storeId + "\",\r\n\"additionalDataRequired\":" + additionalFields + ",\r\n" + "			\"orderItem\": [\r\n" + "				    {\r\n" + "				      \"partNumber\": \"" + partNumber + "\",\r\n" + "				      \"quantity\": \""
				+ qty + "\",\r\n" + "					  \"orderItemId\": \"" + orderItemId + "\" \r\n" + "				    }\r\n" + "				  ]\r\n" + "				}";

	}
	
	public static String getSlotByLocation(String storeId, String collectionPointId,String daysForward, String daysSpan, String windowType) {
		return "{\r\n  \"storeId\": \""+storeId+"\",\r\n  \"collectionPointId\": \""+collectionPointId+"\",\r\n  \"daysForward\": "+daysForward+",\r\n  \"daysSpan\": "+daysSpan+",\r\n  \"sortBy\": \"1\",\r\n  \"slotsChannel\": 1,\r\n  \"windowType\": \""+windowType+"\"\r\n}";
	}
	
	public static String getSlotByLocationDate(String storeId, String collectionPointId,String startDate, String endDate, String windowType) {
		return "{\r\n" + 
				"  \"storeId\": \""+storeId+"\",\r\n" + 
				"  \"collectionPointId\": \""+collectionPointId+"\",\r\n" + 
				"  \"sortBy\": \"1\",\r\n" + 
				"  \"slotsChannel\": 1,\r\n" + 
				"  \"windowType\": \""+windowType+"\",\r\n" + 
				"  \"startDateTimeUTC\": \""+startDate+"T00:00:00\",\r\n" + 
				"  \"endDateTimeUTC\": \""+endDate+"T23:59:59\"\r\n" + 
				"}";
	}
	
	public static String getSlotByAddress(String storeId, String daysForward, String daysSpan, String suburb, String postcode, String state, String windowType) {
		return "{\r\n    \"storeId\": \""+storeId+"\",\r\n    \"daysForward\": "+daysForward+",\r\n    \"daysSpan\": "+daysSpan+",\r\n    \"sortBy\": \"1\",\r\n    \"slotsChannel\": 1,\r\n    \"windowType\": \""+windowType+"\",\r\n    \"suburb\": \""+suburb+"\",\r\n    \"postcode\": \""+postcode+"\",\r\n    \"state\": \""+state+"\",\r\n    \"longitude\": \"144.97151\",\r\n    \"latitude\": \"-37.82505\"\r\n}";
	}
	
	public static String reseverSlot(String storeId, String slotId, String serviceType, String shiftId, String collectionPointId, String ccpAddressId) {
		return "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n" + "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\",\r\n" + "    \"collectionPointId\" : \"" + collectionPointId + "\",\r\n"
				+ "    \"ccpAddressId\" : \"" + ccpAddressId + "\"\r\n" + "}\r\n" + "";
		
	}
	
	public static String reseverSlot_NoCCPAddressID(String storeId, String slotId, String serviceType, String shiftId) {
		return "{\r\n" + "    \"storeId\": \"" + storeId + "\",\r\n" + "    \"slotId\" : \"" + slotId + "\",\r\n"
				+ "    \"serviceType\" : \"" + serviceType + "\",\r\n" + "    \"shiftId\" : \"" + shiftId + "\"\r\n"
				 +"}\r\n" + "";
		
	}
	
	
	
	public static String cartAttribute(String storeId, String baggingOption, String unattendedType, String delInstructions, String SubtitueOption) {
		return "{\r\n" + 
				"\"storeId\":\""+storeId+"\",\r\n" + 
				"\"attributes\": [\r\n" + 
				"{\r\n" + 
				"\"name\": \"baggingOption\",\r\n" + 
				"\"value\": \""+baggingOption+"\"\r\n" + 
				"}]}";
				
				/*,\r\n" + 
				"{\"name\":\"unattendedType\",\r\n" + 
				"\"value\": \""+unattendedType+"\"\r\n" + 
				"},\r\n" + 
				"{\"name\":\"deliveryInstruction\",\r\n" + 
				"\"value\": \""+delInstructions+"\"\r\n" + 
				"},\r\n" + 
				"{\r\n" + 
				"    \"name\": \"substitutionOption\",\r\n" + 
				"    \"value\": \""+SubtitueOption+"\"\r\n" + 
				"}\r\n" + 
				"]\r\n" + 
				"}";*/
	}
	
	public static String applyPromoCode(String promoCode) {
		
			  return "{\r\n" + 
					 "  \"promoCode\": \""+promoCode+"\"\r\n" + 
			  		"\r\n" + 
			  		"}";
	}
	
	
	public static String getMPGSCardToken(String cardNo, String month, String year, String cvv)
	{
		return "{\r\n" + "    \"sourceOfFunds\": {\r\n" + "        \"type\": \"CARD\",\r\n" + "        \"provided\": {\r\n" + "            \"card\": {\r\n" + "                \"expiry\": {\r\n" + "                    \"month\": \"" + month + "\",\r\n" + "                    \"year\": \""
				+ year + "\"\r\n" + "                },\r\n" + "                \"number\": \"" + cardNo + "\",\r\n" + "                \"securityCode\": \"" + cvv + "\"\r\n" + "            }\r\n" + "        }\r\n" + "    }\r\n" + "}";
	}
	
	
	public static String savePaymentMethod_Card(String storeId, String name, String saveToProfile, String identifier) 
	{
	return	"{\r\n" + "  \"storeId\": \"" + storeId + "\",\r\n" + "  \"name\": \"" + name + "\",\r\n" + "  \"identifier\": \"" + identifier + "\",\r\n" + "  \"saveToProfile\": " + saveToProfile + "\r\n" + "}";

	}
	
	public static String orderProcess( String storeId) {
		 return "{\r\n" + "    \"storeId\" : \"" + storeId + "\"\r\n" + "}";
	}
	
	public static String orderSubmit(String salesChannel,String storeId, String flybuysBarcodes, String staffDiscountNums) {
  	return	"{\r\n" + "    \"salesChannel\" : \"" + salesChannel + "\",\r\n" + "    \"flybuysBarcode\" : \"" + flybuysBarcodes + "\",\r\n" + "    \"staffDiscountNum\" : \"" + staffDiscountNums + "\",\r\n" + "      \"storeId\"  : \"" + storeId + "\"\r\n" + " }";

	}
	
	public static String getSlotByRDLocationDate(String storeId, String collectionPointId,String startDate, String endDate, String windowType) {
        return "{\r\n" +
                "  \"storeId\": \""+storeId+"\",\r\n" +
                "  \"collectionPointId\": \""+collectionPointId+"\",\r\n" +
                "  \"serviceType\": \"RD\",\r\n" +
                "  \"sortBy\": \"1\",\r\n" +
                "  \"slotsChannel\": 1,\r\n" +
                "  \"windowType\": \""+windowType+"\",\r\n" +
                "  \"startDateTimeUTC\": \""+startDate+"T00:00:00\",\r\n" +
                "  \"endDateTimeUTC\": \""+endDate+"T23:59:59\"\r\n" +
                "}";
    }
}
