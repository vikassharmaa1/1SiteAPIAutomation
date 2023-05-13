package orderprocess.api.response.pojo;

import java.util.ArrayList;

public class Root {
	public CustomerCredits customerCredits;
    public boolean hasAvailableCredit;
    public String orderId;
    public double orderDiscount;
    public ArrayList<AutoAppliedPromosWithNoPromoCode> autoAppliedPromosWithNoPromoCodes;
    public double orderSubTotal;
	public CustomerCredits getCustomerCredits() {
		return customerCredits;
	}
	public void setCustomerCredits(CustomerCredits customerCredits) {
		this.customerCredits = customerCredits;
	}
	public boolean isHasAvailableCredit() {
		return hasAvailableCredit;
	}
	public void setHasAvailableCredit(boolean hasAvailableCredit) {
		this.hasAvailableCredit = hasAvailableCredit;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getOrderDiscount() {
		return orderDiscount;
	}
	public void setOrderDiscount(double orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
	public ArrayList<AutoAppliedPromosWithNoPromoCode> getAutoAppliedPromosWithNoPromoCodes() {
		return autoAppliedPromosWithNoPromoCodes;
	}
	public void setAutoAppliedPromosWithNoPromoCodes(
			ArrayList<AutoAppliedPromosWithNoPromoCode> autoAppliedPromosWithNoPromoCodes) {
		this.autoAppliedPromosWithNoPromoCodes = autoAppliedPromosWithNoPromoCodes;
	}
	public double getOrderSubTotal() {
		return orderSubTotal;
	}
	public void setOrderSubTotal(double orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
	}
}
