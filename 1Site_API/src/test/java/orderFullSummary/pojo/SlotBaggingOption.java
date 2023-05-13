package orderFullSummary.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class SlotBaggingOption {
	public String bagDescription;
    public int bagBitFlag;
    public String bagSupportText;
    public String bagSKU;
    public double bagMaxWeight;
    public String bagHeaderDisplay;
    public String bagType;
    @JsonIgnoreProperties (ignoreUnknown = true)
    public double bagUnitPrice;
    
	public double getBagUnitPrice() {
		return bagUnitPrice;
	}
	public void setBagUnitPrice(double bagUnitPrice) {
		this.bagUnitPrice = bagUnitPrice;
	}
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
	public String getBagSupportText() {
		return bagSupportText;
	}
	public void setBagSupportText(String bagSupportText) {
		this.bagSupportText = bagSupportText;
	}
	public String getBagSKU() {
		return bagSKU;
	}
	public void setBagSKU(String bagSKU) {
		this.bagSKU = bagSKU;
	}
	public double getBagMaxWeight() {
		return bagMaxWeight;
	}
	public void setBagMaxWeight(double bagMaxWeight) {
		this.bagMaxWeight = bagMaxWeight;
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
