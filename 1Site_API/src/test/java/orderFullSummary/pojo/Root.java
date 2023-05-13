package orderFullSummary.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import orderprocess.api.response.pojo.CustomerCredits;

public class Root{
    public String serviceType;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String dlAddressId;
    public String catalogId;
    public String orderId;
    public Integer totalQty;
    public Double orderShipping;
    public OrderAttributes orderAttributes;
    public String storeId;
    public String overallMOV;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String ccLocationId;
    public List<OrderItem> orderItems;
    public String colWebstoreId;
    public Integer orderSaving;
    public Number orderSubTotal;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String suburbOfSPOnlyLocalisation;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String postcodeOfSPOnlyLocalisation;
    public boolean freeDelivery ;
    public Double orderDiscount;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public CustomerCredits customerCredits;
    public ArrayList<Object> baggingItems;
    public PaymentMethod paymentMethod;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public String orderModificationCount ;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public OrderAddress orderAddress;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public Boolean orderModifiedFlag;
   
	public List<AutoAppliedPromosWithNoPromoCodes> autoAppliedPromosWithNoPromoCodes ;
    
	public String getDlAddressId() {
		return dlAddressId;
	}
	public void setDlAddressId(String dlAddressId) {
		this.dlAddressId = dlAddressId;
	}
	public String getSuburbOfSPOnlyLocalisation() {
		return suburbOfSPOnlyLocalisation;
	}
	public void setSuburbOfSPOnlyLocalisation(String suburbOfSPOnlyLocalisation) {
		this.suburbOfSPOnlyLocalisation = suburbOfSPOnlyLocalisation;
	}
	public String getPostcodeOfSPOnlyLocalisation() {
		return postcodeOfSPOnlyLocalisation;
	}
	public void setPostcodeOfSPOnlyLocalisation(String postcodeOfSPOnlyLocalisation) {
		this.postcodeOfSPOnlyLocalisation = postcodeOfSPOnlyLocalisation;
	}
	public String getServiceType() {
		return serviceType;
	}
	public String getOverallMOV() {
		return overallMOV;
	}
	public void setOverallMOV(String overallMOV) {
		this.overallMOV = overallMOV;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	public Double getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(Double orderShipping) {
       // BigDecimal ship = new BigDecimal(orderShipping).setScale(2, RoundingMode.FLOOR);
        String pattern = ".##";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        final String output = myFormatter.format(orderShipping);
        this.orderShipping = Double.valueOf(output);//BigDecimal.valueOf(orderShipping).setScale(2, RoundingMode.FLOOR).doubleValue();
	}
	public OrderAttributes getOrderAttributes() {
		return orderAttributes;
	}
	public void setOrderAttributes(OrderAttributes orderAttributes) {
		this.orderAttributes = orderAttributes;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getCcLocationId() {
		return ccLocationId;
	}
	public void setCcLocationId(String ccLocationId) {
		this.ccLocationId = ccLocationId;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getColWebstoreId() {
		return colWebstoreId;
	}
	public void setColWebstoreId(String colWebstoreId) {
		this.colWebstoreId = colWebstoreId;
	}
	public Integer getOrderSaving() {
		return orderSaving;
	}
	public void setOrderSaving(Integer orderSaving) {
		this.orderSaving = orderSaving;
	}
	public Number getOrderSubTotal() {
		return orderSubTotal;
	}
	public void setOrderSubTotal(Number orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
	}
	
	 public boolean isFreeDelivery() {
			return freeDelivery;
		}
		public void setFreeDelivery(boolean freeDelivery) {
			this.freeDelivery = freeDelivery;
		}
		public Double getOrderDiscount() {
			return orderDiscount;
		}
		public void setOrderDiscount(Double orderDiscount) {
			this.orderDiscount = orderDiscount;
		}
		public List<AutoAppliedPromosWithNoPromoCodes> getAutoAppliedPromosWithNoPromoCodes() {
			return autoAppliedPromosWithNoPromoCodes;
		}
		public void setAutoAppliedPromosWithNoPromoCodes(
				List<AutoAppliedPromosWithNoPromoCodes> autoAppliedPromosWithNoPromoCodes) {
			this.autoAppliedPromosWithNoPromoCodes = autoAppliedPromosWithNoPromoCodes;
		}
		public String getOrderModificationCount() {
			return orderModificationCount;
		}
		public void setOrderModificationCount(String orderModificationCount) {
			this.orderModificationCount = orderModificationCount;
		}
		public OrderAddress getOrderAddress() {
			return orderAddress;
		}
		public void setOrderAddress(OrderAddress orderAddress) {
			this.orderAddress = orderAddress;
		}
		public Boolean getOrderModifiedFlag() {
			return orderModifiedFlag;
		}
		public void setOrderModifiedFlag(Boolean orderModifiedFlag) {
			this.orderModifiedFlag = orderModifiedFlag;
		}
	
	
}

