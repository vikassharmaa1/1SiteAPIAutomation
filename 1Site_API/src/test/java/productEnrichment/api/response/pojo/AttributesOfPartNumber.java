package productEnrichment.api.response.pojo;

import java.util.List;

public class AttributesOfPartNumber {
	public Boolean weightedItemIndicator;
	public Boolean weigthedByEach;
	public List<String> deliveryRestrictions;
	public List<String> barcodeID;
	public Boolean excludedFromSubstitution;
	public Boolean sensitiveProduct;
	public Boolean hideProductSavingsFlag;
	public Boolean hideColesPromotionIconFlag;
	
	public Boolean ishideProductSavingsFlag() {
		return hideProductSavingsFlag;
	}

	public void sethideProductSavingsFlag(Boolean hideProductSavingsFlag) {
		this.hideProductSavingsFlag = hideProductSavingsFlag;
	}
	
	public Boolean ishideColesPromotionIconFlag() {
		return hideColesPromotionIconFlag;
	}

	public void sethideColesPromotionIconFlag(Boolean hideColesPromotionIconFlag) {
		this.hideColesPromotionIconFlag = hideColesPromotionIconFlag;
	}
	

	protected Boolean getSensitiveProduct() {
		return sensitiveProduct;
	}

	protected void setSensitiveProduct(Boolean sensitiveProduct) {
		this.sensitiveProduct = sensitiveProduct;
	}

	public Boolean isWeightedItemIndicator() {
		return weightedItemIndicator;
	}

	public void setWeightedItemIndicator(Boolean weightedItemIndicator) {
		this.weightedItemIndicator = weightedItemIndicator;
	}

	public Boolean isWeigthedByEach() {
		return weigthedByEach;
	}

	public void setWeigthedByEach(Boolean weigthedByEach) {
		this.weigthedByEach = weigthedByEach;
	}

	public List<String> getDeliveryRestrictions() {
		return deliveryRestrictions;
	}

	public void setDeliveryRestrictions(List<String> deliveryRestrictions) {
		this.deliveryRestrictions = deliveryRestrictions;
	}

	public List<String> getBarcodeID() {
		return barcodeID;
	}

	public void setBarcodeID(List<String> barcodeID) {
		this.barcodeID = barcodeID;
	}

	public Boolean isExcludedFromSubstitution() {
		return excludedFromSubstitution;
	}

	public void setExcludedFromSubstitution(Boolean excludedFromSubstitution) {
		this.excludedFromSubstitution = excludedFromSubstitution;
	}

}
