package orderFullSummary.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderAttributes {

	
    public String serviceType;
    
    
    
    public int slotMaxItems;
    public String orderDeliveryInstruction;
    public int orderBaggingPreference;
    public String deliveryWindowStartTimeUTC;
    public DeliverySlotBagging deliverySlotBagging;
    public String reservationExpirationTimeLocal;
    public int substitutionOptionSelected;
    public String deliverySlotUnattendedType;
    public String deliveryWindowId;
    public String deliverySlotShift;
    public String deliveryWindowType;
    public String shiftId;
    public String orderFlybuyBarcode;
    public String orderSubServiceType;
    public String slotcutofftimeLocal;
    
    public String reservationExpirationTimeUTC;
    public String deliveryWindowEndTime;

    public String deliveryWindowStartTime;
    public String orderSubServiceTypeDesc;
    public int shipChargeSaving;
    public String deliveryWindowTypeDescription;
    
    public String deliverySlotPaymentOptions;
    
    public String slotcutofftimeUTC;
    public String  deliveryWindowEndTimeUTC;
    
    public String substitutionOptionsAvailable;
    public String zoneLocationId;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String rdAddressId;
    
   
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String remoteDeliveryPartnerPaymentAccount;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String orderRemoteDeliveryAddress;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public double slotShipCharge;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String orderUnattendedAllowed;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public double slotMOVForFreeShipCharge;
    
     
   
	@JsonIgnoreProperties (ignoreUnknown = true)
    public String collectionPointId;
    public String orderFlybuyNumber;
    @JsonIgnoreProperties (ignoreUnknown = true) 
    public boolean ageGateViewed;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public int orderTotalGapToGetFreeShipCharge;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String tokenisedCardNumberOfOrder;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String tokenisedCardExpiryOfOrder;
    public String timeZone;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String tokenisedCardType;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public boolean isColesPlusMember;
    public int deliverySlotBaggingOption;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String orderBagCountForBagless;
    
    @JsonIgnoreProperties (ignoreUnknown = true)
    public ArrayList<Object> deliverySlotRestrictions;
    
    
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getCollectionPointId() {
		return collectionPointId;
	}
	public void setCollectionPointId(String collectionPointId) {
		this.collectionPointId = collectionPointId;
	}
	public String getOrderFlybuyNumber() {
		return orderFlybuyNumber;
	}
	public void setOrderFlybuyNumber(String orderFlybuyNumber) {
		this.orderFlybuyNumber = orderFlybuyNumber;
	}
	public boolean isAgeGateViewed() {
		return ageGateViewed;
	}
	public void setAgeGateViewed(boolean ageGateViewed) {
		this.ageGateViewed = ageGateViewed;
	}
	public int getOrderTotalGapToGetFreeShipCharge() {
		return orderTotalGapToGetFreeShipCharge;
	}
	public void setOrderTotalGapToGetFreeShipCharge(int orderTotalGapToGetFreeShipCharge) {
		this.orderTotalGapToGetFreeShipCharge = orderTotalGapToGetFreeShipCharge;
	}
	public String getTokenisedCardNumberOfOrder() {
		return tokenisedCardNumberOfOrder;
	}
	public void setTokenisedCardNumberOfOrder(String tokenisedCardNumberOfOrder) {
		this.tokenisedCardNumberOfOrder = tokenisedCardNumberOfOrder;
	}
	public String getTokenisedCardExpiryOfOrder() {
		return tokenisedCardExpiryOfOrder;
	}
	public void setTokenisedCardExpiryOfOrder(String tokenisedCardExpiryOfOrder) {
		this.tokenisedCardExpiryOfOrder = tokenisedCardExpiryOfOrder;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getTokenisedCardType() {
		return tokenisedCardType;
	}
	public void setTokenisedCardType(String tokenisedCardType) {
		this.tokenisedCardType = tokenisedCardType;
	}
	public boolean isColesPlusMember() {
		return isColesPlusMember;
	}
	public void setColesPlusMember(boolean isColesPlusMember) {
		this.isColesPlusMember = isColesPlusMember;
	}
	public int getDeliverySlotBaggingOption() {
		return deliverySlotBaggingOption;
	}
	public void setDeliverySlotBaggingOption(int deliverySlotBaggingOption) {
		this.deliverySlotBaggingOption = deliverySlotBaggingOption;
	}
	public String getOrderBagCountForBagless() {
		return orderBagCountForBagless;
	}
	public void setOrderBagCountForBagless(String orderBagCountForBagless) {
		this.orderBagCountForBagless = orderBagCountForBagless;
	}
	public int getSlotMaxItems() {
		return slotMaxItems;
	}
	public void setSlotMaxItems(int slotMaxItems) {
		this.slotMaxItems = slotMaxItems;
	}
	public String getRdAddressId() {
		return rdAddressId;
	}
	public void setRdAddressId(String rdAddressId) {
		this.rdAddressId = rdAddressId;
	}
	public String getRemoteDeliveryPartnerPaymentAccount() {
		return remoteDeliveryPartnerPaymentAccount;
	}
	public void setRemoteDeliveryPartnerPaymentAccount(String remoteDeliveryPartnerPaymentAccount) {
		this.remoteDeliveryPartnerPaymentAccount = remoteDeliveryPartnerPaymentAccount;
	}
	public String getOrderRemoteDeliveryAddress() {
		return orderRemoteDeliveryAddress;
	}
	public void setOrderRemoteDeliveryAddress(String orderRemoteDeliveryAddress) {
		this.orderRemoteDeliveryAddress = orderRemoteDeliveryAddress;
	}
	public Double getSlotShipCharge() {
		return slotShipCharge;
	}
	public void setSlotShipCharge(double slotShipCharge) {
		this.slotShipCharge = slotShipCharge;
	}
	
	 public double getSlotMOVForFreeShipCharge() {
			return slotMOVForFreeShipCharge;
		}
		public void setSlotMOVForFreeShipCharge(double slotMOVForFreeShipCharge) {
			this.slotMOVForFreeShipCharge = slotMOVForFreeShipCharge;
		}
		public ArrayList<Object> getDeliverySlotRestrictions() {
			return deliverySlotRestrictions;
		}
		public void setDeliverySlotRestrictions(ArrayList<Object> deliverySlotRestrictions) {
			this.deliverySlotRestrictions = deliverySlotRestrictions;
		}
	
	    
}