package orderprocess.api.response.pojo;

import java.util.ArrayList;

public class CustomerCredits {
	public int totalAvailableAmount;
    public double totalAppliedAmount;
    public ArrayList<CustomerCredit> customerCredit;
	public int getTotalAvailableAmount() {
		return totalAvailableAmount;
	}
	public void setTotalAvailableAmount(int totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
	}
	public double getTotalAppliedAmount() {
		return totalAppliedAmount;
	}
	public void setTotalAppliedAmount(double totalAppliedAmount) {
		this.totalAppliedAmount = totalAppliedAmount;
	}
	public ArrayList<CustomerCredit> getCustomerCredit() {
		return customerCredit;
	}
	public void setCustomerCredit(ArrayList<CustomerCredit> customerCredit) {
		this.customerCredit = customerCredit;
	}

}
