package slots.api.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlotBaggingOption {

	public String bagDescription;
    public int bagBitFlag;
    public Object bagUnitPrice;
    public Object bagSupportText;
    public String bagSKU;
    public boolean isFlatFee;
    public String relationalSKU;
    public int bagMaxWeight;
    public String relationalBagType;
    public String bagHeaderDisplay;
    public String bagType;
    
	public String getBagDescription() {
		return bagDescription;
	}
	public void setBagDescription(String bagDescription) {
		this.bagDescription = bagDescription;
	}
	public int getBagBitFlag() {
		return bagBitFlag;
	}
	public void setBagBitFlag(int bagBitFlag) {
		this.bagBitFlag = bagBitFlag;
	}
	public Object getBagUnitPrice() {
		return bagUnitPrice;
	}
	public void setBagUnitPrice(Object bagUnitPrice) {
		this.bagUnitPrice = bagUnitPrice;
	}
	public Object getBagSupportText() {
		return bagSupportText;
	}
	public void setBagSupportText(Object bagSupportText) {
		this.bagSupportText = bagSupportText;
	}
	public String getBagSKU() {
		return bagSKU;
	}
	public void setBagSKU(String bagSKU) {
		this.bagSKU = bagSKU;
	}
	public boolean isFlatFee() {
		return isFlatFee;
	}
	public void setFlatFee(boolean isFlatFee) {
		this.isFlatFee = isFlatFee;
	}
	public String getRelationalSKU() {
		return relationalSKU;
	}
	public void setRelationalSKU(String relationalSKU) {
		this.relationalSKU = relationalSKU;
	}
	public int getBagMaxWeight() {
		return bagMaxWeight;
	}
	public void setBagMaxWeight(int bagMaxWeight) {
		this.bagMaxWeight = bagMaxWeight;
	}
	public String getRelationalBagType() {
		return relationalBagType;
	}
	public void setRelationalBagType(String relationalBagType) {
		this.relationalBagType = relationalBagType;
	}
	public String getBagHeaderDisplay() {
		return bagHeaderDisplay;
	}
	public void setBagHeaderDisplay(String bagHeaderDisplay) {
		this.bagHeaderDisplay = bagHeaderDisplay;
	}
	public String getBagType() {
		return bagType;
	}
	public void setBagType(String bagType) {
		this.bagType = bagType;
	}
	
    
}
